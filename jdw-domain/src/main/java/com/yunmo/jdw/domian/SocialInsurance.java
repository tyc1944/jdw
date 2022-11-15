package com.yunmo.jdw.domian;

import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Entity
@Builder
@NoArgsConstructor
@AutoValueDTO
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SocialInsurance extends CreatTime{
    //社保
    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String socialSecurityNumber;

    @Column(columnDefinition = "varchar(255)")
    private String name;

    @Column(columnDefinition = "bigint")
    private Long companyId;

    @Column(columnDefinition = "varchar(255)")
    private String company;


}
