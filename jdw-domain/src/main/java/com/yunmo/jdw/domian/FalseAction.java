package com.yunmo.jdw.domian;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yunmo.generator.annotation.AutoValueDTO;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.Map;


@Data
@Entity
@Builder
@NoArgsConstructor
@AutoValueDTO
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FalseAction extends CreatTime{
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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public FalseAction() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FalseAction(Long id, String title, String plaintiff, String defendant, String content, String sign) {
        this.id = id;
        this.title = title;
        this.plaintiff = plaintiff;
        this.defendant = defendant;
        this.content = content;
        this.sign = sign;
    }

    @Column(columnDefinition = "longtext")
    private String sign;//标记

}
