package com.hrm.system.dao;


import com.hrm.entity.system.User;
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
public interface UserDao extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {

}
