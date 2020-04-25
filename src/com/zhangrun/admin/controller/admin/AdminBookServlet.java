package com.zhangrun.admin.controller.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.Category;
import com.zhangrun.entity.PageBean;
import com.zhangrun.service.impl.BookServiceImpl;
import com.zhangrun.service.impl.CategoryService;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/3/2 15:16
 */
@WebServlet("/adminBookServlet")
public class AdminBookServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();
    private BookServiceImpl bookService=new BookServiceImpl();

    /*
     * @Param [request, response]
     * @return void
     * 查询出所有分类
    */
    public void findCategoryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> parents = categoryService.findAll();
        request.setAttribute("parents",parents);
        request.getRequestDispatcher("/adminjsps/admin/book/left.jsp").forward(request,response);

    }
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
        request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request,response);
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
        request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request,response);
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
        request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request,response);
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
        request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request,response);
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
        request.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 根据bid查询book对象
     */
    /*public void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid=request.getParameter("bid");
        Book book = bookService.load(bid);
        request.setAttribute("book",book);
        request.getRequestDispatcher("/adminjsps/admin/book/desc.jsp").forward(request,response);
    }*/

    /*
     * @Param [request, response]
     * @return void
     * 添加图书功能之加载一级分类到下拉列表
    */
    public void addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> parents= categoryService.findParent();
        request.setAttribute("parents",parents);
        request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request,response);
    }

    public void ajaxFindChildren(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pid=request.getParameter("pid");
        List<Category> children = categoryService.findByParent(pid);//查询一级分类下的二级分类
        String s = toJsonList(children);
        System.out.println(s);
        response.getWriter().print(s);

    }
    // {"cid":"asfa",}
    private String toJson(Category category){
        StringBuilder sb=new StringBuilder("{");
        sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
        sb.append(",");
        sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
        sb.append("}");
        return sb.toString();
    }
    private String toJsonList(List<Category> categories){
        StringBuilder sb=new StringBuilder("[");
        for (int i=0;i<categories.size();i++){
            sb.append(toJson(categories.get(i)));
            if (i<categories.size()-1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /*
     * @Param [request, response]
     * @return void
     * 加载图书
    */
    public void load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid = request.getParameter("bid");
        Book book = bookService.load(bid);
        request.setAttribute("book",book);
        List<Category> parents = categoryService.findParent();  //加载所有一级分类
        request.setAttribute("parents",parents);
        //获取当前图书所属一级分类的所有二级分类
        String pid = book.getCategory().getParent().getCid();
        List<Category> children = categoryService.findByParent(pid);
        request.setAttribute("children",children);
        request.getRequestDispatcher("/adminjsps/admin/book/desc.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 修改图书功能
    */
    public void exitBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> map = request.getParameterMap();
        Book book = CommonUtils.toBean(map, Book.class);
        Category category = CommonUtils.toBean(map, Category.class);
        book.setCategory(category);
        bookService.exit(book);
        request.setAttribute("msg","修改图书成功");
        request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 图书删除
    */
    public void deleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid=request.getParameter("bid");
        Book book = bookService.load(bid);
        String savepath = this.getServletContext().getRealPath("/");  //获取真实路径
        new File(savepath,book.getImage_w());  //删除图片
        new File(savepath,book.getImage_b());  //删除图片
        bookService.delete(bid);
        request.setAttribute("msg","删除图书成功");
        request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,response);
    }
}
