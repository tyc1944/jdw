package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.domian.FalseAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FalseActionMapper extends BaseMapper<FalseAction> {
    IPage<FalseAction> findByNearRepeatCompany(Page page);

    IPage<FalseAction> findBySearchRepeatCompany(@Param("searchInfo") String searchInfo, Page page);

    IPage<FalseAction> findBySignOnlineList(@Param("sign") String sign, Page page);
}
