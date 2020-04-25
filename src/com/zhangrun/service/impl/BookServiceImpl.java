package com.zhangrun.service.impl;

import com.zhangrun.dao.impl.BookDaoImpl;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.Category;
import com.zhangrun.entity.PageBean;
import com.zhangrun.service.IBookService;

import java.sql.SQLException;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/21 13:22
 */
public class BookServiceImpl implements IBookService {
    private BookDaoImpl bookDao=new BookDaoImpl();
    /*
     * @Param [cid, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Category>
     *     按分类查询
    */
    @Override
    public PageBean<Book> findByCategory(String cid, int pc) {
        try {
            return bookDao.findByCategory(cid,pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * @Param [bname, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按书名模糊查询
    */
    @Override
    public PageBean<Book> findByBname(String bname, int pc) {
        try {
            return bookDao.findByBname(bname,pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * @Param [anthor, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按作者模糊查询
    */
    @Override
    public PageBean<Book> findByAuthor(String anthor, int pc) {
        try {
            System.out.println(bookDao.findByAuthor(anthor,pc));
            return bookDao.findByAuthor(anthor,pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * @Param [press, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按出版社模糊查询
    */
    @Override
    public PageBean<Book> findByPress(String press, int pc) {
        try {
            return bookDao.findByPress(press,pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /*
     * @Param [criteria, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     多条件组合查询
    */
    @Override
    public PageBean<Book> findByCombination(Book criteria, int pc) {
        try {
            return bookDao.findByCombination(criteria,pc);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [bid]
     * @return com.zhangrun.entity.Book
     * 根据bid查询book对象
    */
    @Override
    public Book load(String bid) {
        try {
            return bookDao.findByBid(bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [cid]
     * @return int
     * 根据指定二级分类id查询book数量
    */
    @Override
    public int findByBookCount(String cid) {
        try {
            return bookDao.findByBookCount(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [book]
     * @return void
     * 添加图书
    */
    @Override
    public void add(Book book)  {
        try {
            bookDao.add(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [book]
     * @return void
     * 图书修改
    */
    @Override
    public void exit(Book book) {
        try {
            bookDao.exit(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [bid]
     * @return void
     * 图书删除
    */
    @Override
    public void delete(String bid) {
        try {
            bookDao.delete(bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
