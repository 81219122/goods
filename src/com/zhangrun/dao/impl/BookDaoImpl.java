package com.zhangrun.dao.impl;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.sun.mail.imap.protocol.BODY;
import com.zhangrun.dao.IBookDao;
import com.zhangrun.entity.*;
import javassist.expr.Expr;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 15:26
 */
public class BookDaoImpl implements IBookDao {
    private TxQueryRunner txQueryRunner=new TxQueryRunner();
    /*
     * @Param [expressionList 查询条件, pc 当前页码]
     * @return com.zhangrun.entity.PageBean
    */
    @Override
    public PageBean findByCriteria(List<Expression> expressionList, int pc) throws SQLException {
        //1.每页记录数
        int ps = PageConstants.book_page_size;
        //2.通过expression来生成where子句
        StringBuilder wheresql=new StringBuilder(" where 1 = 1");  //1=1  因为条件后面要加and处理
        List<Object> params=new ArrayList<Object>();
        for (Expression e:expressionList){
            wheresql.append(" and ").append(e.getName()).append(" ").append(e.getOperator()).append(" ");
            if (!e.getOperator().equals("is null")){ //如果运算符是is null 就不给它?占位符
                wheresql.append("?");
                params.add(e.getValue()); //把条件的值放到params中
            }
        }
        //3.总记录数tr
        String sql="select count(*) from t_book " +wheresql;
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),params.toArray());
        int tr = number.intValue();  //得到总记录数

        //4.得到beanList，即当前页记录
        sql="select * from t_book" +wheresql + " order by orderBy limit ?,?";
        params.add((pc-1)*ps); //当前页首页记录的下标
        params.add(ps);  //一共查询几行，即每页记录数
        List<Book> beanlist=txQueryRunner.query(sql,new BeanListHandler<Book>(Book.class),params.toArray());
        //5.创建PageBean，设置参数
        PageBean<Book> pageBean=new PageBean();
        pageBean.setPs(ps);
        pageBean.setPc(pc);
        pageBean.setTr(tr);
        pageBean.setBeanList(beanlist);
        return pageBean;
    }

    /*
     * @Param [cid, pc 当前页码]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Category>
     *    按分类查询
    */
    @Override
    public PageBean<Book> findByCategory(String cid, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("cid","=",cid));
        return findByCriteria(expressionList,pc);
    }
    /*
     * @Param [bname, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按照书名模糊查询
    */
    @Override
    public PageBean<Book> findByBname(String bname, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("bname","like","%"+bname+"%"));
        return findByCriteria(expressionList,pc);
    }

    /*
     * @Param [anthor, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按作者名模糊查询
    */
    @Override
    public PageBean<Book> findByAuthor(String author, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("author","like","%"+author+"%"));
        return findByCriteria(expressionList, pc);
    }

    /*
     * @Param [press, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     按出版社模糊查询
    */
    @Override
    public PageBean<Book> findByPress(String press, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("press","like","%"+press+"%"));
        return findByCriteria(expressionList,pc);
    }
    /*
     * @Param [criteria, pc]
     * @return com.zhangrun.entity.PageBean<com.zhangrun.entity.Book>
     *     多条件组合查询
    */
    @Override
    public PageBean<Book> findByCombination(Book criteria, int pc) throws SQLException {
        List<Expression> expressionList=new ArrayList<Expression>();
        expressionList.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
        expressionList.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
        expressionList.add(new Expression("press","like","%"+criteria.getPress()+"%"));
        return findByCriteria(expressionList,pc);
    }

    /*
     * @Param [bid]
     * @return com.zhangrun.entity.Book
     * 根据bid查询book
    */
    @Override
    public Book findByBid(String bid) throws SQLException {
        //查询到当前图书所有信息包括父分类和子分类
        String sql="select * from t_book b,t_category c where b.cid =c.cid and b.bid= ?";
        //因为Book实体类有引用数据类型Category所有把查询出来的结果先封装到map 再把map映射到Book对象
        Map<String, Object> map = txQueryRunner.query(sql, new MapHandler(), bid);

        //把父分类赋给book对象
        String pid=(String) map.get("pid");
        Category parent =new Category();
        parent.setCid(pid);

        //把map映射到Book对象但是cid没有映射进去
        Book book = CommonUtils.toBean(map, Book.class);
        //这个category只映射到了cid
        Category category = CommonUtils.toBean(map, Category.class);
        category.setParent(parent);  //把父分类赋给book对象里面的分类对象属性
        book.setCategory(category);

        return book;
    }

    /*
     * @Param [cid]
     * @return int
     * 根据指定二级分类id查询book数量
    */
    @Override
    public int findByBookCount(String cid) throws SQLException {
        String sql="select count(*) from t_book where cid = ?";
        Number number=(Number) txQueryRunner.query(sql,new ScalarHandler(),cid);
        return number ==null ?0:number.intValue();
    }

    /**
     * 添加图书
     * @param book
     * @throws SQLException
     */
    public void add(Book book) throws SQLException {
        String sql = "insert into t_book(bid,bname,author,price,currPrice," +
                "discount,press,publishtime,edition,pageNum,wordNum,printtime," +
                "booksize,paper,cid,image_w,image_b)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getImage_w(),book.getImage_b()};
        txQueryRunner.update(sql, params);
    }

    /*
     * @Param [book]
     * @return void
     * 图书修改
    */
    @Override
    public void exit(Book book) throws SQLException {
        String sql="update t_book set bname = ?,author = ?,price = ?,currPrice = ?" +
                ",discount = ?,press = ?,publishtime = ?,edition = ?,pageNum = ?,wordNum = ?," +
                "printtime = ?,booksize = ?,paper = ?,cid = ? where bid = ?";
        Object[] params = {book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getBid()};
        txQueryRunner.update(sql,params);
    }

    /*
     * @Param [bid]
     * @return void
     * 图书删除
    */
    @Override
    public void delete(String bid) throws SQLException {
        String sql="delete from t_book where bid = ?";
        txQueryRunner.update(sql,bid);
    }

}
