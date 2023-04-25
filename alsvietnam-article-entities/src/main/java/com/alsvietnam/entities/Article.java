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

@Entity
@Table(name = "article")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@NamedEntityGraph(
        name = "article-graph",
        attributeNodes = {
                @NamedAttributeNode("articleFiles"),
                @NamedAttributeNode("articleContents"),
                @NamedAttributeNode("articleMedias"),
        }
)
public class Article extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "article_id")
    @Access(AccessType.PROPERTY)
    private String id;

    private String author;

    private String label;

    private String status;

    private String translator;

    private Boolean deleted;

    private Integer likeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<ArticleFile> articleFiles;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<ArticleContent> articleContents;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<ArticleMedia> articleMedias;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "article_reaction",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
