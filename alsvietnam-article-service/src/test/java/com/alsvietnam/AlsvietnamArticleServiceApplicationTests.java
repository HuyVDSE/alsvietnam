package com.alsvietnam;

import com.alsvietnam.controller.AuthenticateController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.model.LoginRequest;
import com.alsvietnam.models.dtos.campaign.CampaignDto;
import com.alsvietnam.models.dtos.campaign.CreateCampaignDto;
import com.alsvietnam.models.dtos.task.CreateTaskDto;
import com.alsvietnam.models.dtos.user.UserDto;
import com.alsvietnam.models.profiles.TaskProfile;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.DonationCampaignService;
import com.alsvietnam.service.TaskService;
import com.alsvietnam.utils.Extensions;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlsvietnamArticleServiceApplicationTests {

    @Autowired
    TaskService taskService;

    @Autowired
    DonationCampaignService donationCampaignService;

    @Autowired
    AuthenticateController authenticateController;

    public void setup() {
        String username = "huyvd";
        String password = "admin@123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        authenticateController.authenticateUser(loginRequest);
    }

//
//    @Test
//    void testCreateTask_Success() {
//        CreateTaskDto taskDto = new CreateTaskDto();
//        taskDto.setName("Test create task");
//        taskDto.setDescription("This is description of task");
//        taskDto.setEndDate("2022-09-25T00-00-00");
//        taskDto.setTeamId("248048ea-8b19-4d5a-9364-365e90b06183");
//        taskDto.setManagerId("22fc3452-1f41-49d2-923e-8882fc4659f8");
//        taskDto.setArticleId("be5627be-6314-4bb5-94e9-7c4859cd38d8");
//        TaskProfile taskProfile = taskService.createTask(taskDto);
//        assertEquals(taskDto.getEndDate(), taskProfile.getEndDate());
//        assertEquals(taskDto.getTeamId(), taskProfile.getTeam().getId());
//        assertEquals(taskDto.getManagerId(), taskProfile.getManager().getId());
//        assertEquals(taskDto.getArticleId(), taskProfile.getArticleId());
//    }
//
//    @Test
//    void testCreateTaskWrongManagerThrow_thenAssertSucceed() {
//        CreateTaskDto taskDto = new CreateTaskDto();
//        taskDto.setName("Test create task");
//        taskDto.setDescription("This is description of task");
//        taskDto.setEndDate("2022-09-25T00-00-00");
//        taskDto.setTeamId("248048ea-8b19-4d5a-9364-365e90b06183");
//        taskDto.setManagerId("21485383-8aca-43a5-935c-dc782766f604");
//        taskDto.setArticleId("be5627be-6314-4bb5-94e9-7c4859cd38d8");
//        ServiceException serviceException = assertThrows(ServiceException.class, () -> taskService.createTask(taskDto));
//
//        String expectedMsg = "Manager not found!";
//        String actualMsg = serviceException.getMessage();
//        assertFalse(Extensions.isBlankOrNull(actualMsg));
//        if (actualMsg != null) {
//            assertTrue(actualMsg.contains(expectedMsg));
//        }
//    }
//
//    @Test
//    void testAddMemberToTask() {
//        String taskId = "d82f065c-8d3c-4a05-9895-a801044d310b";
//        String memberId = "c3e02471-b6fc-4df8-951a-a0624b9f8217";
//        taskService.addMemberToTask(taskId, memberId);
//
//        ParameterSearchTask parameterSearchTask = new ParameterSearchTask();
//        parameterSearchTask.setId(taskId);
//        parameterSearchTask.setStartIndex(0L);
//        parameterSearchTask.setPageSize(1);
//        ListWrapper<TaskProfile> wrapper = taskService.searchTask(parameterSearchTask);
//        TaskProfile taskProfile = wrapper.getData().get(0);
//        List<UserDto> users = taskProfile.getUsers();
//        String memberExist = "";
//        for (UserDto user : users) {
//            if (user.getId().equals(memberId)) {
//                memberExist = user.getId();
//                break;
//            }
//        }
//        assertEquals(memberId, memberExist);
//    }

//    @Test
//    void testCreateCampaign() {
//        CreateCampaignDto dto = new CreateCampaignDto();
//        dto.setTitle("For ALSVietNam Organization");
//        dto.setDescription("This is donation campaign");
//        dto.setCoverImage("https://google.com");
//        dto.setSubImages(Arrays.asList("https://fb.com", "https://instagram.com"));
//        dto.setDateStart("2022-10-18T00:00:00.000Z");
//        dto.setDateEnd("2022-10-20T00:00:00.000Z");
//        dto.setExpectedAmount(BigDecimal.valueOf(2500000));
//        dto.setCurrentAmount(BigDecimal.valueOf(500000));
//        CampaignDto campaign = donationCampaignService.createCampaign(dto);
//        assertEquals(dto.getTitle(), campaign.getTitle());
//        assertEquals(dto.getDescription(), campaign.getDescription());
//        assertEquals(dto.getCoverImage(), campaign.getCoverImage());
//        assertEquals(dto.getSubImages(), campaign.getSubImages());
//        assertEquals(dto.getDateStart(), campaign.getDateStart());
//        assertEquals(dto.getDateEnd(), campaign.getDateEnd());
//        assertEquals(dto.getExpectedAmount(), campaign.getExpectedAmount());
//        assertEquals(dto.getCurrentAmount(), campaign.getCurrentAmount());
//    }
}
