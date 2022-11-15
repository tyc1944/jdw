package com.yunmo.jdw.domian;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.yunmo.generator.annotation.AutoValueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
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
@TableName(autoResultMap = true)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class QccJson extends CreatTime{

    @Id
    @GenericGenerator(name = "sequence_id", strategy = "com.yunmo.id.HibernateIdentifierGenerator")
    @GeneratedValue(generator = "sequence_id")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(columnDefinition = "varchar(255)")
    private String companyName;

    @Column(columnDefinition = "timestamp null")
    private Instant logoutTime;//注销时间

    @Column(columnDefinition = "timestamp null")
    private Instant penaltyTime;//行政处罚时间

    @Column(columnDefinition = "varchar(255)")
    private QccType qccType;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> result;

    @Column(columnDefinition = "longtext")
    private String sign;//标记
    public  enum QccType {
        malice,
        natural
    }
}
