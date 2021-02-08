package com.bc.poolseat.domain.operation.impl;

import com.bc.poolseat.PoolSeat;
import com.bc.poolseat.data.PluginPoolData;
import com.bc.poolseat.domain.config.SqlConfig;
import com.bc.poolseat.domain.operation.SqlUtilInterface;
import com.bc.poolseat.domain.operation.reflect.ReflectUtil;
import com.bc.poolseat.domain.pool.PoolContainer;
import com.bc.poolseat.domain.result.ReflectMap;
import com.bc.poolseat.initializer.impl.SqlInitializerX;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.*;
import java.util.*;


/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:27
 */
@Data
public class SqlUtil implements SqlUtilInterface {
    /**
     * 类型名 : 映射表
     */
    private Map<String, ReflectMap> reflectMap = Collections.synchronizedMap(new HashMap<String, ReflectMap>());

    private PoolContainer poolContainer;

    /**
     * 根据配置初始化信息
     *
     * @param plugin 插件
     * @param sqlConfig 配置信息
     */
    public SqlUtil(JavaPlugin plugin , SqlConfig sqlConfig){
        this.poolContainer = new SqlInitializerX().initPoolContainer(sqlConfig);
        PluginPoolData.setPluginPoolData(plugin.getName(),sqlConfig.clone());
    }

    /**
     * 根据文件映射字段
     *
     * @param file 文件
     * @return 初始化信息值
     */
    public int initResultMap(FileConfiguration file){
        this.reflectMap.clear();
        for (String beanName : file.getKeys(false)){
            // (table字段 : bean字段)
            Map<String,String> resultMap = Collections.synchronizedMap(new HashMap<String, String>());
            for (String line : file.getStringList(beanName+".reflectMap")){
                String columnName = line.split("<->")[0].split(":")[1];
                String javaBeanName = line.split("<->")[1].split(":")[1];
                resultMap.put(columnName,javaBeanName);
            }
            String beanPath = file.getString(beanName+".path");
            String tableName = file.getString(beanName+".table");
            //String beanName , String beanPath , String tableName , Map<String,String> resultMap
            ReflectMap reflectMap = new ReflectMap(beanName, beanPath, tableName, resultMap);
            this.reflectMap.put(beanName , reflectMap);
        }
        return reflectMap.size();
    }

    /**
     * 根据文件初始化数据库
     *
     * @param file 文件
     * @param plugin 插件
     */
    public SqlUtil(JavaPlugin plugin , FileConfiguration file){
        String databaseName = file.getString("MySql.DatabaseName");
        String userName = file.getString("MySql.UserName");
        String password = file.getString("MySql.Password");
        String port = file.getString("MySql.Port");
        String ip = file.getString("MySql.Ip");
        int poolSize = file.getInt("MySql.PoolSize");
        String connectParameter = file.getString("MySql.ConnectParameter");
        SqlConfig sqlConfig = new SqlConfig(databaseName,userName,password,ip,port,poolSize,connectParameter);
        this.poolContainer = new SqlInitializerX().initPoolContainer(sqlConfig);
        PluginPoolData.setPluginPoolData(plugin.getName(),sqlConfig.clone());
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
     * 从文件获取玩家
     *
     * @param file    文件
     * @param cmdName 指令集
     * @return 玩家 Player | OfflinePlayer
     */
    @Override
    public Object selectPlayerFromYml(FileConfiguration file, String cmdName) {
        if(file.get(cmdName) == null || "Player".equalsIgnoreCase(file.getString(cmdName+".return"))){
            PoolSeat.logMessage("§4路径地址错误或返回类型错误,该方法仅适用Player类型!");
            return null;
        }
        String type = file.getString(cmdName+".type");
        String cmd = file.getString(cmdName+".cmd");
        String columnName = file.getString(cmdName+".column");
        List<String> parameterList = file.getStringList(cmdName+".parameters");
        if("uuid".equalsIgnoreCase(type)){
            return selectPlayerByUuid(cmd, columnName, parameterList);
        }else {
            return selectPlayerByName(cmd, columnName, parameterList);
        }
    }

    /**
     * 使用实体类进行数据更新
     *
     * @param pluginName 插件名称
     * @param javaBean   实体类
     * @param primaryKey 主键名称
     * @return 影响条数
     */
    @Override
    public int updateDataFromBean(String pluginName, Object javaBean, String primaryKey) {
        //do something
        return 0;
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
    public Player selectPlayerByName(String cmd, String nameColumnName , List<String> parameters) {
        return Bukkit.getPlayerExact(selectPlayerNameOrUUID(cmd,nameColumnName,parameters));
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
    public Object selectPlayerByUuid(String cmd, String uuidColumnName , List<String> parameters) {
        if("".equalsIgnoreCase(selectPlayerNameOrUUID(cmd,uuidColumnName,parameters))){
            UUID playerUUID = UUID.fromString(selectPlayerNameOrUUID(cmd,uuidColumnName,parameters));
            if(Bukkit.getPlayer(playerUUID) != null){
                return Bukkit.getPlayer(playerUUID);
            }else if (Bukkit.getOfflinePlayer(playerUUID) != null){
                return Bukkit.getOfflinePlayer(playerUUID);
            }
        }
        return null;
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
    public List<Object> selectData(String cmd, String className, String... parameters) {
        List<String> parameterList = Arrays.asList(parameters);
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameterList),className,this.reflectMap);
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
    public List<Object> selectData(String cmd, String className, List<String> parameters) {
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className,this.reflectMap);
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
    public List<Object> selectData(FileConfiguration file, String cmdName) {
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return null;
        }
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameters = file.getStringList(cmdName+".parameters");
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className,this.reflectMap);
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
    public List<Object> selectData(FileConfiguration file, String cmdName, String... parameters) {
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return null;
        }
        String cmd = file.getString(cmdName+".cmd");
        List<String> parameterList = Arrays.asList(parameters);
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameterList),className,this.reflectMap);
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
    public List<Object> selectData(FileConfiguration file, String cmdName, List<String> parameters) {
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return null;
        }
        String cmd = file.getString(cmdName+".cmd");
        String className = file.getString(cmdName+".return");
        return ReflectUtil.getObjectByResultList(getResultList(cmd,parameters),className,this.reflectMap);
    }

    /**
     * 通过文件更新数据
     *
     * @param file    文件
     * @param cmdName 指令名
     */
    @Override
    public int updateData(FileConfiguration file, String cmdName) {
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return 0;
        }
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
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return 0;
        }
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
        if(file.get(cmdName) == null){
            PoolSeat.logMessage("§4路径地址错误!");
            return 0;
        }
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
