package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Donation;
import com.alsvietnam.entities.Team;
import com.alsvietnam.entities.User;
import com.alsvietnam.models.dtos.user.CreateUserDto;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Duc_Huy
 * Date: 6/9/2022
 * Time: 12:53 AM
 */

@RestController
@RequestMapping("/init")
@Hidden
public class InitController extends BaseController {

    @GetMapping
    public ObjectResponseWrapper start() {
        return ObjectResponseWrapper.builder().status(1)
                .message("ALS Viet Nam Article Service REST API")
                .build();
    }

    @GetMapping("/roles")
    public ObjectResponseWrapper initRoles() {
        roleService.createRole("ROLE_ADMIN", "Admin");
        roleService.createRole("ROLE_MANAGER", "Manager");
        roleService.createRole("ROLE_MEMBER", "Member");
        roleService.createRole("ROLE_VOLUNTEER", "Volunteer");

        return ObjectResponseWrapper.builder().status(1).message("success").build();
    }

    @GetMapping("/users")
    public ObjectResponseWrapper initUser() {
        CreateUserDto userAdmin = CreateUserDto.builder()
                .username("huyvd")
                .password("admin@123")
                .email("huyvd.soft@gmail.com")
                .firstName("Huy")
                .role("ROLE_ADMIN")
                .build();
        userService.createUser(userAdmin);
        CreateUserDto userManager = CreateUserDto.builder()
                .username("manager")
                .password("manager@123")
                .email("manager@yopmail.com")
                .firstName("Manager")
                .role("ROLE_MANAGER")
                .build();
        userService.createUser(userManager);
        return ObjectResponseWrapper.builder().status(1).message("success").build();
    }

    @PutMapping("/donations")
    public ObjectResponseWrapper updateDonations() {
        List<Donation> donations = donationRepository.findAll();
        boolean needUpdate;
        for (Donation donation : donations) {
            needUpdate = false;
            if (donation.getHiddenInfo() == null) {
                donation.setHiddenInfo(false);
                needUpdate = true;
            }
            if (donation.getCreatedBy() == null) {
                donation.setCreatedBy("anonymousUser");
                needUpdate = true;
            }
            if (donation.getPaymentGateway() == null) {
                donation.setPaymentGateway(EnumConst.PaymentGatewayEnum.VNPAY.name());
                needUpdate = true;
            }
            if (donation.getGeneralFund() == null) {
                donation.setGeneralFund(donation.getAmount());
                donation.setDocumentFund(BigDecimal.ZERO);
                donation.setStoryFund(BigDecimal.ZERO);
                donation.setWebsiteFund(BigDecimal.ZERO);
                needUpdate = true;
            }

            if (needUpdate) {
                donationRepository.save(donation);
            }
        }
        return ObjectResponseWrapper.builder().status(1).message("success").build();
    }

    @DeleteMapping("/donations")
    public ObjectResponseWrapper deleteDonations() {
        List<Donation> donations = donationRepository.findAll();
        int size = donations.size();
        donationRepository.deleteAll(donations);
        return ObjectResponseWrapper.builder().status(1)
                .message(size + " donations deleted")
                .build();
    }

    @DeleteMapping("/teams")
    public ObjectResponseWrapper deleteTeams(@RequestParam("team_id") String teamIdList) {
        List<String> teamIds = Arrays.asList(teamIdList.split(","));
        Set<Team> teams = teamRepository.findByIdIn(teamIds);
        List<User> users = userRepository.findByTeams_IdIn(teamIds);
        for (User user : users) {
            for (Team team : teams) {
                user.getTeams().remove(team);
            }
            userRepository.save(user);
        }
        teamRepository.deleteAll(teams);
        return ObjectResponseWrapper.builder().status(1).message("success").build();
    }

    @DeleteMapping("/users")
    public ObjectResponseWrapper RemoveAllUserDeleted() {
        ParameterSearchUser searchUser = new ParameterSearchUser();
        searchUser.setDeleted(true);
        List<User> users = userRepository.searchUsers(searchUser).getData();
        for (User user : users) {
            userService.hardDeleteUser(user);
        }
        return ObjectResponseWrapper.builder()
                .status(1)
                .message("Remove success " + users.size() + " users")
                .build();
    }

}
