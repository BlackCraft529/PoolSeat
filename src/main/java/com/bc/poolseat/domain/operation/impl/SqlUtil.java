package com.bc.poolseat.domain.operation.impl;

import com.bc.poolseat.domain.config.SqlConfig;
import com.bc.poolseat.domain.operation.SqlUtilInterface;
import com.bc.poolseat.domain.operation.reflect.ReflectUtil;
import com.bc.poolseat.domain.pool.PoolContainer;
import com.bc.poolseat.initializer.impl.SqlInitializerX;
import com.mysql.fabric.xmlrpc.base.Array;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;


/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:27
 */
@Data
public class SqlUtil implements SqlUtilInterface {

    private PoolContainer poolContainer;

    public SqlUtil(SqlConfig sqlConfig){
        this.poolContainer = new SqlInitializerX().initPoolContainer(sqlConfig);
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
     * 更新数据
     *
     * @param cmd 指令
     * @return 影响条数
     */
    private int updateDataToMySql(String cmd , List<String> parameters){
        Connection connection = getConnection();
        if(connection == null){
            return 0;
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(cmd);
            for (int i =1 ;i<parameters.size()+1;i++){
                preparedStatement.setObject(i,parameters.get(i-1));
            }
            int influenceLine = preparedStatement.executeUpdate();
            close(preparedStatement,connection);
            return influenceLine;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            close(preparedStatement,connection);
        }
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
     * 通过列名获取 玩家
     *
     * @param cmd 指令
     * @param columnName 列名
     * @param parameters 参数集
     * @return 获取内容
     */
    private String selectPlayerNameOrUUID(String cmd, String columnName ,List<String> parameters){
        Connection connection = getConnection();
        if(connection == null){
            return "";
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String result = "";
        try {
            preparedStatement = connection.prepareStatement(cmd);
            for (int i = 1; i<parameters.size()+1;i++){
                preparedStatement.setObject(i,parameters.get(i-1));
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                result = resultSet.getString(columnName);
            }
            close(resultSet,preparedStatement,connection);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet,preparedStatement,connection);
        }
        return result;
    }

    /**
     * 从数据库查询玩家数据 - 名字
     *
     * @param cmd            指令
     * @param nameColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    @Override
    public Player selectPlayerByName(String cmd, String nameColumnName , String... parameters) {
        return Bukkit.getPlayerExact(selectPlayerNameOrUUID(cmd,nameColumnName,Arrays.asList(parameters)));
    }

    /**
     * 从数据库查询玩家数据 - uuid
     *
     * @param cmd            指令
     * @param uuidColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    @Override
    public Object selectPlayerByUuid(String cmd, String uuidColumnName , String... parameters) {
        if("".equalsIgnoreCase(selectPlayerNameOrUUID(cmd,uuidColumnName,Arrays.asList(parameters)))){
            UUID playerUUID = UUID.fromString(selectPlayerNameOrUUID(cmd,uuidColumnName,Arrays.asList(parameters)));
            if(Bukkit.getPlayer(playerUUID) != null){
                return Bukkit.getPlayer(playerUUID);
            }else if (Bukkit.getOfflinePlayer(playerUUID) != null){
                return Bukkit.getOfflinePlayer(playerUUID);
            }
        }
        return null;
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
     * @param parameters 参数列表
     */
    @Override
    public int updateData(String cmd, String... parameters) {
        List<String> parameterList = Arrays.asList(parameters);
        return updateDataToMySql(cmd , parameterList);
    }

    /**
     * 更新数据库信息
     *
     * @param cmd        指令
     * @param parameters 参数列表
     */
    @Override
    public int updateData(String cmd, List<String> parameters) {
        return updateDataToMySql(cmd , parameters);
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
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameters = file.getStringList(cmdName+".parameters");
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className);
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
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameterList = Arrays.asList(parameters);
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameterList),className);
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
        String cmd = file.getString(cmdName+".cmd");
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className);
    }

    /**
     * 通过文件更新数据
     *
     * @param file    文件
     * @param cmdName 指令名
     */
    @Override
    public int updateData(FileConfiguration file, String cmdName) {
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameters = file.getStringList(cmdName+".parameters");
        return updateDataToMySql(cmd , parameters);
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
    public int updateData(FileConfiguration file, String cmdName, String... parameters) {
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameterList = Arrays.asList(parameters);
        return updateDataToMySql(cmd , parameterList);
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
    public int updateData(FileConfiguration file, String cmdName, List<String> parameters) {
        String cmd = file.getString(cmdName+".cmd");
        return updateDataToMySql(cmd , parameters);
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
