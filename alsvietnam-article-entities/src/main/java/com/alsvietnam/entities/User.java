package com.alsvietnam.entities;

import com.alsvietnam.utils.Extensions;
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
 * Date: 6/26/2022
 * Time: 9:40 PM
 */

@Entity
@Table(name = "user", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class User extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "user_id")
    @Access(AccessType.PROPERTY)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String phone;

    private String address;

    private Boolean deleted;

    private String major;

    private String avatar;

    private String socialLink;

    private String description;

    private Boolean status;

    private Boolean approveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "team_member",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teams;

    @OneToMany(mappedBy = "user")
    private Set<VerificationEmail> verificationEmails;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "users")
    private Set<Article> articleReactions;

    @ManyToMany(mappedBy = "users")
    private Set<Comment> commentReactions;

    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user")
    private Set<Article> articles;

    @OneToMany(mappedBy = "user")
    private Set<HonoredUser> honoredUsers;

    @OneToMany(mappedBy = "user")
    private Set<Donation> donations;

    @OneToMany(mappedBy = "user")
    private Set<Story> stories;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "manager")
    private Set<Task> manageTasks;

    // getter, setter custom

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (!Extensions.isBlankOrNull(lastName)) {
            fullName.append(lastName).append(" ");
        }
        if (!Extensions.isBlankOrNull(middleName)) {
            fullName.append(middleName).append(" ");
        }
        if (!Extensions.isBlankOrNull(firstName)) {
            fullName.append(firstName);
        }
        return fullName.toString().trim();
    }

    public void addTeam(Team team) {
        this.teams.add(team);
        team.getUsers().add(this);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
        team.getUsers().remove(this);
    }

    public void addArticleReaction(Article article) {
        this.articleReactions.add(article);
        article.getUsers().add(this);
    }

    public void removeArticleReaction(Article article) {
        this.articles.remove(article);
        article.getUsers().remove(this);
    }

    public void addCommentReaction(Comment comment) {
        this.commentReactions.add(comment);
        comment.getUsers().add(this);
    }

    public void removeCommentReaction(Comment comment) {
        this.commentReactions.remove(comment);
        comment.getUsers().remove(this);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.getUsers().add(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.getUsers().remove(this);
    }
}
