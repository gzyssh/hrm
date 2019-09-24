package com.hrm.system.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.JwtUtils;
import com.hrm.entity.system.Permission;
import com.hrm.entity.system.User;
import com.hrm.entity.system.response.ProfileResult;
import com.hrm.entity.system.response.UserResult;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 分配角色
     */
    @PutMapping(value = "/user/assignRoles")
    public Result assignRoles(@RequestBody Map<String,Object> map){
        //获取被分配的用户
        String userId = (String) map.get("id");
        //获取被分配的角色列表
        List<String> roleIds= (List<String>) map.get("roleIds");
        userService.assignRoles(userId,roleIds);
        return Result.SUCCESS();
    }

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
        return new Result(ResultCode.SUCCESS,userResult);
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

    /**
     * 用户登录
     * @param objectMap
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String,Object> objectMap){
        String mobile= (String) objectMap.get("mobile");
        String password= (String) objectMap.get("password");
        User user=userService.findByMobile(mobile);
        //登录失败
        if(user==null||!user.getPassword().equals(password)){
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }else{
            //登录成功
            Map<String,Object> map=new HashMap<>();
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);
        }
    }

    /**
     * 登录成功，获取用户信息
     * @return
     */
    @PostMapping(value = "/profile")
    public Result profile(HttpServletRequest request) throws Exception {
        String id = claims.getId();
        User user = userService.findById(id);
        //根据不同的用户级别获取用户权限
        ProfileResult profileResult=null;
        if("user".equals(user.getLevel())){
            profileResult=new ProfileResult(user);
        }else{
            Map<String,Object> map=new HashMap<>();
            if("coAdmin".equals(user.getLevel())){
                map.put("enVisible","1");
            }
            List<Permission> list = permissionService.findAll(map);
            profileResult=new ProfileResult(user,list);
        }
        return new Result(ResultCode.SUCCESS,profileResult);
    }
}
