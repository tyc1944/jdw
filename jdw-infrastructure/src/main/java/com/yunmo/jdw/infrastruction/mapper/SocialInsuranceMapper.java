package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunmo.jdw.domian.SocialInsurance;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【social_insurance_form】的数据库操作Mapper
* @createDate 2022-06-20 13:35:03
* @Entity com.yunmo.jdw.domain.SocialInsuranceForm
*/

@Mapper
public interface SocialInsuranceMapper extends BaseMapper<SocialInsurance> {
    
}




