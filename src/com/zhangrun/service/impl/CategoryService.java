package com.zhangrun.service.impl;

import com.zhangrun.dao.impl.CategoryDao;
import com.zhangrun.entity.Category;
import com.zhangrun.service.ICategoryService;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/19 17:26
 */
public class CategoryService implements ICategoryService {
    private CategoryDao categoryDao=new CategoryDao();

    /*
     * @Param []
     * @return java.util.List<com.zhangrun.entity.Category>
     *     查询所有分类
    */
    @Override
    public List<Category> findAll() {
        try {
            return categoryDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }
    }

    /*
     * @Param [category]
     * @return void
     * 添加分类
    */
    @Override
    public void add(Category category) {
        try {
            categoryDao.add(category);
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }
    }

    /*
     * @Param []
     * @return java.util.List<com.zhangrun.entity.Category>
     *     通过一级分类的cid查询所有一级分类不需要带二级分类
    */
    @Override
    public List<Category> findParent() {
        try {
            return categoryDao.findParent();
        } catch (SQLException e) {
            throw new RuntimeException (e);
        }
    }

    /*
     * @Param [cid]
     * @return com.zhangrun.entity.Category
     * 即可以加载一级分类也可以加载二级分类
    */
    @Override
    public Category load(String cid) {
        try {
            return categoryDao.load(cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [category]
     * @return void
     * 既可以修改一级分类也可以修改二级分类
    */
    @Override
    public void update(Category category) {
        try {
            categoryDao.update(category);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int findChildCount(String pid) {
        try {
           return categoryDao.findChildCount(pid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteParent(String pid) {
        try {
            categoryDao.deleteParent(pid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * @Param [pid]
     * @return java.util.List<com.zhangrun.entity.Category>
     *     通过父分类查询子分类
    */
    @Override
    public List<Category> findByParent(String pid) {
        try {
            return categoryDao.findByParent(pid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
