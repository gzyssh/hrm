package com.hrm.company.controller;


import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.company.service.CompanyService;
import com.hrm.entity.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author guozy
 * @create 2019/08/07
 */
@CrossOrigin
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 添加企业
     */
    @PostMapping
    public Result add(@RequestBody Company company){
        companyService.add(company);
        return Result.SUCCESS();
    }
    /**
     * 根据id更新企业信息
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Company company){
        Company one = companyService.findById(id);
        one.setName(company.getName());
        one.setRemarks(company.getRemarks());
        one.setState(company.getState());
        one.setAuditState(company.getAuditState());
        companyService.update(company);
        return Result.SUCCESS();
    }
    /**
     * 根据id删除企业信息
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(name = "id") String id){
        companyService.deleteById(id);
        return Result.SUCCESS();
    }
    /**
     * 根据ID获取公司信息
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable(name = "id") String id){
        Company company = companyService.findById(id);
        return new Result(ResultCode.SUCCESS,company);
    }
    /**
     * 获取企业列表
     */
    @GetMapping()
    public Result findAll(){
        List<Company> companyList = companyService.findAll();
        return new Result(ResultCode.SUCCESS,companyList);
    }

}
