package com.hrm.system.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.entity.system.Permission;
import com.hrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  权限前端控制器
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 添加权限
     */
    @PostMapping(value = "/permission")
    public Result add(@RequestBody Map<String,Object> map) throws Exception {
        permissionService.save(map);
        return Result.SUCCESS();
    }
    /**
     * 修改权限信息
     */
    @PutMapping(value = "/permission/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody Map<String,Object> map) throws Exception {
        map.put("id",id);
        permissionService.update(map);
        return Result.SUCCESS();
    }
    /**
     * 删除权限
     */
    @DeleteMapping(value = "/permission/{id}")
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        permissionService.delete(id);
        return Result.SUCCESS();
    }
    /**
     * 根据id查询
     */
    @GetMapping(value = "/permission/{id}")
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Map<String,Object> map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,map);
    }
    /**
     * 查询全部权限
     */
    @GetMapping(value = "/permission")
    public Result findAll(@RequestParam Map map) {
        List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }
}
