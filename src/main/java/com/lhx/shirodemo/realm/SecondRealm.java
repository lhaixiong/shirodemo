package com.lhx.shirodemo.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecondRealm extends AuthenticatingRealm{
    private static final Logger log= LoggerFactory.getLogger(SecondRealm.class);
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken
               token) throws AuthenticationException {
        log.info("[SecondRealm] >>>>doGetAuthenticationInfo authenticationToken:{}",token.toString());
        //1. 把 AuthenticationToken 转换为 UsernamePasswordToken
        UsernamePasswordToken upt= (UsernamePasswordToken) token;
        //2. 从 UsernamePasswordToken 中来获取 username
        String username=upt.getUsername();
        //3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
        log.info(">>>>3从数据库中取出用户:{}",username);
        //4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
        if("unknown".equals(username)){
            log.error(">>>>账号不存在:{}", username);
            throw new UnknownAccountException(">>>账号不存在");
        }
        //5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
        if("monster".equals(username)){
            log.error(">>>>账号被锁定:{}", username);
            throw new LockedAccountException(">>>you are a monster,you are locked!!");
        }
        //6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
        //以下信息是从数据库中获取的.
        //1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
        Object principal = username;
        //2). credentials: 密码.
        Object credentials = "";//"123456";//fc1709d0a95a6be30bc5926fdb7f22f4
        if("admin".equals(username)){
            credentials="ce2f6417c7e1d32c1d81a797ee0b499f87c5de06";
        }
        if("user".equals(username)){
            credentials="073d4c3ae812935f23cb3f2a71943f49e082a718";
        }
        //3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
        String realmName = getName();
        //4). 盐值.
        ByteSource credentialsSalt = ByteSource.Util.bytes(username);
        SimpleAuthenticationInfo info=null;//new SimpleAuthenticationInfo(principal,credentials,realmName);
        info=new SimpleAuthenticationInfo("SecondRealm",credentials,credentialsSalt,realmName);
        return info;
    }

    public static void main(String[] args) {
        String hashAlgorithmName="SHA1";
        Object credentials="123456";
        ByteSource salt = ByteSource.Util.bytes("admin");
        //ByteSource salt = ByteSource.Util.bytes("user");
        int hashIterations = 1024;
        SimpleHash simpleHash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        System.out.println(simpleHash);//fc1709d0a95a6be30bc5926fdb7f22f4
    }
}
