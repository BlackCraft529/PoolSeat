package com.bc.poolseat.domain.operation.reflect;

import com.bc.poolseat.PoolSeat;
import com.bc.poolseat.domain.result.ReflectMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Luckily_Baby
 * @date 2021/2/7 16:02
 */
public class ReflectUtil {
    /**
     * 获取实体类
     *
     * @param resultList 结果
     * @param className 类型名
     * @return 实体
     */
    public static List<Object> getObjectByResultList(List<Map<String , Object>> resultList , String className, Map<String, ReflectMap> reflectMap){
        if(resultList == null ){
            return null;
        }
        try {
            String beanName = className.split("\\.")[className.split("\\.").length-1];
            if(reflectMap.get(beanName) == null){
                System.out.println("错误1："+beanName);
                return null;
            }
            ReflectMap reflectMapGot = reflectMap.get(beanName);
            List<Object> objectList = new ArrayList<>();
            Class objectClass = Class.forName(className);
            for (Map<String , Object> map: resultList) {
                Object object = objectClass.newInstance();
                //遍历数据库字段名
                for (String key : map.keySet()){
                    //获取相应的java bean字段名
                    String beanFieldName = reflectMapGot.getResultMap().get(key);
                    if(beanFieldName == null){
                        System.out.println("错误2："+beanFieldName+" : "+key);
                        continue;
                    }
                    try {
                        objectClass.getDeclaredField(beanFieldName);
                    }catch (NoSuchFieldException e){
                        continue;
                    }
                    Field field = objectClass.getDeclaredField(beanFieldName);
                    field.setAccessible(true);
                    //设置值
                    field.set(object , map.get(key));
                }
                objectList.add(object);
            }
            return objectList;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            PoolSeat.logMessage("§4请确认类名正确,如 String 应为 java.lang.String!");
            e.printStackTrace();
        }
        return null;
    }
}
