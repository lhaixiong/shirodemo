package com.lhx.shirodemo.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class ShiroRealm extends AuthorizingRealm{
    private static final Logger log= LoggerFactory.getLogger(ShiroRealm.class);
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken
               token) throws AuthenticationException {
        log.info("[FirstRealm] >>>>doGetAuthenticationInfo authenticationToken:{}",token.toString());
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken upt= (UsernamePasswordToken) token;
        //2. 从 UsernamePasswordToken 中来获取 username
        String username=upt.getUsername();
        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        log.info(">>>>3从数据库中取出用户:{}",username);
        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if("unknown".equals(username)){
            log.error(">>>>账号不存在:{}",username);
            throw new UnknownAccountException(">>>账号不存在");
        }
        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
        if("monster".equals(username)){
            log.error(">>>>账号被锁定:{}",username);
            throw new LockedAccountException(">>>you are a monster,you are locked!!");
        }
        //6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        //以下信息是从数据库中获取的.
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        Object principal = username;
        //2). credentials: 密码.
        Object credentials = "";//"123456";//fc1709d0a95a6be30bc5926fdb7f22f4
        if("admin".equals(username)){
            credentials="038bdaf98f2037b31f1e75b5b4c9b26e";
        }
        if("user".equals(username)){
            credentials="098d2c478e9c11555ce2823231e02ec1";
        }
        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        String realmName = getName();
        //4). 盐值.
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        SimpleAuthenticationInfo info=null;//new SimpleAuthenticationInfo(principal,credentials,realmName);
        info=new SimpleAuthenticationInfo(principal,credentials,credentialsSalt,realmName);
        return info;
    }

    public static void main(String[] args) {
        String hashAlgorithmName="MD5";
        Object credentials="123456";
        //ByteSource salt = ByteSource.Util.bytes("admin");
        ByteSource salt = ByteSource.Util.bytes("user");
        int hashIterations = 1024;
        SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(simpleHash);//fc1709d0a95a6be30bc5926fdb7f22f4
    }
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("[FirstRealm] 认证>>>>doGetAuthorizationInfo principals:{}",principals.toString());
        //1. 从 PrincipalCollection 中来获取登录用户的信息
        String principal = (String) principals.getPrimaryPrincipal();
        //2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
        Set<String> roleSet=new HashSet<String>();
        roleSet.add("user");
        if("admin".equals(principal)){
            roleSet.add("admin");
        }
        //3. 创建 SimpleAuthorizationInfo, 并设置其 reles 属性.
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo(roleSet);
        //4. 返回 SimpleAuthorizationInfo 对象.
        return info;
    }
}
