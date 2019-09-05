package com.hrm.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
public class BaseService<T> {

    /**
     * 用户构造查询条件：
     * 1.只查询companyId
     * 2.很多地方都需要根据companyId查询
     * 3.很多对象都具有companyId
     * @param companyId
     * @return
     */
    protected Specification<T> getSpec(String companyId){
        Specification<T> specification=new Specification<T>() {
            /**
             * 用户构造查询条件
             * @param root ：包含了对象的所有属性数据
             * @param criteriaQuery ：一般不用
             * @param cb ：构造查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return specification;
    }
}
