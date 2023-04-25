package com.alsvietnam.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "donation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Donation extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "donation_id")
    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String phone;

    private String paymentGateway;

    private Date paymentDate;

    private String transactionId;

    private BigDecimal amount;

    private String status;

    private String donationType;

    private BigDecimal generalFund;

    private BigDecimal websiteFund;

    private BigDecimal documentFund;

    private BigDecimal storyFund;

    private Boolean hiddenInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_account_id")
    private PaymentAccount paymentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_campaign_id")
    private DonationCampaign donationCampaign;
}
