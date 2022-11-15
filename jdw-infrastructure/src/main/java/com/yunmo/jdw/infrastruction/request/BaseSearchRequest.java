package com.yunmo.jdw.infrastruction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseSearchRequest {
  private String searchField;

  @NotNull(
      groups = {BaseSearchRequestVaGroup.class},
      message = "每页数量不能为空")
  @Null(
      groups = {AllRecordSearchRequestVaGroup.class},
      message = "全量获取参数,不支持分页")
  private Long size;

  @NotNull(
      groups = {BaseSearchRequestVaGroup.class},
      message = "当前页不能为空")
  @Null(
      groups = {AllRecordSearchRequestVaGroup.class},
      message = "全量获取参数,不支持分页")
  private Long current;

  public interface BaseSearchRequestVaGroup {}

  public interface AllRecordSearchRequestVaGroup {}
}
