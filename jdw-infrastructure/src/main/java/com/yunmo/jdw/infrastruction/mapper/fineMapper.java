package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.domian.fine;
import com.yunmo.jdw.domian.QccJson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface fineMapper extends BaseMapper<fine> {
    IPage<fine> findByNewSearchMaliciousList(Page page);

    List<fine> findFineListByCompanyName(@Param("qccJsonList") List<QccJson> qccJsonList);
}
