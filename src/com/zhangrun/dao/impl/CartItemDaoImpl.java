package com.zhangrun.dao.impl;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.zhangrun.dao.ICartItemDao;
import com.zhangrun.entity.Book;
import com.zhangrun.entity.CartItem;
import com.zhangrun.entity.User;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/24 13:59
 */
public class CartItemDaoImpl implements ICartItemDao {
    private TxQueryRunner tx=new TxQueryRunner();

    private String toWhereSql(int len){
        StringBuilder sb=new StringBuilder("cartItemId in(");
        for (int i=0;i<len;i++){
            sb.append("?");
            if (i<len-1){//因为最后一个参数不用加逗号
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    //把一个map映射成一个CateItem
    private CartItem toCartItem(Map<String,Object> map){
        if (map ==null ||map.size()==0) return null;
        CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        User user = CommonUtils.toBean(map, User.class);
        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }

    //把多个map映射成list<map>
    private List<CartItem> toCareItemList(List<Map<String,Object>> mapList){
        List<CartItem> list=new ArrayList<CartItem>();
        for (Map<String,Object> maps:mapList){
            CartItem cartItem = toCartItem(maps);
            list.add(cartItem);
        }
        return list;
    }
    /*
     * @Param [uid]
     * @return java.util.List<com.zhangrun.entity.CartItem>
     *     根据uid查询出我的购物车
    */
    @Override
    public List<CartItem> findByUser(String uid) throws SQLException {
        String sql="select * from t_cartitem c,t_book b where c.bid=b.bid and uid = ? order by c.orderBy";
        List<Map<String, Object>> maps = tx.query(sql, new MapListHandler(), uid);
        List<CartItem> list = toCareItemList(maps);
        return list;
    }

    /*
     * @Param [uid, bid]
     * @return com.zhangrun.entity.CartItem
     * 根据uid和bid查询是否有当前条目对象
    */
    @Override
    public CartItem findByUidAndBid(String uid, String bid) throws SQLException {
        String sql="select * from t_cartitem where uid = ? and bid = ?";
        //因为CartItem对象中有引用数据类型 把查询出来的结果直接封装到CartItem对象中的话会丢失数据 所有先放到map
        Map<String, Object> map = tx.query(sql, new MapHandler(), uid, bid);
        CartItem cartItem = toCartItem(map);
        return cartItem;
    }

    /*
     * @Param [cartItemId, quantity]
     * @return void
     * 根据条目id修改数量
    */
    @Override
    public void updateQuantity(String cartItemId, int quantity) throws SQLException {
        String sql="update t_cartitem set quantity = ? where cartItemId = ?";
        tx.update(sql,quantity,cartItemId);
    }

    /*
     * @Param [cartItem]
     * @return void
     * 添加条目
    */
    @Override
    public void addCartItem(CartItem cartItem) throws SQLException {
        String sql="insert into t_cartitem(cartItemId, quantity ,bid,uid) values (?,?,?,?)";
        Object [] params ={cartItem.getCartItemId(),cartItem.getQuantity(),
                cartItem.getBook().getBid(),cartItem.getUser().getUid()
        };
        tx.update(sql,params);
    }

    /*
     * @Param [cartItemIds]
     * @return void
     * 批量删除
    */
    @Override
    public void batchDelete(String cartItemIds) throws SQLException {
        Object[] params = cartItemIds.split(",");
        String s = toWhereSql(params.length);
        String sql="delete from t_cartitem where "+s;
        tx.update(sql,params);
    }

    /*
     * @Param [cartItemId]
     * @return com.zhangrun.entity.CartItem
     * 根据id查询购物车条目
     * 多表查询 因为cartitem对象中book是引用数据类型
    */
    @Override
    public CartItem findByCartItemId(String cartItemId) throws SQLException {
        String sql="select * from t_cartitem c,t_book b where c.bid=b.bid and cartItemId = ?";
        Map<String, Object> map = tx.query(sql, new MapHandler(), cartItemId);
        return toCartItem(map);
    }

    /*
     * @Param [cartItemIds]
     * @return java.util.List<com.zhangrun.entity.CartItem>
     *     加载多个cartitem
    */
    @Override
    public List<CartItem> loadCartItems(String cartItemIds) throws SQLException {
        Object[] params = cartItemIds.split(",");
        String s = toWhereSql(params.length);
        String sql="select * from t_cartitem c,t_book b where c.bid=b.bid and "+s;
        List<Map<String, Object>> mapList = tx.query(sql, new MapListHandler(), params);
        return toCareItemList(mapList);
    }
}
