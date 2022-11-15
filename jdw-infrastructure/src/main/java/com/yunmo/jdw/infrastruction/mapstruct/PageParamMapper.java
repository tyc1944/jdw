package com.yunmo.jdw.infrastruction.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunmo.jdw.infrastruction.request.BaseSearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PageParamMapper {
  @Mapping(target = "total", ignore = true)
  @Mapping(target = "searchCount", ignore = true)
  @Mapping(target = "records", ignore = true)
  @Mapping(target = "pages", ignore = true)
  @Mapping(target = "orders", ignore = true)
  @Mapping(target = "optimizeCountSql", ignore = true)
  @Mapping(target = "countId",ignore = true)
  @Mapping(target = "maxLimit",ignore = true)
  Page mapper(BaseSearchRequest param);
}
