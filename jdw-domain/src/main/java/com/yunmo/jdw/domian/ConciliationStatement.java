package com.yunmo.jdw.domian;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Date;

//调解书
@Data
@Entity
@Builder
@NoArgsConstructor
@AutoValueDTO
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ConciliationStatement extends CreatTime{
  //裁判文书网
  @Id
  @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
  @GeneratedValue(generator = "sequence_id")
  @Column(columnDefinition = "bigint")
  private Long id;

  @Column(columnDefinition = "varchar(255)")
  private String title;

  @Column(columnDefinition = "varchar(255)")
  private String plaintiff;//原告

  @Column(columnDefinition = "varchar(255)")
  private String defendant;//被告

  @Column(columnDefinition = "longtext")
  private String content;//详情

}
