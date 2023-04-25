package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.User;
import com.alsvietnam.entities.VerificationEmail;
import com.alsvietnam.model.LoginRequest;
import com.alsvietnam.model.LoginResponse;
import com.alsvietnam.model.ResetPasswordModel;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.security.CustomUserDetails;
import com.alsvietnam.security.filter.JwtTokenProvider;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 4:02 PM
 */

@RestController
@RequestMapping(Constants.AUTHENTICATE_SERVICE)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticateController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Operation(summary = "Đăng nhập", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    })
    @PostMapping("/login")
    public ObjectResponseWrapper authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
        User user = customUserDetails.getUser();

        if (Boolean.FALSE.equals(user.getStatus())) {
            userService.resendMail(user.getId());
            return ObjectResponseWrapper.builder().status(1)
                    .message("Unverified")
                    .data(user.getId())
                    .build();
        }

        if (Boolean.FALSE.equals(user.getApproveStatus()) &&
                !EnumConst.RoleEnum.ROLE_MEMBER.toString().equals(user.getRole().getName())) {
            return ObjectResponseWrapper.builder().status(2)
                    .message("Didn't approved")
                    .data(user.getId())
                    .build();
        }

        String jwt = tokenProvider.generateAccessToken(user);
        return ObjectResponseWrapper.builder().status(3)
                .data(LoginResponse.builder()
                    .accessToken(jwt)
                    .tokenType("Bearer")
                    .build())
                .build();
    }

    @Operation(summary = "Tạo mã xác thực quên mật khẩu cho API reset password", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @GetMapping("/forgot-password")
    public ObjectResponseWrapper requestResetPassword(@RequestParam("username") String username) {
        VerificationEmail verificationEmail = userService.requestResetPassword(username);
        return ObjectResponseWrapper.builder().status(1)
                .message("Send verify code success, please check your email")
                .build();
    }

    @Operation(summary = "Tạo lại mật khẩu", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PostMapping("/reset-password")
    public ObjectResponseWrapper resetPassword(@RequestBody @Valid ResetPasswordModel resetPasswordModel) {
        userService.resetPassword(resetPasswordModel);
        return ObjectResponseWrapper.builder().status(1)
                .message("Reset password success")
                .build();
    }

}
