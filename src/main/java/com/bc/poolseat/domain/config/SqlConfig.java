package com.bc.poolseat.domain.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SqlConfig extends HikariConfig implements Cloneable {

    /**
     * 初始化数据库连接配置
     *
     * @param databaseName 数据库名称
     * @param password 密码
     * @param userName 用户名
     * @param port 端口
     * @param ip ip
     * @param poolSize 连接池大小
     * @param connectParameter useSSL=false&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull
     */
    public SqlConfig(String databaseName, String userName, String password, String ip, String port, int poolSize, String connectParameter){
        if(!"".equalsIgnoreCase(connectParameter)){
            databaseName += "?" +connectParameter;
        }
        this.setJdbcUrl("jdbc:mysql://" + ip + ":"
                + port + "/" + databaseName);
        this.setUsername(userName);
        this.setPassword(password);
        this.setMaximumPoolSize(poolSize);
        this.setIdleTimeout(60000);
        this.setConnectionTimeout(600000);
        this.setValidationTimeout(3000);
        this.setMaxLifetime(60000);
        this.setAutoCommit(true);
    }

    @Override
    public String toString(){
        return "用户名: "+ this.getUsername() +" ;\n"+
                "密码: "+ this.getPassword() +" ;\n"+
                "连接池大小: "+ this.getMaximumPoolSize() +" ;\n"+
                "URL: "+ this.getJdbcUrl()+" ;";
    }

    /**
     * 克隆配置
     *
     * @return 配置
     */
    @Override
    public SqlConfig clone() {
        SqlConfig sqlConfig = null;
        try{
            sqlConfig = (SqlConfig)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sqlConfig;
    }



    public SqlConfig(){}
}
