package com.lhx.shirodemo.factory;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {
    public LinkedHashMap<String,String> buildFilterChainDefinitionMap(){
        /*
        <property name="filterChainDefinitions">
            <value>
                /login.jsp = anon
                /shiro/login = anon
                /shiro/logout = logout

                /user.jsp = roles[user]
                /admin.jsp = roles[admin]

                # everything else requires authentication:
                /** = authc
            </value>
        </property>
         */

        LinkedHashMap<String,String> map=new LinkedHashMap<>();
        //这里去读数据库的配置
        map.put("/login.jsp","anon");
        map.put("/shiro/login","anon");
        map.put("/shiro/logout","logout");
        map.put("/user.jsp","roles[user]");
        map.put("/admin.jsp","roles[admin]");
        map.put("/**","authc");
        return map;
    }
}
