package com.bc.poolseat.domain.operation.reflect;

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
    public static List<Object> getObjectByResultList(List<Map<String , Object>> resultList , String className){
        if(resultList == null ){
            return null;
        }
        try {
            List<Object> objectList = new ArrayList<>();
            Class objectClass = Class.forName(className);
            for (Map<String , Object> map: resultList) {
                Object object = objectClass.newInstance();
                for (String key : map.keySet()){
                    try {
                        objectClass.getDeclaredField(key);
                    }catch (NoSuchFieldException e){
                        continue;
                    }
                    Field field = objectClass.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(object , map.get(key));
                }
                objectList.add(object);
            }
            return objectList;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
