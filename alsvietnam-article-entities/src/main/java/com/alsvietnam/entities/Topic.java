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
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Topic extends BaseEntity implements Serializable {

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String titleEnglish;

    private String titleVietnamese;

    private String topicParentId;

    private String description;

    private Boolean deleted;

    private Boolean active;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private Set<Article> articles;
}
