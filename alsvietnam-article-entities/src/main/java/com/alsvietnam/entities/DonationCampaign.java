package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:08 AM
 */

@Entity
@Table(name = "donation_campaign")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DonationCampaign extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    private String title;

    private String description;

    private String coverImage;

    private String subImage;

    private Date dateStart;

    private Date dateEnd;

    private BigDecimal expectedAmount;

    private BigDecimal currentAmount;

    private boolean active;

    private boolean deleted;
}
