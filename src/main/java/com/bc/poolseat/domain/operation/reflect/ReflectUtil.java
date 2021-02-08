package com.bc.poolseat.domain.operation.reflect;

import com.bc.poolseat.PoolSeat;
import com.bc.poolseat.domain.result.ReflectMap;
import org.bukkit.configuration.file.FileConfiguration;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Luckily_Baby
 * @date 2021/2/7 16:02
 */
public class ReflectUtil {

    /**
     * 根据实体更新数据库信息
     *
     * @param cmdGroup 指令组
     * @param object 数据
     * @param file 文件
     * @param connection 连接
     * @param reflectMap 映射表
     * @return 处理语句
     */
    public static PreparedStatement getUpdatePrepareStatement(String cmdGroup, Object object, FileConfiguration file, Connection connection, Map<String, ReflectMap> reflectMap){
        try {
            String className = object.getClass().getName().split("\\.")[object.getClass().getName().split("\\.").length-1];
            String cmd = file.getString(cmdGroup+".cmd");
            Class objectClass = object.getClass();
            ReflectMap reflectMapGot = reflectMap.get(className);
            for(Field field:objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    String fieldName = field.getName();
                    String columnFieldName = reflectMapGot.getParameterMap().get(fieldName);
                    if(columnFieldName == null){
                        continue;
                    }
                    String value = field.get(object).toString();
                    if(value == null){
                        continue;
                    }
                    if(!isInteger(value)){
                        value="\'"+value+"\'";
                    }
                    if(cmd.contains("<"+columnFieldName+">")){
                        cmd = cmd.replaceAll("<"+columnFieldName+">" , value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return connection.prepareStatement(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    private static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
