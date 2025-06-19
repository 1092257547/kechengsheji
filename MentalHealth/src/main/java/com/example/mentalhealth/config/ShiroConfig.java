package com.example.mentalhealth.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * java配置类 需要交给spring托管的类必须加上@@Configuration
 */
@Configuration
public class ShiroConfig {


    //ShiroFilterFactoryBean
    /**
     * anon 表示匿名访问，不需要认证以及授权
     * authc表示需要认证 没有进行身份认证是不能进行访问的
     * roles[admin]表示角色认证，必须是拥有admin角色的用户才行
     * user表示用户不一定已通过认证,只要曾被Shiro记住过登录状态的用户就可以正常发起请求,比如rememberMe
     * perms表示指定过滤规则，这个一般是扩展使用，不会使用原生的
     * port表示请求的URL端口验证
     * ssl表示安全的URL请求,协议为https
     * rest表示根据请求的方法，如post、get、delete等
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier(value = "securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        Map<String,String> map = new LinkedHashMap<String, String>();
        //认证相关
        map.put("/index","authc");//需要登录认证才能访问
        map.put("/login","anon");
        map.put("/sendMessageByEmail","anon");
        map.put("/goods","authc");
        //授权相关
        map.put("/add","perms[user:add]");
        map.put("/delete","perms[user:delete]");
        map.put("/update","perms[user:update]");

        factoryBean.setFilterChainDefinitionMap(map);
        factoryBean.setLoginUrl("/userLogin");//登录页面
        factoryBean.setSuccessUrl("/index");

        factoryBean.setSecurityManager(securityManager);
        return factoryBean;
    }
    // DefaultWebSecurityManager
    @Bean(value = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier(value = "userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //获取自定义realm
    /**
     * @Bean 这个注解是作用于方法上面，其意义是将该方法标注成为一个组件，也就是一个Spring bean ,交给IOC托管
     * @return
     */
    @Bean(name = "userRealm")
    public UserRealm getUserRealm(){
        return new UserRealm();
    }

    /**
     *
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return  new ShiroDialect();
    }

}
