package com.alsvietnam.models.dtos.topOrganizationSupport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTopOrganizationSupportDto {

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    private String image;

    private String description;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
