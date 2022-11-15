package com.yunmo.jdw.infrastruction.jpa;

import com.yunmo.jdw.domian.QccJson;
import com.yunmo.jdw.domian.repository.QccJsonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QccJsonJpaRepository extends JpaRepository<QccJson, Long>, QccJsonRepository {

}