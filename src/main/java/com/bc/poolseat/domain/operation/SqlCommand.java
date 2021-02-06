package com.bc.poolseat.domain.operation;

import lombok.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Luckily_Baby
 * @date 2021/2/6 16:27
 */
@Data
public class SqlCommand {
    private String command;
    private Map<Integer,Object> parameters = new HashMap<Integer, Object>();

    public SqlCommand(){}
    public SqlCommand(String command){
        this.command = command;
    }

    public void setInt(int index , int parameter){
        parameters.put(index,parameter);
    }

    public void setDouble(int index , double parameter){
        parameters.put(index , parameter);
    }

    public void setDate(int index , Date date){
        parameters.put(index , date);
    }

    public void setString(int index , String string){
        parameters.put(index , string);
    }
}
