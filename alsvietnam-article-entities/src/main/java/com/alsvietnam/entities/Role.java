package com.alsvietnam.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 4:58 PM
 */

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "role_id")
    @Access(AccessType.PROPERTY)
    private String id;

    private String name;

    @Column(length = 20)
    private String label;

    private String createdBy;

    private Date createdAt;

}


