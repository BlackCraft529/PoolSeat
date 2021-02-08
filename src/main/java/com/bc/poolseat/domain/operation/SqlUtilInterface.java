package com.bc.poolseat.domain.operation;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2021/2/7 14:40
 */
public interface SqlUtilInterface {

    /**
     * 使用实体类进行数据更新
     *
     * @param pluginName 插件名称
     * @param javaBean 实体类
     * @param primaryKey 主键名称
     * @return 影响条数
     */
    int updateDataFromBean(String pluginName , Object javaBean , String primaryKey);

    /**
     * 从数据库查询玩家数据 - 名字
     *
     * @param cmd            指令
     * @param nameColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    Player selectPlayerByName(String cmd, String nameColumnName , List<String> parameters);

    /**
     * 从数据库查询玩家数据 - uuid
     *
     * @param cmd            指令
     * @param uuidColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    Object selectPlayerByUuid(String cmd, String uuidColumnName , List<String> parameters);

    /**
     * 从文件获取玩家
     *
     * @param file 文件
     * @param cmdName 指令集
     * @return 玩家 Player | OfflinePlayer
     */
    Object selectPlayerFromYml(FileConfiguration file , String cmdName);

    /**
     * 从数据库查询玩家数据 - 名字
     *
     * @param cmd 指令
     * @param nameColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    Object selectPlayerByName(String cmd , String nameColumnName , String... parameters);

    /**
     * 从数据库查询玩家数据 - uuid
     *
     * @param cmd 指令
     * @param uuidColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     */
    Object selectPlayerByUuid(String cmd , String uuidColumnName , String... parameters);

    /**
     * 查询数据并包装成类
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @param className 类型名
     * @return 实体类
     */
    List<Object> selectData(String cmd , String className , String... parameters );

    /**
     * 查询数据并包装成类
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @param className 类型名
     * @return 实体类
     */
    List<Object> selectData(String cmd , String className, List<String> parameters);

    /**
     * 更新数据库信息
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @return 影响条数
     */
    int updateData(String cmd , String... parameters);

    /**
     * 更新数据库信息
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @return 影响条数
     */
    int updateData(String cmd , List<String> parameters);

    /**
     * 通过文件执行指令
     *
     * @param file 文件
     * @param cmdName 指令名
     * @return 实体类
     */
    List<Object> selectData(FileConfiguration file , String cmdName);

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    List<Object> selectData(FileConfiguration file , String cmdName , String... parameters);

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 实体类
     */
    List<Object> selectData(FileConfiguration file , String cmdName , List<String> parameters);

    /**
     * 通过文件更新数据
     *
     * @param file 文件
     * @param cmdName 指令名
     * @return 影响条数
     */
    int updateData(FileConfiguration file , String cmdName);

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 影响条数
     */
    int updateData(FileConfiguration file , String cmdName , String... parameters);

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 影响条数
     */
    int updateData(FileConfiguration file , String cmdName , List<String> parameters);

    /**
     * 释放资源
     *
     * @param resultSet 结果集
     * @param preparedStatement 参数集
     * @param connection 连接
     */
    void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection);

    /**
     * 释放资源
     *
     * @param preparedStatement 参数集
     * @param connection 连接
     */
    void close(PreparedStatement preparedStatement, Connection connection);

    /**
     * 释放资源
     *
     * @param connection 连接
     */
    void close(Connection connection);

}
