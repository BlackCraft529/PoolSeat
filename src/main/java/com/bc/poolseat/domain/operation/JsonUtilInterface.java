package com.bc.poolseat.domain.operation;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Luckily_Baby
 * @date 2021/2/10 0:22
 */
public interface JsonUtilInterface {
    /**
     * json转object
     *
     * @param jsonStr json类
     * @param classPath 类路径
     * @return 实体
     * @throws ClassNotFoundException 类路径错误
     */
    Object jsonToObject(JSONObject jsonStr, String classPath) throws ClassNotFoundException;

    /**
     * 实体类转换为json
     *
     * @param obj 实体
     * @return json对象
     */
    JSONObject objectToJson(Object obj);

    /**
     * json转string
     *
     * @param jsonObject json对象
     * @return string
     */
    String jsonObjectToString(JSONObject jsonObject);

    /**
     * string转 json对象
     *
     * @param objectString string
     * @return json对象
     */
    JSONObject stringToJsonObject( String objectString);


}
