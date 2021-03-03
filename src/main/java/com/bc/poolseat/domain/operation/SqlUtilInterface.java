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
     * 执行yml文件命令组
     *
     * @param file 文件
     * @param cmdGroup cmd组
     * @return 影响条数
     */
    int executeCommandFromYml(FileConfiguration file , String cmdGroup);

    /**
     * 执行命令
     *
     * @param cmd 命令
     * @return 影响条数
     */
    int executeCommand(String cmd);

    /**
     * 使用实体类更新数据库json数据
     *
     * @param file 文件
     * @param cmdGroup 指令组
     * @param object 实体类
     * @return 影响条数
     * 测试完成
     */
    int updateJsonDataFromBean(FileConfiguration file , String cmdGroup, Object object);

    /**
     * 通过文件从数据库查询到json数据并转换为相应的实体类
     * 自带参数 - 参数对应的为语句中的参数，并非实体类中的参数
     *
     * @param file 文件
     * @param cmdGroup cmd指令组
     * @param parameters 参数
     * @return 对象
     * 测试完成
     */
    Object selectJsonDataToBean(FileConfiguration file , String cmdGroup, List<String> parameters);

    /**
     * 通过文件从数据库查询到json数据并转换为相应的实体类
     *
     * @param file 文件
     * @param cmdGroup cmd指令组
     * @return 对象
     * 测试完成
     */
    Object selectJsonDataToBean(FileConfiguration file , String cmdGroup);

    /**
     * 从数据库查询到json数据并转换为相应的实体类
     *
     * @param cmd 指令
     * @param columnName 列名
     * @param classPath 类路径
     * @param parameters 参数
     * @return 实体
     * @throws ClassNotFoundException 路径错误
     * 测试完成
     */
    Object selectJsonDataToBean(String cmd ,String columnName,  List<String> parameters, String classPath) throws ClassNotFoundException;

    /**
     * 从数据库查询到json数据并转换为相应的实体类
     *
     * @param cmd 指令
     * @param columnName 列名
     * @param classPath 类路径
     * @param parameters 参数
     * @return 对象
     * 测试完成
     */
    Object selectJsonDataToBean(String cmd ,String columnName, String classPath,  String... parameters);

    /**
     * 使用文件查询数据
     *
     * @param cmd 指令
     * @param columnName 字段名
     * @param parameters 参数
     * @return string数据
     * 测试完成
     */
    String selectStringData(String cmd ,String columnName,  String... parameters);

    /**
     * 使用文件查询数据
     *
     * @param cmd 指令
     * @param columnName 字段名
     * @param parameters 参数
     * @return string数据
     * 测试完成
     */
    String selectStringData(String cmd ,String columnName, List<String> parameters);

    /**
     * 使用文件查询数据
     *
     * @param file 文件
     * @param cmdGroup 指令组
     * @return string数据
     * 测试完成
     */
    String selectStringData(FileConfiguration file , String cmdGroup);

    /**
     * 使用实体类进行数据更新
     *
     * @param file 文件
     * @param javaBean 实体类
     * @param cmdGroup 指令组
     * @return 影响条数
     * 测试完成
     */
    int updateDataFromBean(FileConfiguration file , Object javaBean, String cmdGroup);

    /**
     * 从数据库查询玩家数据 - 名字
     *
     * @param cmd            指令
     * @param nameColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Player selectPlayerByName(String cmd, String nameColumnName , List<String> parameters);

    /**
     * 从数据库查询玩家数据 - uuid
     *
     * @param cmd            指令
     * @param uuidColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerByUuid(String cmd, String uuidColumnName , List<String> parameters);

    /**
     * 从文件获取玩家
     *
     * @param file 文件
     * @param cmdName 指令集
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerFromYml(FileConfiguration file , String cmdName);

    /**
     * 从文件获取玩家
     *
     * @param file 文件
     * @param cmdName 指令集
     * @param parameters 自带参数
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerFromYml(FileConfiguration file, String cmdName , List<String> parameters);

    /**
     * 从文件获取玩家
     *
     * @param file 文件
     * @param cmdName 指令集
     * @param parameters 自带参数
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerFromYml(FileConfiguration file, String cmdName , String... parameters);

    /**
     * 从数据库查询玩家数据 - 名字
     *
     * @param cmd 指令
     * @param nameColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerByName(String cmd , String nameColumnName , String... parameters);

    /**
     * 从数据库查询玩家数据 - uuid
     *
     * @param cmd 指令
     * @param uuidColumnName uuid字段
     * @param parameters 参数集
     * @return 玩家 Player | OfflinePlayer
     * 测试完成
     */
    Object selectPlayerByUuid(String cmd , String uuidColumnName , String... parameters);

    /**
     * 查询数据并包装成类
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @param className 类型名
     * @return 实体类
     * 测试完成
     */
    List<Object> selectData(String cmd , String className , String... parameters );

    /**
     * 查询数据并包装成类
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @param className 类型名
     * @return 实体类
     * 测试完成
     */
    List<Object> selectData(String cmd , String className, List<String> parameters);

    /**
     * 更新数据库信息
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @return 影响条数
     * 测试完成
     */
    int updateData(String cmd , String... parameters);

    /**
     * 更新数据库信息
     *
     * @param cmd 指令
     * @param parameters 参数列表
     * @return 影响条数
     * 测试完成
     */
    int updateData(String cmd , List<String> parameters);

    /**
     * 通过文件执行指令
     *
     * @param file 文件
     * @param cmdName 指令名
     * @return 实体类
     * 测试完成
     */
    List<Object> selectData(FileConfiguration file , String cmdName);

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 实体类
     * 测试完成
     */
    List<Object> selectData(FileConfiguration file , String cmdName , String... parameters);

    /**
     * 通过文件执行指令（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 实体类
     * 测试完成
     */
    List<Object> selectData(FileConfiguration file , String cmdName , List<String> parameters);

    /**
     * 通过文件更新数据
     *
     * @param file 文件
     * @param cmdName 指令名
     * @return 影响条数
     * 测试完成
     */
    int updateData(FileConfiguration file , String cmdName);

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 影响条数
     * 测试完成
     */
    int updateData(FileConfiguration file , String cmdName , String... parameters);

    /**
     * 通过文件更新数据（自带参数）
     *
     * @param file 文件
     * @param cmdName 指令名
     * @param parameters 参数列表
     * @return 影响条数
     * 测试完成
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

    /**
     * 关闭连接池
     */
    void dispose();

}
