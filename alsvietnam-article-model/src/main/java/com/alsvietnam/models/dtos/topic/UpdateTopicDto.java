package com.alsvietnam.models.dtos.topic;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UpdateTopicDto extends CreateTopicDto {

    @NotBlank(message = "id không được để trống")
    private String id;

    private Boolean deleted;

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
        if (deleted != null && !deleted) {
            this.deleted = null;
        }
    }
}
