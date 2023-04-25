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
 * Date: 11/20/2022
 * Time: 11:40 AM
 */

@Entity
@Table(name = "category_faq")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CategoryFaq extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String vnName;

    private String enName;

    @OneToMany(mappedBy = "categoryFaq", cascade = CascadeType.ALL)
    private Set<Faq> faqs;
}
