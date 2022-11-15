package com.yunmo.jdw.infrastruction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.domian.ConciliationStatement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Administrator
* @description 针对表【conciliation_statement】的数据库操作Mapper
* @createDate 2022-06-08 17:03:17
* @Entity com.yunmo.jdw.domain.ConciliationStatement
*/
@Mapper
public interface ConciliationStatementMapper extends BaseMapper<ConciliationStatement> {

    IPage<ConciliationStatement> findByRepeatCompanyPageList(Page page);
    List<ConciliationStatement> findByRepeatCompanyList(@Param("fileNameList") List<String> fileNameList);
    List<ConciliationStatement> findByEmploymentRepeatList(@Param("companyList") List<ConciliationStatement> companyList);
    List<ConciliationStatement> findBySocialRepeatList(@Param("companyList") List<ConciliationStatement> companyList);

}




