package com.yunmo.jdw.infrastruction.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * 填充器
 *
 * @author nieqiurong 2018-08-10 22:59:23.
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdDate", Instant.class, Instant.now()); // 起始版本 3.3.0(推荐使用)
        this.strictUpdateFill(metaObject, "lastModifiedDate", Instant.class, Instant.now()); // 起始版本 3.3.0(推荐)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "lastModifiedDate", Instant.class, Instant.now()); // 起始版本 3.3.0(推荐)
    }
}
