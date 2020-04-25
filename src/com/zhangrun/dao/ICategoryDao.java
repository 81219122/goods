package com.zhangrun.dao;

import com.zhangrun.entity.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/19 17:24
 */
public interface ICategoryDao {
    //查询所有分类
    List<Category> findAll() throws SQLException;

    //通过父分类查询子分类
    List<Category> findByParent(String pid) throws SQLException;

    //添加分类方法
    void add(Category category) throws SQLException;

    //通过一级分类的cid查询所有一级分类不需要带二级分类
    List<Category> findParent() throws SQLException;

    //加载分类，既可以加载一级分类也可以加载二级分类
    Category load(String cid) throws SQLException;

    //修改二级分类，既可以修改一级分类也可以修改二级分类
    void update(Category category) throws SQLException;

    //根据一级分类id查询该分类下是否有二级分类
    int findChildCount(String pid) throws SQLException;

    //删除一级分类
    void deleteParent(String pid) throws SQLException;
}
