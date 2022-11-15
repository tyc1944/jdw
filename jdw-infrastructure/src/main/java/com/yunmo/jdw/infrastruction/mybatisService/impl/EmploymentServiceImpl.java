package com.yunmo.jdw.infrastruction.mybatisService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunmo.jdw.domian.Employment;
import com.yunmo.jdw.infrastruction.mapper.EmploymentMapper;
import com.yunmo.jdw.infrastruction.mybatisService.EmploymentService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【employment_form】的数据库操作Service实现
* @createDate 2022-06-20 16:19:44
*/
@Service
public class EmploymentServiceImpl extends ServiceImpl<EmploymentMapper, Employment>
    implements EmploymentService {

}




