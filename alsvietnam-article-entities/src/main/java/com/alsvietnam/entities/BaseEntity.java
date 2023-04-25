package com.alsvietnam.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 5:10 PM
 */

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class BaseEntity implements Serializable {

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;

}
