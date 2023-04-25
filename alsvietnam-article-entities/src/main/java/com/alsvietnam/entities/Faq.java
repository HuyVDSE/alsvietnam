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
 * Date: 11/20/2022
 * Time: 11:44 AM
 */

@Entity
@Table(name = "faq")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Faq extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "faq_id")
    @Access(AccessType.PROPERTY)
    private String id;

    private String vnQuestion;

    private String enQuestion;

    private String vnAnswer;

    private String enAnswer;

    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_faq_id")
    private CategoryFaq categoryFaq;

}
