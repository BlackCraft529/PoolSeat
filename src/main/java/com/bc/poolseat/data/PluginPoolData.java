package com.bc.poolseat.data;

import com.bc.poolseat.domain.config.SqlConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luckily_Baby
 * @date 2021/2/8 1:56
 */
public class PluginPoolData {
    private static Map<String, SqlConfig> allPluginPoolData = Collections.synchronizedMap(new HashMap<String, SqlConfig>());

    /**
     * 获取所有插件的连接信息
     *
     * @return 连接信息集合
     */
    public static Map<String, SqlConfig> getAllPluginPoolData(){
        return Collections.synchronizedMap(new HashMap<String, SqlConfig>(allPluginPoolData));
    }

    /**
     * 获取指定插件连接池数据信息
     *
     * @param pluginName 插件名称
     * @return 连接信息
     */
    public static synchronized SqlConfig getPluginData(String pluginName){
        if(allPluginPoolData.get(pluginName) != null){
            allPluginPoolData.get(pluginName).clone();
        }
        return null;
    }

    /**
     * 设置某插件的连接信息
     *
     * @param pluginName 插件名
     * @param sqlConfig 连接配置
     */
    public static synchronized void setPluginPoolData(String pluginName, SqlConfig sqlConfig){
        allPluginPoolData.put(pluginName,sqlConfig);
    }

}
