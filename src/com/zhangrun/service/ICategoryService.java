package com.zhangrun.service;

import com.zhangrun.entity.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/19 17:25
 */
public interface ICategoryService {
    //查询所有分类
    List<Category> findAll();

    //添加分类方法
    void add(Category category);

    //通过一级分类的cid查询所有一级分类不需要带二级分类
    List<Category> findParent();

    //加载分类，既可以加载一级分类也可以加载二级分类
    Category load(String cid);

    //修改二级分类，既可以修改一级分类也可以修改二级分类
    void update(Category category);

    //根据一级分类id查询该分类下是否有二级分类
    int findChildCount(String pid);

    //删除一级分类
    void deleteParent(String cid);

    //通过父分类查询子分类
    List<Category> findByParent(String pid);
}
