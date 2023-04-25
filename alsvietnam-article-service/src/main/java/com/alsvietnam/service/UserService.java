package com.alsvietnam.service;

import com.alsvietnam.entities.User;
import com.alsvietnam.entities.VerificationEmail;
import com.alsvietnam.model.ResetPasswordModel;
import com.alsvietnam.models.dtos.user.*;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;

import java.util.List;

/**
 * Duc_Huy
 * Date: 9/3/2022
 * Time: 11:12 PM
 */
public interface UserService {

    ListWrapper<UserDto> searchUsers(ParameterSearchUser searchUser);

    UserDto createUser(CreateUserDto user);

    UserDto createGetInvolve(CreateGetInvolveUserDto user);

    void updateUserRole(String username, String roleName);

    String getUserIdLogin();

    String getUsernameLogin();

    User getUserLogin();

    UserDto updateUser(UpdateUserDto user, Boolean updateRequest);

    void deleteUser(String userId, Boolean updateRequest);

    void deleteUser(User user);

    void hardDeleteUser(User user);

    void updatePassword(UpdateUserPassword model);

    void verifyAccount(String userId, String verifyId);

    void resendMail(String userId);

    VerificationEmail requestResetPassword(String username);

    void resetPassword(ResetPasswordModel resetPasswordModel);

    List<UserDto> statisticTask(ParameterSearchTask parameterSearchTask, boolean buildTeam, boolean buildRole);
}
