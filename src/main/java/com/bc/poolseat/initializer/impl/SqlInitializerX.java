package com.bc.poolseat.initializer.impl;

import com.bc.poolseat.domain.config.SqlConfig;
import com.bc.poolseat.domain.pool.PoolContainer;
import com.bc.poolseat.initializer.SqlInitializer;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:26
 */
public class SqlInitializerX implements SqlInitializer {

    /**
     * 初始化连接池信息 并 返回连接池
     *
     * @param sqlConfig 数据库配置
     * @return null | 连接池
     */
    @Override
    public PoolContainer initPoolContainer(SqlConfig sqlConfig) {
        return new PoolContainer(sqlConfig);
    }
    

}
