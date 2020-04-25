package com.zhangrun.controller;

import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.Category;
import com.zhangrun.service.impl.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 12:56
 */
@WebServlet("/categoryServlet")
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();
    /*
     * @Param [request, response]
     * @return void
     * 查询所有分类
    */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> all = categoryService.findAll();
        request.setAttribute("parents",all);
        request.getRequestDispatcher("/jsps/left.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
