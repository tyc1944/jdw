package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunmo.jdw.domian.Employment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【employment_form】的数据库操作Mapper
* @createDate 2022-06-20 13:35:03
* @Entity com.yunmo.jdw.domain.EmploymentForm
*/

@Mapper
public interface EmploymentMapper extends BaseMapper<Employment> {
}




