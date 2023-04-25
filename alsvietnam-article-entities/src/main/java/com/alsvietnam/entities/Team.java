package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 9:35 PM
 */

@Entity
@Table(name = "team")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "team_id")
    @Access(AccessType.PROPERTY)
    private String id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @ManyToMany(mappedBy = "teams")
    private Set<User> users;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Task> tasks;

    // getter, setter custom

    public void addUser(User user) {
        this.users.add(user);
        user.getTeams().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getTeams().remove(this);
    }
}
