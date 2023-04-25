package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Duc_Huy
 * Date: 10/25/2022
 * Time: 11:23 PM
 */

@Entity
@Table(name = "honored_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HonoredUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "honored_table_id")
    private HonoredTable honoredTable;

    private String role;

    private String medal;

    private String description;

}
