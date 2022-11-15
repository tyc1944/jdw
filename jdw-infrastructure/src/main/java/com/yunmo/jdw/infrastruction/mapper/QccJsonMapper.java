package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.domian.QccJson;
import com.yunmo.jdw.infrastruction.request.ListQccViewRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Administrator
* @description 针对表【qcc_form】的数据库操作Mapper
* @createDate 2022-06-20 13:35:03
* @Entity com.yunmo.jdw.domain.QccForm
*/

@Mapper
public interface QccJsonMapper extends BaseMapper<QccJson> {

    IPage<QccJson> findByAllSearchQccJsonList(@Param("listQccViewRequest")ListQccViewRequest listQccViewRequest, Page page);

    IPage<QccJson> findBySignOnlineList(@Param("sign") String sign,Page page);
}




