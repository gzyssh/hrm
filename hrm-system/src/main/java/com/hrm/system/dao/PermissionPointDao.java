package com.hrm.system.dao;

import com.hrm.entity.system.PermissionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 企业数据访问接口
*/
public interface PermissionPointDao extends JpaRepository<PermissionPoint, String>, JpaSpecificationExecutor<PermissionPoint> {

}