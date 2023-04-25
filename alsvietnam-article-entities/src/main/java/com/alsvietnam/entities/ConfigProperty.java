package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Duc_Huy
 * Date: 12/1/2022
 * Time: 10:54 PM
 */

@Entity
@Table(name = "config_property")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ConfigProperty extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String name;

    private String value;

}
