package com.yunmo.jdw.domian;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.time.Instant;


@Data
@Entity
@Builder
@NoArgsConstructor
@AutoValueDTO
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class fine extends CreatTime{
    //企查查
    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String companyName;


    @Column(columnDefinition = "timestamp null")
    private Instant penaltyTime;//行政处罚时间

    @Column(columnDefinition = "varchar(500)")
    private String fineReason;

    @Column(columnDefinition = "float(15,2)")
    private Float fineMoney;

    @Column(columnDefinition = "varchar(500)")
    private String fineNo;
}
