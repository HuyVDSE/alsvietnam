package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Duc_Huy
 * Date: 10/25/2022
 * Time: 11:09 PM
 */

@Entity
@Table(name = "honored_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@NamedEntityGraph(
        name = "honored-table-graph",
        attributeNodes = {
                @NamedAttributeNode("honoredUsers")
        }
)
public class HonoredTable extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String title;

    private Long quarter;

    private Long year;

    private boolean active;

    private boolean deleted;

    @OneToMany(mappedBy = "honoredTable", cascade = CascadeType.ALL)
    private Set<HonoredUser> honoredUsers;

}
