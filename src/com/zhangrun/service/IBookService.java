package com.zhangrun.service;

import com.zhangrun.entity.Book;
import com.zhangrun.entity.Category;
import com.zhangrun.entity.PageBean;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 13:34
 */
public interface IBookService {
    //按分类查询
    PageBean<Book> findByCategory(String cid, int pc);

    //按书名模糊查询
    PageBean<Book> findByBname(String bname, int pc);

    //按作者名字查询
    PageBean<Book> findByAuthor(String anthor,int pc);

    //按出版社查询
    PageBean<Book> findByPress(String press,int pc);

    //多条件组合查询
    PageBean<Book> findByCombination(Book criteria ,int pc);

    //根据bid查询单本图书详细信息
    Book load(String bid);

    //根据指定二级分类id查询book数量
    int findByBookCount(String cid);

    //添加图书
    void add(Book book);

    //修改图书
    void exit(Book book);

    //删除图书
    void delete(String bid);
}
