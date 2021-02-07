package com.bc.poolseat;

import com.bc.poolseat.data.PluginPoolData;
import com.bc.poolseat.domain.config.SqlConfig;
import com.bc.poolseat.domain.operation.impl.SqlUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:12
 * @TODO
 *  使用javaBean更新相关表数据
 *  将所有map表映射为实体类的字段（类型）
 */
public class PoolSeat extends JavaPlugin {

    private static PoolSeat plugin;
    private static CommandSender logger;
    private static final String version = "1.0-SNAPSHOT", pluginTitle = "§a[§ePoolSeat§a]";

    public static PoolSeat getPlugin(){return plugin;}
    public static void logMessage(String msg){logger.sendMessage(pluginTitle+msg);}

    /**
     * 插件启动
     */
    @Override
    public void onEnable() {
        plugin = this;
        logger = Bukkit.getConsoleSender();
        logMessage("§aPoolSeat连接池前置开始初始化...");
        logMessage("§aVersion: "+version);
        logMessage("§aPoolSeat连接池初始化完毕!");
    }

    /**
     * 测试插件功能
     *
     * @param sender 玩家
     * @param cmd 指令
     * @param label label
     * @param args 参数
     * @return 是否成功
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("poolseat".equalsIgnoreCase(cmd.getName())){
            if(args.length == 1 && "test".equalsIgnoreCase(args[0])){
                //String databaseName, String userName, String password, String ip, String port, int poolSize, String connectParameter
                SqlConfig sqlConfig = new SqlConfig("testDB","root","1515206","localhost","3306",5,"useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
                SqlUtil sqlUtil = new SqlUtil(this,sqlConfig);
                String sqlCmd = "select * from test_data;";
                System.out.println(sqlUtil.selectData(sqlCmd,"com.bc.poolseat.domain.test.TestSqlBean"));
            } else if (args.length == 1 && "reload".equalsIgnoreCase(args[0])){
                for (String pluginName : PluginPoolData.getAllPluginPoolData().keySet()){
                    logger.sendMessage("\n§a插件:[§e"+pluginName+"§a]信息:\n§b"+PluginPoolData.getAllPluginPoolData().get(pluginName).toString());
                }
                logMessage("§a§l重载完成!");
            }
        }
        return true;
    }
}
