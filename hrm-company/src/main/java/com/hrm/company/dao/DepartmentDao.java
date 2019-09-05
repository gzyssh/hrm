package com.hrm.company.dao;


import com.hrm.entity.company.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
public interface DepartmentDao extends JpaRepository<Department,String>,JpaSpecificationExecutor<Department> {

}
