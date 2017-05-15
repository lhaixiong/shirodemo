package com.lhx.shirodemo.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * service层这里不能有@Service注解？
 * 也不能用@Transation注解？
 * 只能在xml中配置bean？
 * 不然为生命@RequiresRoles({"admin"})不生效
 */

//@Service
public class ShiroService {
    @RequiresRoles({"admin"})
    public void testMethod(){
        System.out.println(">>>>>>ShiroService  times>>>>>"+new Date());
        Session session = SecurityUtils.getSubject().getSession();
        //这里得到的session是org.apache.shiro.subject.support.DelegatingSubject$StoppingAwareProxiedSession@2143aff9
        System.out.println(">>>>>>>session"+session);
        System.out.println(">>>>>>>session.getAttribute(\"key\"):"+session.getAttribute("key"));
    }
}
