package com.yunmo.jdw.domian;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;


@Data
@Entity
@Builder
@NoArgsConstructor
@AutoValueDTO
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Employment extends CreatTime{
    //用工
    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String cardId;

    @Column(columnDefinition = "varchar(255)")
    private String name;

    @Column(columnDefinition = "varchar(255)")
    private String gender;

    @Column(columnDefinition = "varchar(255)")
    private String company;

}
