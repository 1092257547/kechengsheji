package com.example.mentalhealth.config;

import com.example.mentalhealth.Service.PermissionService;
import com.example.mentalhealth.Service.RoleService;
import com.example.mentalhealth.Service.UserService;
import com.example.mentalhealth.entities.Permission;
import com.example.mentalhealth.entities.Role;
import com.example.mentalhealth.entities.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm 必须继承 AuthorizingRealm
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = principalCollection.getPrimaryPrincipal().toString();
        // 根据页面传过来的用户名查询用户数据
        User user = userService.queryUserByUsername(username);
        System.out.println(user);
        // 根据用户数据里面的rId查询角色表
        List<Role> role = roleService.queryRoleByIds(user.getRId());
        Set<String> roleSet = new HashSet<>();//存放角色信息的集合
        Set<String> permissionSet = new HashSet<>();// 存放权限信息的集合
        for (Role role1 : role) {
            roleSet.add(role1.getRCode());// 存放角色信息
            String[] split = role1.getPId().split(",");
            for (String s1 : split) {
                // 根据每一条角色信息里面的权限id查询权限表
                Permission permission = permissionService.queryPermissionById(Integer.parseInt(s1));
                permissionSet.add(permission.getPCode());// 存放权限信息
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roleSet);
        simpleAuthorizationInfo.addStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 执行认证 记忆方式是看参数中带的token
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = authenticationToken.getPrincipal().toString();
        //通过用户名查询用户信息
        User user = userService.queryUserByUsername(principal);
        //将
        if(!user.getUsername().equals(principal)){
            return null;
        }
        //返回
        return new SimpleAuthenticationInfo(principal,user.getPassword(),this.getName());
    }
}
