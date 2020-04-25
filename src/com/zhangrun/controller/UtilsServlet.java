package com.zhangrun.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/14 13:25
 */
@WebServlet("/UtilsServlet")
public class UtilsServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response){
        //完成方法分发
        //1.获取请求路径
        String uri = request.getRequestURI(); //     /user/add
        System.out.println("请求的路径"+uri);//     /user/add
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1); //获取到add方法名称
        System.out.println("方法名称"+methodName);
        //3.获取方法对象Method 用到反射
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            method.invoke(this,request,response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
