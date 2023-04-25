package com.alsvietnam.service.impl;

import com.alsvietnam.entities.*;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.model.ResetPasswordModel;
import com.alsvietnam.models.dtos.ImageModel;
import com.alsvietnam.models.dtos.user.*;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.security.CustomUserDetails;
import com.alsvietnam.service.NotificationService;
import com.alsvietnam.service.UserService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.service.strategy.mail.MailService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 9/3/2022
 * Time: 11:16 PM
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
public class UserServiceImpl extends BaseService implements UserService, UserDetailsService {

    private NotificationService notificationService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final ObjectMapper objectMapper;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("Username " + username + " not found"));
        return new CustomUserDetails(user);
    }

    @Override
    public ListWrapper<UserDto> searchUsers(ParameterSearchUser searchParam) {
        log.info("Fetching users");
        ListWrapper<User> wrapper = userRepository.searchUsers(searchParam);
        List<UserDto> results = userConverter.toUserDTO(wrapper.getData(), searchParam);
        return ListWrapper.<UserDto>builder()
                .data(results)
                .total(wrapper.getTotal())
                .maxResult(wrapper.getMaxResult())
                .currentPage(wrapper.getCurrentPage())
                .totalPage(wrapper.getTotalPage())
                .build();
    }

    @SneakyThrows
    @Override
    public UserDto createUser(CreateUserDto dto) {
        log.info("Create new user {}", dto.getUsername());
        userRepository.findByUsername(dto.getUsername()).ifPresent(user -> {
            throw new ServiceException("Username " + user.getUsername() + " already exists");
        });
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ServiceException("Email " + user.getEmail() + " already exists");
        });
        User user = userConverter.fromCreateUserDTO(dto);
        Role role = roleRepository.findById(dto.getRole())
                .orElseThrow(() -> new ServiceException("Role " + dto.getRole() + " is not existed"));
        user.setRole(role);
        if (!Extensions.isNullOrEmpty(dto.getTeams())) {
            Set<Team> teams = teamRepository.findByIdIn(dto.getTeams());
            if (teams.isEmpty()) {
                throw new ServiceException("Teams is not existed");
            }
            if (teams.size() != dto.getTeams().size()) {
                Set<String> teamIds = teams.stream().map(Team::getName).collect(Collectors.toSet());
                for (String teamId : dto.getTeams()) {
                    if (!teamIds.contains(teamId)) {
                        throw new ServiceException("Team " + teamId + " is not existed");
                    }
                }
            }
            user.setTeams(teams);
        }

        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Create new user " + user.getUsername());

        // upload avatar
        if (dto.getAvatar() != null) {
            File file = fileService.convertMultipartToFile(dto.getAvatar());
            String url = fileStorageService.uploadFile(file, User.class.getSimpleName());

            JSONObject avatarObj = new JSONObject();
            avatarObj.put("name", file.getName());
            avatarObj.put("url", url);
            user.setAvatar(avatarObj.toString());

            fileService.deleteFileLocal(file);
        }
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Create user " + user.getUsername());
        return userConverter.toUserDTO(user, user.getRole() != null, !user.getTeams().isNullOrEmpty());
    }

    @Override
    public UserDto createGetInvolve(CreateGetInvolveUserDto dto) {
        log.info("Create new user get involve {}", dto.getEmail());
        userRepository.findByUsername(dto.getUsername()).ifPresent(user -> {
            throw new ServiceException("Username " + user.getUsername() + " already exists");
        });
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ServiceException("Email " + user.getEmail() + " already exists");
        });

        User user = userConverter.fromCreateGetInvolveUserDTO(dto);
        Role role = roleRepository.findById(dto.getRole())
                .orElseThrow(() -> new ServiceException("Role " + dto.getRole() + " is not existed"));
        user.setRole(role);

        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Create new user get involve " + user.getEmail());

        // notify to admin
        notificationService.notifyNewRequestVolunteer();
        return userConverter.toUserDTO(user, true, !user.getTeams().isNullOrEmpty());
    }

    @Override
    public void updateUserRole(String username, String roleName) {
        log.info("Update role {} to user {}", roleName, username);
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ServiceException("username " + username + " not found"));
        if (user.getRole() != null && user.getRole().getName().equals(roleName)) {
            log.info("Role doesn't change!");
            return;
        }
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new ServiceException("role " + roleName + " not found"));
        user.setRole(role);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Add role " + role.getName()
                + " to user " + user.getUsername());
    }

    @Override
    public String getUserIdLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (String) authentication.getCredentials();
    }

    @Override
    public String getUsernameLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (String) authentication.getPrincipal();
    }

    @Override
    public User getUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = (String) authentication.getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("username " + username + " not found"));
    }

    @Override
    @SneakyThrows
    public UserDto updateUser(UpdateUserDto model, Boolean updateRequest) {
        User user = userRepository.findByUsername(model.getUsername())
                .orElseThrow(() -> new ServiceException("User not found!"));

        if (updateRequest != null && updateRequest.equals(true)) {
            mailService.sendMailRequestMemberResult(user, true);
        }

        user = userConverter.fromUpdateUserDTO(user, model);
        if (model.getAvatar() != null) {
            File file = fileService.convertMultipartToFile(model.getAvatar());
            if (!user.getAvatar().isBlankOrNull()) {
                ImageModel image = objectMapper.readValue(user.getAvatar(), ImageModel.class);
                fileStorageService.deleteFile(image.getName(), User.class.getSimpleName());
            }
            String url = fileStorageService.uploadFile(file, User.class.getSimpleName());
            ImageModel image = new ImageModel(file.getName(), url);
            fileService.deleteFileLocal(file);
            user.setAvatar(objectMapper.writeValueAsString(image));
        }

        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Update user " + user.getUsername());
        return userConverter.toUserDTO(user, true, true);
    }

    @Override
    public void deleteUser(String userId, Boolean updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found!"));

        if (updateRequest != null && updateRequest.equals(false)) {
            mailService.sendMailRequestMemberResult(user, false);
        }

        deleteUser(user);
    }

    @Override
    public void deleteUser(User user) {
        if (user == null) {
            return;
        }
        log.info("delete user: {}", user.getId());
        user.setDeleted(true);
        user.setUpdatedAt(new Date());
        user.setUpdatedBy(userService.getUsernameLogin());
        // remove user from team
        List<Team> teams = teamRepository.findByUsers_Id(user.getId());
        for (Team team : teams) {
            team.removeUser(user);
            User leader = team.getLeader();
            if (leader.getId().equals(user.getId())) {
                team.setLeader(null);
            }
        }
        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Delete user " + user.getUsername());
    }

    @Override
    public void hardDeleteUser(User user) {
        log.info("Hard delete user {}", user.getId());
        log.info("Delete teams");
        for (Team team : user.getTeams()) {
            user.removeTeam(team);
        }
        log.info("Delete article reactions");
        for (Article articleReaction : user.getArticleReactions()) {
            user.removeArticleReaction(articleReaction);
        }
        log.info("Delete comment reactions");
        for (Comment commentReaction : user.getCommentReactions()) {
            user.removeCommentReaction(commentReaction);
        }
        log.info("Delete tasks");
        for (Task task : user.getTasks()) {
            user.removeTask(task);
        }
        log.info("Delete verification emails");
        if (!user.getVerificationEmails().isNullOrEmpty()) {
            verificationEmailRepository.deleteAll(user.getVerificationEmails());
        }
        if (!user.getComments().isNullOrEmpty()) {
            log.info("Delete comments");
            commentRepository.deleteAll(user.getComments());
        }
        if (!user.getHonoredUsers().isNullOrEmpty()) {
            log.info("Delete honored users");
            honoredUserRepository.deleteAll(user.getHonoredUsers());
        }
        if (!user.getDonations().isNullOrEmpty()) {
            log.info("Delete donations");
            donationRepository.deleteAll(user.getDonations());
        }
        if (!user.getArticles().isNullOrEmpty()) {
            log.info("Delete articles");
            for (Article article : user.getArticles()) {
                article.setUser(null);
            }
        }
        if (!user.getStories().isNullOrEmpty()) {
            log.info("Delete stories");
            storyRepository.deleteAll(user.getStories());
        }
        if (!user.getNotifications().isNullOrEmpty()) {
            log.info("Delete notifications");
            notificationRepository.deleteAll(user.getNotifications());
        }
        if (!user.getManageTasks().isNullOrEmpty()) {
            log.info("Delete manage tasks");
            taskRepository.deleteAll(user.getManageTasks());
        }
        userRepository.delete(user);
    }

    @Override
    public void updatePassword(UpdateUserPassword model) {
        if (!model.getNewPassword().equals(model.getConfirmPassword())) {
            throw new ServiceException("New password and confirm password not match");
        }
        User user = userRepository.findByUsername(model.getUsername())
                .orElseThrow(() -> new ServiceException("User not found!"));
        if (!passwordEncoder.matches(model.getOldPassword(), user.getPassword())) {
            throw new ServiceException("Old password not match");
        }
        user.setPassword(passwordEncoder.encode(model.getNewPassword()));
        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Update password for user " + user.getUsername());
    }

    @Override
    public void verifyAccount(String userId, String verifyCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found!"));
        VerificationEmail verificationEmail = verificationEmailRepository.findByVerifyCodeAndUserId(verifyCode, user.getId());

        if (user.getStatus().equals(true)) {
            return;
        }
        if (verificationEmail == null) {
            throw new ServiceException("Verification code is invalid");
        }
        if (verificationEmail.getExpiredAt().before(new Date())) {
            throw new ServiceException("Verification code is expired");
        }
        user.setStatus(true);
        userRepository.save(user);

        //delete all verify code relate to user
        verificationEmailRepository.deleteAllByUser(user);
    }

    @Override
    public void resendMail(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User not found!"));
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        String verifyCode = Integer.toString(random);
        VerificationEmail verificationEmail = verificationEmailConverter.fromUser(user, verifyCode);
        verificationEmailRepository.save(verificationEmail);

        mailService.sendVerifyCodeEmail(user.getEmail(), verifyCode, EnumConst.VerifyEmailType.CONFIRM_EMAIL);
    }

    @Override
    public VerificationEmail requestResetPassword(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException("User not found"));
        if (user.getEmail().isBlankOrNull()) {
            throw new ServiceException("User email is empty!");
        }
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        String verifyCode = Integer.toString(random);
        VerificationEmail verificationEmail = verificationEmailConverter.fromUser(user, verifyCode);
        verificationEmailRepository.save(verificationEmail);

        mailService.sendVerifyCodeEmail(user.getEmail(), verifyCode, EnumConst.VerifyEmailType.FORGOT_PASSWORD);
        return verificationEmail;
    }

    @Override
    public void resetPassword(ResetPasswordModel model) {
        User user = userRepository.findByEmail(model.getEmail())
                .orElseThrow(() -> new ServiceException("User by email " + model.getEmail() + " not exist"));

        if (user.getDeleted() != null || user.getStatus().equals(false)) {
            throw new ServiceException("Invalid user");
        }
        VerificationEmail verificationEmail = verificationEmailRepository.findByVerifyCodeAndUserId(model.getVerifyCode(), user.getId());
        if (verificationEmail == null) {
            throw new ServiceException("Invalid verify code");
        }
        if (verificationEmail.getExpiredAt().before(new Date())) {
            throw new ServiceException("Verify code is expired");
        }
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            throw new ServiceException("Confirm password not matched");
        }
        user.setPassword(passwordEncoder.encode(model.getPassword()));
        userRepository.save(user);
        logDataService.create(user.getId(), user.getClass().getSimpleName(), "Reset password of user " + user.getUsername());

        //delete all verify code relate to user
        verificationEmailRepository.deleteAllByUser(user);
    }

    @Override
    public List<UserDto> statisticTask(ParameterSearchTask parameterSearchTask, boolean buildTeam, boolean buildRole) {
        parameterSearchTask.setBuildTeam(false);
        parameterSearchTask.setBuildManager(false);
        List<Task> tasks = taskRepository.searchTask(parameterSearchTask).getData();
        if (tasks.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> userIds = tasks.stream()
                .map(Task::getUsers)
                .flatMap(Set::stream)
                .map(User::getId)
                .distinct().collect(Collectors.toList());
        ParameterSearchUser parameterSearchUser = new ParameterSearchUser();
        parameterSearchUser.setIds(userIds);
        parameterSearchUser.setBuildTeam(buildTeam);
        parameterSearchUser.setBuildRole(buildRole);
        List<User> users = userRepository.searchUsers(parameterSearchUser).getData();

        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = userConverter.toUserDTO(user, parameterSearchUser);
            for (Task task : tasks) {
                Set<User> usersDoTask = task.getUsers();
                if (usersDoTask == null) continue;
                for (User userDoTask : usersDoTask) {
                    if (userDoTask.getId().equals(user.getId())) {
                        userDto.addTaskCompleted(1);
                    }
                }
            }
            result.add(userDto);
        }
        return result.stream()
                .sorted(Comparator.comparing(UserDto::getNumberTaskCompleted).reversed())
                .collect(Collectors.toList());
    }

}
