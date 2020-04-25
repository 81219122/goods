package com.zhangrun.controller;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.Category;
import com.zhangrun.entity.PageBean;
import com.zhangrun.service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 13:35
 */
@WebServlet("/bookServlet")
public class BookServlet extends BaseServlet {
    private BookServiceImpl bookService=new BookServiceImpl();
    /*
     * @Param [request]
     * @return int
     * 获取当前页码
    */
    private int getPc(HttpServletRequest request){
        int pc=1;
        String param = request.getParameter("pc");
        if (param!=null && !param.trim().isEmpty()){
            try {
                pc=Integer.parseInt(param);
            }catch (RuntimeException e){}
        }
        return pc;
    }
    /*
     * @Param [request]
     * @return java.lang.String
     * 截取url  页面中的分页导航中需要使用它作为超链接的目标
    */
    private String getUrl(HttpServletRequest request){
        String url =null;
        String requestURI = request.getRequestURI(); //该方法得到  /goods/bookServlet
        String queryString = request.getQueryString();  //该方法得到method?方法名&参数
        url= requestURI + "?"+queryString;
        int index=url.lastIndexOf("&pc");  //要截取掉pc这个参数
        if (index!=-1){
            url=url.substring(0,index);
        }
        return url;
    }

    public void findByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.得到pc及当前页码，如果页码传递，就使用页码传递的，如果不传递默认为1
        int pc=getPc(request);
        //2.得到url
        String url=getUrl(request);
        //3.获取查询条件，本方法就是cid，及分类id
        String cid=request.getParameter("cid");
        //4.使用pc和cid调用service方法
        PageBean<Book> pb = bookService.findByCategory(cid, pc);
        //5.给pagebean设置url，保存pagebean，转发到jsps/book/list.jsp页面
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 按作者查
    */
    public void findByAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        int pc=getPc(request);
        String url = getUrl(request);
        String author = request.getParameter("author");
        PageBean<Book> pb = bookService.findByAuthor(author, pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * 按出版社查询
     * @return void
    */
    public void findByPress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        int pc=getPc(request);
        String url = getUrl(request);
        String press = request.getParameter("press");
        PageBean<Book> pb = bookService.findByPress(press, pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * 多条件组合查询
     * @return void
    */
    public void findByCombination(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        int pc=getPc(request);
        String url = getUrl(request);
        Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
        PageBean<Book> pb = bookService.findByCombination(book, pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * 按书名查询
     * @return void
    */
    public void findByBname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        int pc=getPc(request);
        String url = getUrl(request);
        String bname = request.getParameter("bname");
        PageBean<Book> pb = bookService.findByBname(bname, pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 根据bid查询book对象
    */
    public void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid=request.getParameter("bid");
        Book book = bookService.load(bid);
        request.setAttribute("book",book);
        request.getRequestDispatcher("/jsps/book/desc.jsp").forward(request,response);
    }
}
