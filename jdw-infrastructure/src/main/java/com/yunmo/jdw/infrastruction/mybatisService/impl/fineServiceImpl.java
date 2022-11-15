package com.yunmo.jdw.infrastruction.mybatisService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunmo.jdw.domian.fine;
import com.yunmo.jdw.infrastruction.mapper.fineMapper;
import com.yunmo.jdw.infrastruction.mybatisService.fineService;
import org.springframework.stereotype.Service;

@Service
public class fineServiceImpl extends ServiceImpl<fineMapper, fine>
        implements fineService {
}
