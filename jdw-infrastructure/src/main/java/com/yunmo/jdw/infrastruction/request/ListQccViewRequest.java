package com.yunmo.jdw.infrastruction.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListQccViewRequest extends BaseSearchRequest {



  private String qccType;
}