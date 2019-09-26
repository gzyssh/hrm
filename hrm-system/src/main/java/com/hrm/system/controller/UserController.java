package com.hrm.system.controller;


import com.hrm.common.controller.BaseController;
import com.hrm.common.entity.PageResult;
import com.hrm.common.entity.Result;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.utils.JwtUtils;
import com.hrm.entity.system.User;
import com.hrm.entity.system.UserSimpleResult;
import com.hrm.entity.system.response.ProfileResult;
import com.hrm.entity.system.response.UserResult;
import com.hrm.system.service.PermissionService;
import com.hrm.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    @RequiresPermissions("API-USER-DELETE")
    @DeleteMapping(value = "/user/{id}")
    public Result delete(@PathVariable(name = "id") String id){
        userService.delete(id);
        return Result.SUCCESS();
    }

    /**
     * 根据公司ID获取员工列表
     * @return
     * @throws Exception
     */
    @GetMapping("/user/simple")
    public Result simple() throws Exception {
        List<UserSimpleResult> list = new ArrayList<>();
        List<User> users = userService.findAll(companyId);
        for (User user : users) {
            list.add(new UserSimpleResult(user.getId(),user.getUsername()));
        }
        return new Result(ResultCode.SUCCESS,list);
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
        try {
            //构造登录令牌
            //加密密码
            password=new Md5Hash(password,mobile,3).toString();//密码，盐，加密次数
            UsernamePasswordToken upToken=new UsernamePasswordToken(mobile,password);
            //获取Subject
            Subject subject = SecurityUtils.getSubject();
            //调用login方法，进入realm完成认证
            subject.login(upToken);
            //获取sessionID
            String sessionId = (String) subject.getSession().getId();
            //构造返回结果
            return new Result(ResultCode.SUCCESS,sessionId);
        } catch (Exception e) {
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }
    }

    /**
     * 登录成功，获取用户信息
     * @return
     */
    @PostMapping(value = "/profile")
    public Result profile(HttpServletRequest request) throws Exception {
        //获取Session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        ProfileResult profileResult = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS,profileResult);
    }
}
