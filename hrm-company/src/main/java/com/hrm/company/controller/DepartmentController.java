package com.hrm.company.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.company.service.CompanyService;
import com.hrm.company.service.DepartmentService;
import com.hrm.entity.company.Company;
import com.hrm.entity.company.Department;
import com.hrm.entity.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@CrossOrigin
@RestController
@RequestMapping("company")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;

    /**
     * 添加部门
     */
    @PostMapping(value = "/departments")
    public Result add(@RequestBody Department department){
        department.setCompanyId(companyId);
        departmentService.save(department);
        return Result.SUCCESS();
    }
    /**
     * 修改部门信息
     */
    @PutMapping(value = "/departments/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Department department){
        department.setCompanyId(companyId);
        department.setId(id);
        departmentService.update(department);
        return Result.SUCCESS();
    }
    /**
     * 删除部门
     */
    @DeleteMapping(value = "/departments/{id}")
    public Result delete(@PathVariable(name = "id") String id){
        departmentService.delete(id);
        return Result.SUCCESS();
    }
    /**
     * 根据id查询
     */
    @GetMapping(value = "/departments/{id}")
    public Result findById(@PathVariable(name = "id") String id){
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }
    /**
     * 组织架构列表
     */
    @GetMapping(value = "/departments")
    public Result findAll() {
        Company company = companyService.findById(companyId);
        List<Department> list = departmentService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,new DeptListResult(company!=null?company:new Company(),list));
    }
}
