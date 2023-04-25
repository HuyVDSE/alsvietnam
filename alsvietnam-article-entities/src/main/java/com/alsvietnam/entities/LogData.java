package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 11:17 PM
 */

@Entity
@Table(name = "log_data")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class LogData implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "log_id")
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    private String type;

    private String content;

    private String createdBy;

    private Date createdAt;

}
