package com.bc.poolseat.domain.result;

import lombok.Data;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luckily_Baby
 * @date 2021/2/8 16:50
 */
@Data
public class ReflectMap {
    private String beanName;
    private String beanPath;
    private String tableName;
    private Map<String,String> resultMap = Collections.synchronizedMap(new HashMap<String, String>());
    private Map<String,String> parameterMap = Collections.synchronizedMap(new HashMap<String, String>());

    public ReflectMap(String beanName , String beanPath , String tableName , Map<String,String> resultMap , Map<String,String> parameterMap){
        this.beanName = beanName;
        this.beanPath = beanPath;
        this.tableName = tableName;
        this.resultMap.putAll(resultMap);
        this.parameterMap.putAll(parameterMap);
    }
}
