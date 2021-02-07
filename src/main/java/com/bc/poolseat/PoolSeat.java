package com.bc.poolseat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:12
 */
public class PoolSeat extends JavaPlugin {

    private static PoolSeat plugin;
    private static CommandSender logger;
    private static final String version = "1.0-SNAPSHOT", pluginTitle = "§a[§ePoolSeat§a]";

    public static PoolSeat getInstance(){return plugin;}
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

            }
        }
        return true;
    }
}
