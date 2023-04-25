package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "top_organization_support")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TopOrganizationSupport extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String organizationName;

    private String image;

    private String description;

    private String imageName;

    private Boolean active;

    private Boolean deleted;
}
