package com.hrm.employee.dao;


import com.hrm.entity.employee.UserCompanyPersonal;
import com.hrm.entity.employee.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {

    UserCompanyPersonal findByUserId(String userId);

    @Query(value = "select new com.hrm.entity.employee.response.EmployeeReportResult(a,b) from UserCompanyPersonal a left join EmployeeResignation b " +
           "on a.userId=b.userId where a.companyId=?1 and a.timeOfEntry like?2 or (b.resignationTime like ?2)")
    List<EmployeeReportResult> findByReport(String companyId, String month);
}