package com.stu.config;

import com.stu.domain.Permission;
import com.stu.domain.Role;
import com.stu.domain.User;
import com.stu.service.RoleService;
import com.stu.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userServicel;
    @Resource
    private RoleService roleService;
    //角色权限和对应权限添加
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection collection) {
        System.out.println(".................................授权中" +
                ".................................");
        //获取用户登陆信息
        String name = (String) collection.getPrimaryPrincipal();
        //查询用户名称
        User user = userServicel.findByName(name);
        //添加角色和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        System.out.println(user.toString());
        for(Role role : user.getRoles()){
            //添加角色
            info.addRole(role.getRoleName());
            for(Permission permission : role.getPermissions()){
                //添加权限
                info.addStringPermission(permission.getPermission());
            }
        }
        return info;
    }

    //用户认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println(".................................身份认证中" +
                ".................................");
        if (token.getPrincipal() == null) {
            throw new AccountException("用户名不正确");
        }

        //获取用户信息
        String name = token.getPrincipal().toString();
        User user = userServicel.findByName(name);
        if (user == null){
            throw new AuthenticationException("用户名错误");
        }else{
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo
                    (name,user.getPassword().toString(),getName());
            return info;
        }
    }
}