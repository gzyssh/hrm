package com.hrm.system.controller;

import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.entity.system.Role;
import com.hrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  角色前端控制器
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 添加角色
     */
    @PostMapping(value = "/role")
    public Result add(@RequestBody Role role){
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }
    /**
     * 修改角色信息
     */
    @PutMapping(value = "/role/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role){
        roleService.update(role);
        return Result.SUCCESS();
    }
    /**
     * 删除角色
     */
    @DeleteMapping(value = "/role/{id}")
    public Result delete(@PathVariable(name = "id") String id){
        roleService.delete(id);
        return Result.SUCCESS();
    }
    /**
     * 根据id查询
     */
    @GetMapping(value = "/role/{id}")
    public Result findById(@PathVariable(name = "id") String id){
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS,role);
    }
    /**
     * 分页查询角色
     * 指定企业id
     */
    @GetMapping(value = "/role")
    public Result findAll(Role role,int page, int size) {
        Page<Role> pageRole = roleService.findSearch(companyId,page,size);
        //3.构造返回结果
        PageResult pageResult = new PageResult(pageRole.getTotalElements(),pageRole.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }
}
