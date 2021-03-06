package com.hrm.system.feign;

import com.hrm.common.entity.Result;
import com.hrm.entity.company.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  通过Feign调用其他微服务
 * </p>
 *
 * @author guozy
 * @create 2019/09/29
 */
//声明调用的微服务名称
@FeignClient("hrm-company")
public interface DepartmentFeign {

    @GetMapping(value = "/company/departments/{id}")
    Result findById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/company/departments/search")
    Department findByCode(@RequestParam("code")String code, @RequestParam("companyId") String companyId);

}
