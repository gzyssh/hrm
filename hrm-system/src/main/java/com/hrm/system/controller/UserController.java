package com.hrm.system.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.entity.system.User;
import com.hrm.entity.system.response.UserResult;
import com.hrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  用户前端控制器
 * </p>
 *
 * @author guozy
 * @create 2019/08/08
 */
@CrossOrigin
@RestController
@RequestMapping("sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户
     */
    @PostMapping(value = "/user")
    public Result add(@RequestBody User user){
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.save(user);
        return Result.SUCCESS();
    }
    /**
     * 修改用户信息
     */
    @PutMapping(value = "/user/{id}")
    public Result update(@PathVariable(name = "id") String id, @RequestBody User user){
        user.setCompanyId(companyId);
        user.setId(id);
        userService.update(user);
        return Result.SUCCESS();
    }
    /**
     * 删除用户
     */
    @DeleteMapping(value = "/user/{id}")
    public Result delete(@PathVariable(name = "id") String id){
        userService.delete(id);
        return Result.SUCCESS();
    }
    /**
     * 根据id查询
     */
    @GetMapping(value = "/user/{id}")
    public Result findById(@PathVariable(name = "id") String id){
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS,user);
    }
    /**
     * 查询全部用户
     * 指定企业id
     */
    @GetMapping(value = "/user")
    public Result findAll(int page, int size, @RequestParam Map map) {
        //1.获取当前的企业id
        map.put("companyId",companyId);
        //2.完成查询
        Page<User> pageUser = userService.findAll(map,page,size);
        //3.构造返回结果
        PageResult pageResult = new PageResult(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }
}
