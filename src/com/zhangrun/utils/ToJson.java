package com.zhangrun.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 11:35
 */
public class ToJson {
    public static void toJson(Object object, HttpServletResponse response) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),object);
    }
}
