package com.hrm.company.dao;


import com.hrm.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/07
 */
public interface CompanyDao extends JpaRepository<Company,String>, JpaSpecificationExecutor<Company> {

}
