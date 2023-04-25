package com.alsvietnam.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Duc_Huy
 * Date: 6/9/2022
 * Time: 1:10 AM
 */

public class EnumConst {

    public enum LogTypeEnum {
        ROLE,
        USER,
        TOPIC,
        DONATION,
        TEAM,
        ARTICLE,
        FILE,
        TASK,
        DONATION_CAMPAIGN,
        TOP_ORGANIZATION_SUPPORT,
        STORY,
        HONORED_TABLE,
        NOTIFICATION,
    }

    @AllArgsConstructor
    public enum LanguageTypeEnum {
        EN("English"),
        VI("Vietnamese");

        @Getter
        private final String description;
    }

    @AllArgsConstructor
    public enum ArticleStatusEnum {
        DRAFT("Bản thảo"),
        PUBLISHED("Đã đăng"),
        DELETED("Đã xóa");

        @Getter
        private final String description;
    }

    @AllArgsConstructor
    public enum PaymentStatusEnum {
        PENDING("wait for pay"),
        SUCCESS("pay successful"),
        FAILED("pay failed"),
        CANCELED("pay canceled");

        @Getter
        private final String description;
    }

    public enum PaymentGatewayEnum {
        VNPAY,
        MOMO,
    }

    @AllArgsConstructor
    public enum FileTypeEnum {
        JPG(".jpg"),
        JPEG(".jpeg"),
        PNG(".png"),
        GIF(".gìf"),
        TIFF(".tiff"),
        PSD(".psd"),
        PDF(".pdf"),
        AI(".ai"),
        RAW(".raw"),
        DOCX(".docx");

        @Getter
        private final String description;
    }

    @AllArgsConstructor
    public enum StorageDestinationEnum {
        ARTICLE("article/");


        @Getter
        private final String description;
    }

    @AllArgsConstructor
    public enum FileStorageEnum {
        DOC("documentation");

        @Getter
        private final String description;
    }

    @AllArgsConstructor
    public enum TaskStatusEnum {
        NEW("mới tạo"),
        DOING("đang làm"),
        DONE("đã xong"),
        COMPLETED("hoàn thành"),
        CLOSED("đóng");

        @Getter
        private final String description;
    }

    public enum RoleEnum {
        ROLE_MEMBER,
        ROLE_ADMIN,
        ROLE_MANAGER,
        ROLE_VOLUNTEER,
        ROLE_LEADER;

        public static final String MEMBER = "ROLE_MEMBER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String VOLUNTEER = "ROLE_VOLUNTEER";
        public static final String LEADER = "ROLE_LEADER";
    }

    public enum MailProfileEnum {
        GMAIL,
        SEND_GRID_MAIL,
    }

    public enum DonationTypeEnum {
        GENERAL,
        CAMPAIGN,
    }

    public enum VerifyEmailType {
        CONFIRM_EMAIL,
        FORGOT_PASSWORD
    }
}
