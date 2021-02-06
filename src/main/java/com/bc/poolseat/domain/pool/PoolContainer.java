package com.bc.poolseat.domain.pool;

import com.bc.poolseat.domain.config.SqlConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:24
 */
public class PoolContainer extends HikariDataSource {

    public PoolContainer(SqlConfig sqlConfig){
        super(sqlConfig);
    }
}
