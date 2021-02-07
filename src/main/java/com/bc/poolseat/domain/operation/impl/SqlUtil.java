package com.bc.poolseat.domain.operation.impl;

import com.bc.poolseat.domain.operation.SqlUtilInterface;
import com.bc.poolseat.domain.operation.reflect.ReflectUtil;
import com.bc.poolseat.domain.pool.PoolContainer;
import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;
import java.sql.*;
import java.util.*;


/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:27
 */
@Data
public class SqlUtil implements SqlUtilInterface {

    private PoolContainer poolContainer;

    public SqlUtil(PoolContainer poolContainer){
        this.poolContainer = poolContainer;
    }

    /**
     * 获取连接
     *
     * @return 连接
     */
    private Connection getConnection(){
        try {
            if(poolContainer.getConnection() != null){
                return poolContainer.getConnection();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取结果集
     *
     * @param cmd 指令
     * @param parameters 参数集
     * @return 结果集
     */
    private List<Map<String , Object>> getResultList(String cmd, List<String> parameters){
        Connection connection = getConnection();
        if(connection == null){
            return null;
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSetMetaData metaData;
        List<Map<String , Object>> resultList = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(cmd);
            for (int i = 1; i<parameters.size(); i++){
                preparedStatement.setObject(i , parameters.get(i-1));
            }
            resultSet = preparedStatement.executeQuery();
            metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            while (resultSet.next()){
                Map<String , Object> map = new HashMap<>();
                for (int i = 0; i<count ;i++) {
                    String columnName = metaData.getColumnName(i+1);
                    map.put(columnName, resultSet.getObject(columnName));
                }
                resultList.add(map);
            }
            close(resultSet,preparedStatement,connection);
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(resultSet,preparedStatement,connection);
        }
    }



    /**
     * 查询数据并包装成类
     *
     * @param cmd        指令
     * @param className  类型名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object selectData(String cmd, String className, String... parameters) {
        List<String> parameterList = Arrays.asList(parameters);
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameterList),className);
    }

    /**
     * 查询数据并包装成类
     *
     * @param cmd        指令
     * @param className  类型名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object selectData(String cmd, String className, List<String> parameters) {
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className);
    }

    /**
     * 更新数据库信息
     *
     * @param cmd        指令
     * @param className  类型名
     * @param parameters 参数列表
     */
    @Override
    public void updateData(String cmd, String className, String... parameters) {

    }

    /**
     * 更新数据库信息
     *
     * @param cmd        指令
     * @param className  类型名
     * @param parameters 参数列表
     */
    @Override
    public void updateData(String cmd, String className, List<String> parameters) {

    }

    /**
     * 通过文件执行指令
     *
     * @param file    文件
     * @param cmdName 指令名
     * @return 实体类
     */
    @Override
    public Object selectData(FileConfiguration file, String cmdName) {
        return null;
    }

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file       文件
     * @param cmdName    指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object selectData(FileConfiguration file, String cmdName, String... parameters) {
        return null;
    }

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file       文件
     * @param cmdName    指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object selectData(FileConfiguration file, String cmdName, List<String> parameters) {
        return null;
    }

    /**
     * 通过文件更新数据
     *
     * @param file    文件
     * @param cmdName 指令名
     */
    @Override
    public void updateData(FileConfiguration file, String cmdName) {

    }

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file       文件
     * @param cmdName    指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object updateData(FileConfiguration file, String cmdName, String... parameters) {
        return null;
    }

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file       文件
     * @param cmdName    指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    @Override
    public Object updateData(FileConfiguration file, String cmdName, List<String> parameters) {
        return null;
    }

    /**
     * 释放资源
     *
     * @param resultSet         结果集
     * @param preparedStatement 参数集
     * @param connection        连接
     */
    @Override
    public void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            close(preparedStatement,connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 释放资源
     *
     * @param preparedStatement 参数集
     * @param connection        连接
     */
    @Override
    public void close(PreparedStatement preparedStatement, Connection connection) {
        try {
            if(preparedStatement!=null){
                preparedStatement.close();
            }
            close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     *
     * @param connection 连接
     */
    @Override
    public void close(Connection connection) {
        try {
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
