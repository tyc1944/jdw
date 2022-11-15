package com.yunmo.jdw.infrastruction.mybatisService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunmo.jdw.domian.FalseAction;
import com.yunmo.jdw.infrastruction.mapper.FalseActionMapper;
import com.yunmo.jdw.infrastruction.mybatisService.FalseActionService;
import org.springframework.stereotype.Service;

@Service
public class FalseActionServiceImpl extends ServiceImpl<FalseActionMapper, FalseAction>
        implements FalseActionService {
}
