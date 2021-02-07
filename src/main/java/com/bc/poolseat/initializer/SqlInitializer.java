package com.bc.poolseat.initializer;

import com.bc.poolseat.domain.config.SqlConfig;
import com.bc.poolseat.domain.pool.PoolContainer;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:19
 */
public interface SqlInitializer {

    /**
     * 初始化连接池信息 并 返回连接池
     *
     * @param sqlConfig 数据库配置
     * @return null | 连接池
     */
    PoolContainer initPoolContainer(SqlConfig sqlConfig);

}
