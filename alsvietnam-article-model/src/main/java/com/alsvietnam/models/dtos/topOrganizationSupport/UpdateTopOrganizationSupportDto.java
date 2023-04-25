package com.alsvietnam.models.dtos.topOrganizationSupport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTopOrganizationSupportDto extends CreateTopOrganizationSupportDto{

    private String id;

    private Boolean deleted;
}
