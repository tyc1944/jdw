package com.yunmo.jdw.api.resource;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.domian.ConciliationStatement;
import com.yunmo.jdw.domian.FalseAction;
import com.yunmo.jdw.domian.fine;
import com.yunmo.jdw.domian.QccJson;
import com.yunmo.jdw.infrastruction.mapper.ConciliationStatementMapper;
import com.yunmo.jdw.infrastruction.mapper.FalseActionMapper;
import com.yunmo.jdw.infrastruction.mapper.fineMapper;
import com.yunmo.jdw.infrastruction.mapper.QccJsonMapper;
import com.yunmo.jdw.infrastruction.service.QccApi;
import com.yunmo.jdw.infrastruction.request.ListQccViewRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Transactional
@Tag(name = "获取列表和单个数据相关")
public class CompanyListResource {

    @Autowired
    ConciliationStatementMapper conciliationStatementMapper;

    @Autowired
    QccJsonMapper qccJsonMapper;

    @Autowired
    fineMapper fineMapper;

    @Autowired
    QccApi qccApi;
    @Autowired
    FalseActionMapper falseActionMapper;

    @GetMapping("/conciliationStatement")
    @Operation(description = "获取全部裁判文书历史数据")
    public IPage<ConciliationStatement> getAllConciliationStatementList(Page page) {
        return conciliationStatementMapper.selectPage(page, null);
    }

    @GetMapping("/conciliationStatement/false")
    @Operation(description = "获取疑似虚假诉讼的裁判文书历史数据")
    public IPage<ConciliationStatement> getFalseConciliationStatementList(Page page) {
        return conciliationStatementMapper.findByRepeatCompanyPageList(page);
    }

    @GetMapping("/false/action/online")
    @Operation(description = "获取虚假诉讼实时数据")
    public IPage<FalseAction> getFalseActionOnlineList(String sign,Page page) {
        //String defendant = conciliationStatementMapper.findNewdefendant();
        return falseActionMapper.findBySignOnlineList(sign,page);
    }

    @GetMapping("/false/action")
    @Operation(description = "获取虚假诉讼历史数据")
    public IPage<FalseAction> getSearchFalseActionListAll(String searchField, Page page) {
        return falseActionMapper.findBySearchRepeatCompany(searchField, page);
    }

    @GetMapping("/false/action/{id}")
    @Operation(description = "获取单条虚假诉讼历史数据")
    public FalseAction getFalseAction(@PathVariable("id") Long id) {
        return falseActionMapper.selectById(id);
    }

    @GetMapping("/fine")
    @Operation(description = "获取全部行政处罚数据")
    public IPage<fine> getFineList(Page page) {
        return fineMapper.selectPage(page, null);
    }

    @GetMapping("/qcc/json/online")
    @Operation(description = "获取恶意注销实时数据")
    public IPage<QccJson> getQccJsonOnlineList(String sign, Page page) {
        return qccJsonMapper.findBySignOnlineList(sign,page);
    }

    @GetMapping("/qcc/json")
    @Operation(description = "获取恶意注销历史数据")
    public IPage<QccJson> getQccJsonSearchList(ListQccViewRequest listQccViewRequest, Page page) {
        return qccJsonMapper.findByAllSearchQccJsonList(listQccViewRequest, page);
    }

    @GetMapping("/qcc/json/{id}")
    @Operation(description = "获取单条恶意注销数据")
    public QccJson getQccJson(@PathVariable("id") Long id) {
        return qccJsonMapper.selectById(id);
    }

}
