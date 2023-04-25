package com.alsvietnam.models.profiles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileProfile {

    private String id;

    private String name;

    private String url;

    private String extension;

    private String mime;

    private String type;

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

    private Boolean primary;

    private Integer index;
}
