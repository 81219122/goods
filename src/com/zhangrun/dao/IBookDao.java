package com.zhangrun.dao;

import com.sun.mail.imap.protocol.BODY;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.Category;
import com.zhangrun.entity.Expression;
import com.zhangrun.entity.PageBean;
import sun.security.krb5.internal.PAData;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 13:34
 */
public interface IBookDao {
    //通用的查询方法  List<Expression>  这是条件
    PageBean findByCriteria(List<Expression> expressionList,int pc) throws SQLException;

    //按分类查询
    PageBean<Book> findByCategory(String cid,int pc) throws SQLException;

    //按书名模糊查询
    PageBean<Book> findByBname(String bname,int pc) throws SQLException;

    //按作者名字查询
    PageBean<Book> findByAuthor(String anthor,int pc) throws SQLException;

    //按出版社查询
    PageBean<Book> findByPress(String press,int pc) throws SQLException;

    //多条件组合查询
    PageBean<Book> findByCombination(Book criteria ,int pc) throws SQLException;

    //根据bid查询book
    Book findByBid(String bid) throws SQLException;

    //根据指定二级分类id查询book数量
    int findByBookCount(String cid) throws SQLException;

    //添加图书
    void add(Book book) throws SQLException;

    //修改图书
    void exit(Book book) throws SQLException;

    //删除图书
    void delete(String bid) throws SQLException;
}
