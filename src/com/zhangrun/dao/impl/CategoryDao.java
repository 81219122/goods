package com.zhangrun.dao.impl;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.zhangrun.dao.ICategoryDao;
import com.zhangrun.entity.Category;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/20 13:07
 */
public class CategoryDao implements ICategoryDao {
    private TxQueryRunner txQueryRunner=new TxQueryRunner();
    /*
     * @Param [map]
     * @return com.zhangrun.entity.Category
     * 把一个map中的数据映射到Category中
     */
    private Category toCategory(Map<String,Object> map){
        Category category = CommonUtils.toBean(map, Category.class);
        String pid = (String) map.get("pid");  //如果是一级分类那么pid为null 看数据库表即可
        if (pid!=null){//如果父分类id不为空
            //使用一个父分类对象来拦截pid,再把父分类设置给Category
            Category parent=new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }
        return category;
    }
    /*
     * @Param [mapList]
     * @return java.util.List<com.zhangrun.entity.Category>
     *     把多个map转换成多个Category
     */
    private List<Category> toCategoryList(List<Map<String,Object>> mapList){
        List<Category> categoryList=new ArrayList<Category>();
        for (Map<String,Object> map:mapList){
            Category c = toCategory(map);  //把一个map转换成一个Category
            categoryList.add(c);
        }
        return categoryList;
    }
    /*
     * @Param []
     * @return java.util.List<com.zhangrun.entity.Category>
     *     查询所有分类
     */
    @Override
    public List<Category> findAll() throws SQLException {
        //1.查询出所有一级分类
        String sql="select * from t_category where pid is null order by orderBy";
        List<Map<String, Object>> mapList = txQueryRunner.query(sql, new MapListHandler());
        List<Category> parents = toCategoryList(mapList);
        //2.循环遍历所有的一级分类为每个一级分类加载二级分类
        for (Category parent:parents){
            //查询出当前一级分类的所有子分类
            List<Category> chidlren=findByParent(parent.getCid());
            //设置给父分类
            parent.setChildren(chidlren);
        }
        return parents;
    }
    /*
     * @Param [pid]
     * @return java.util.List<com.zhangrun.entity.Category>
     *     通过父分类查询子分类
     */
    @Override
    public List<Category> findByParent(String pid) throws SQLException {
        String sql="select * from t_category where pid = ? order by orderBy";
        List<Map<String, Object>> query = txQueryRunner.query(sql, new MapListHandler(), pid);
        return toCategoryList(query);
    }

    /*
     * @Param [category]
     * @return void
     * 添加分类
    */
    @Override
    public void add(Category category) throws SQLException {
        //因为desc是数据库的关键字 所有必须加上esc键下的小浪！
        String sql="insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
        String pid=null;  //这是一级分类
        if (category.getParent() !=null){//这是二级分类
            pid=category.getParent().getCid();
        }
        Object [] params={category.getCid(),category.getCname(),pid,category.getDesc()};
        txQueryRunner.update(sql,params);
    }

    /*
     * @Param [cid]
     * @return com.zhangrun.entity.Category
     * 通过一级分类的cid查询所有一级分类不需要带二级分类
    */
    @Override
    public List<Category> findParent() throws SQLException {
        String sql="select * from t_category where pid is null";
        List<Map<String, Object>> map = txQueryRunner.query(sql, new MapListHandler());
        return toCategoryList(map);
    }

    /*
     * @Param [oid]
     * @return com.zhangrun.entity.Category
     * 即可以加载一级分类也可以加载二级分类
    */
    @Override
    public Category load(String cid) throws SQLException {
        String sql="select * from t_category where cid = ?";
        Map<String, Object> map = txQueryRunner.query(sql, new MapHandler(), cid);
        Category category = toCategory(map);
        return category;
    }

    /*
     * @Param [category]
     * @return void
     * 既可以修改一级分类也可以修改二级分类
    */
    @Override
    public void update(Category category) throws SQLException {
        String sql="update t_category set cname = ?,pid = ?,`desc` = ? where cid = ?";
        String pid=null; //这是一级分类
        if (category.getParent() !=null){  //这个不为空就是二级分类 然后给pid赋值
            pid=category.getParent().getCid();
        }
        Object [] params ={category.getCname(),pid,category.getDesc(),category.getCid()};
        txQueryRunner.update(sql,params);
    }

    /*
     * @Param [pid]
     * @return boolean
     * 根据一级分类id查询该分类下是否有二级分类
    */
    @Override
    public int findChildCount(String pid) throws SQLException {
        String sql="select count(*) from t_category where pid = ?";
        Number number = (Number) txQueryRunner.query(sql, new ScalarHandler(), pid);
        return number ==null ? 0:number.intValue();
    }

    /*
     * @Param [pid]
     * @return void
     * 删除一级分类
    */
    @Override
    public void deleteParent(String cid) throws SQLException {
        String sql="delete from t_category where cid = ?";
        txQueryRunner.update(sql,cid);
    }
}
