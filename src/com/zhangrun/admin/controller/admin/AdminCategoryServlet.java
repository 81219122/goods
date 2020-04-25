package com.zhangrun.admin.controller.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.Category;
import com.zhangrun.service.impl.BookServiceImpl;
import com.zhangrun.service.impl.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/29 15:40
 * 管理员分类管理功能servlet
 */
@WebServlet("/adminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();
    private BookServiceImpl bookService=new BookServiceImpl();
    /*
     * @Param [request, response]
     * @return void
     * 查询所有分类
    */
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> all = categoryService.findAll();  //调用service 查询所有分类
        request.setAttribute("parents",all);  //保存分类
        request.getRequestDispatcher("/adminjsps/admin/category/list.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 添加一级分类
    */
    public void addParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        Category category = CommonUtils.toBean(map, Category.class);
        category.setCid(CommonUtils.uuid());
        categoryService.add(category);
        findAll(request,response);//完成添加后调用findall方法显示所有分类
    }

    /*
     * @Param
     * @return
     * 添加二级分类准备
    */
    public void addChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid=request.getParameter("pid");  //从页面获取当前一级分类的id
        List<Category> parent = categoryService.findParent();  //查询所有的一级分类 不用带二级分类
        request.setAttribute("pid",pid);
        request.setAttribute("parents",parent);
        request.getRequestDispatcher("/adminjsps/admin/category/add2.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 添加二级分类
    */
    public void addChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        Category child = CommonUtils.toBean(map, Category.class);
        child.setCid(CommonUtils.uuid());
        String pid=request.getParameter("pid");  //获取当前页面父分类的id 把它封装到child中
        //把父分类的id封装到子分类中 添加需要判断
        Category parent=new Category();
        parent.setCid(pid);
        child.setParent(parent);
        categoryService.add(child);
        findAll(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 修改一级分类做准备
    */
    public void updateParentPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
        Category category = categoryService.load(cid);
        request.setAttribute("category",category);
        request.getRequestDispatcher("/adminjsps/admin/category/edit.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 修改一级分类
    */
    public void updateParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        Category category = CommonUtils.toBean(map, Category.class);
        categoryService.update(category);
        findAll(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 修改二级分类准备
    */
    public void updateChildPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");  //获取二级分类的cid
        Category child = categoryService.load(cid);  //二级分类对象
        List<Category> parent = categoryService.findParent();  //加载所有一级分类
        request.setAttribute("child",child); //保存二级分类对象
        request.setAttribute("parents",parent);  //保存所有一级分类
        request.getRequestDispatcher("/adminjsps/admin/category/edit2.jsp").forward(request,response);

    }

    /*
     * @Param [request, response]
     * @return void
     * 修改二级分类
    */
    public void updateChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        String pid = request.getParameter("pid");  //获取一级分类的cid
        Category child = CommonUtils.toBean(map, Category.class);
        //把一级分类的pid封装到二级分类child对象中
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);
        categoryService.update(child);  //调用方法修改
        findAll(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 删除一级分类
     * 前题：该一级分类下没有二级分类
    */
    public void deleteParent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        int childCount = categoryService.findChildCount(cid);
        if (childCount==0){//该一级分类下没有二级分类
            categoryService.deleteParent(cid);
            findAll(request,response);
        }else {//该一级分类下有二级分类
            request.setAttribute("msg","该分类下还有子分类！");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }
    }

    /*
     * @Param [request, response]
     * @return void
     * 删除二级分类
     * 前题：该二级分类下没有图书
    */
    public void deleteChild(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        int byBookCount = bookService.findByBookCount(cid);
        if (byBookCount==0){
            categoryService.deleteParent(cid);
            findAll(request,response);
        }else {
            request.setAttribute("msg","该分类下还有图书！");
            request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
        }

    }
}
