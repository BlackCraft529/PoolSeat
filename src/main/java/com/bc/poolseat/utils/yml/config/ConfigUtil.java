package com.bc.poolseat.utils.yml.config;

import com.bc.poolseat.PoolSeat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luckily_Baby
 * @date 2021/2/8 1:45
 */
public class ConfigUtil {
    private static FileConfiguration cfg;
    public static List<String> ruleFiles = new ArrayList<>();
    /**
     * 加载配置文件
     */
    public static void loadData(boolean isReload){
        if(!new File(PoolSeat.getPlugin().getDataFolder(), "config.yml").exists()) {
            PoolSeat.logMessage("§4未找到[C]config.yml，正在创建...");
            PoolSeat.getPlugin().saveDefaultConfig();
            cfg= PoolSeat.getPlugin().getConfig();
            PoolSeat.logMessage("§b文件[C]config.yml创建完成!");
        }else {
            cfg =  YamlConfiguration.loadConfiguration(new File(PoolSeat.getPlugin().getDataFolder(), "config.yml"));
            PoolSeat.logMessage("§a文件[C]config.yml已找到!");
        }
    }
}
