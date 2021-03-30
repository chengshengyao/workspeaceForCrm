package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @ProjectName: workspeaceForCrm
 * @Package: com.bjpowernode.crm.web.listener
 * @Description: java类作用描述
 * @Author: 生尧
 * @CreateDate: 2021/3/8 1:11
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {  //sce表示 该监听器所监听的 作用域对象
        System.out.println("此刻服务器启动时，全局作用域对象被创建,进行数据字典的处理 0 ");
        ServletContext application = sce.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = ds.getAll();
        Set<String> keySet = map.keySet();
        for (String key :
                keySet) {

            application.setAttribute(key,map.get(key));
        }
        System.out.println("此刻服务器启动时，全局作用域对象被创建,进行数据字典的处理 1 ");
        System.out.println("此刻服务器启动时，全局作用域对象被创建,进行状态可能性 0 ");
        HashMap<String, String> pMap = new HashMap<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();//阶段
            String value = resourceBundle.getString(key);//可能性
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
