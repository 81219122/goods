package com.zhangrun.controller;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.zhangrun.entity.*;
import com.zhangrun.service.impl.CartItemServiceImpl;
import com.zhangrun.service.impl.OrderServiceImpl;
import com.zhangrun.utils.PaymentUtil;
import com.zhangrun.utils.UuidUtil;
import org.omg.CORBA.ORB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author zhangrun
 * @version 1.0
 * @date 2020/2/27 16:12
 */
@WebServlet("/orderServlet")
public class OrderServlet extends BaseServlet {
    private OrderServiceImpl orderService=new OrderServiceImpl();
    private CartItemServiceImpl cartItemService=new CartItemServiceImpl();
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
    /*
     * @Param [request, response]
     * @return void
     * 查看我的订单功能
    */
    public void myorder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pc = getPc(request);
        String url = getUrl(request);
        User user=(User)request.getSession().getAttribute("sessionuser");
        PageBean<Order> pb = orderService.myOrder(user.getUid(), pc);
        pb.setUrl(url);
        request.setAttribute("pb",pb);
        request.getRequestDispatcher("/jsps/order/list.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 创建订单功能
    */
    public void creatOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取所有购物车条目 然后查询
        String cartItemIds = request.getParameter("cartItemIds");
        List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);

        //2.创建order
        Order order=new Order();
        order.setOid(UuidUtil.getUuid());//设置主键
        order.setAddress(request.getParameter("address")); //设置地址
        //设置时间
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sp.format(new Date());
        order.setOrdertime(date);
        User user=(User)request.getSession().getAttribute("sessionuser");
        order.setOwner(user); //设置当前订单所有者
        order.setStatus(1);  //设置状态未付款
        //设置总计
        BigDecimal total=new BigDecimal(0);
        for (CartItem cartItem:cartItemList){
            total=total.add(new BigDecimal(cartItem.getSubtotal()+""));
        }
        order.setTotal(total.doubleValue());

        //3.创建List<orderitem> 一个orderitem对应一个cartitem
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartItem cartItem:cartItemList){
            OrderItem orderItem=new OrderItem();
            orderItem.setBook(cartItem.getBook());  //设置Book对象
            orderItem.setOrderItemId(CommonUtils.uuid()); //设置主键
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItemList.add(orderItem);
        }
        //4.把订单条目设置给订单
        order.setOrderItemList(orderItemList);

        //删除购物车所选条目
        cartItemService.batchDelete(cartItemIds);
        //5.调用service添加订单
        orderService.creatOrder(order);
        request.setAttribute("order",order);  //把order存起来
        request.getRequestDispatcher("/jsps/order/ordersucc.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 加载订单功能
    */
    public void loadOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid=request.getParameter("oid");
        Order order = orderService.loadOrder(oid);
        request.setAttribute("order",order);
        String btn=request.getParameter("btn");  //获取它来判断用户点击了哪个按钮
        request.setAttribute("btn",btn);
        request.getRequestDispatcher("/jsps/order/desc.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 取消订单功能
     *
    */
    public void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        int status = orderService.findStatus(oid);  //根据oid查询状态
        if (status !=1){//如果状态不为1
            request.setAttribute("code","error");
            request.setAttribute("msg","该状态不能取消");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }else {
            orderService.updateOrderStatus(oid,5);
            request.setAttribute("code","success");
            request.setAttribute("msg","取消订单成功");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }
    }

    /*
     * @Param [request, response]
     * @return void
     * 确认收货功能
    */
    public void confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        int status = orderService.findStatus(oid);  //根据oid查询状态
        if (status !=3){
            request.setAttribute("code","error");
            request.setAttribute("msg","该状态不能取消");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }else {
            orderService.updateOrderStatus(oid,4);
            request.setAttribute("code","success");
            request.setAttribute("msg","确认收货成功");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }
    }

    /*
     * @Param [request, response]
     * @return void
     * 删除订单
    */
    public void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid=request.getParameter("oid");
        int status = orderService.findStatus(oid);
        if (status!=5){
            request.setAttribute("code","error");
            request.setAttribute("msg","请先取消订单再删除");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }
        orderService.deleteOrder(oid);
        request.setAttribute("code","success");
        request.setAttribute("msg","订单删除成功");
        request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
    }
    /*
     * @Param [request, response]
     * @return void
     * 跳转到支付页面
    */
    public void paymentPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("order",orderService.loadOrder(request.getParameter("oid"))); //加载order 然后保存order
        request.getRequestDispatcher("/jsps/order/pay.jsp").forward(request,response);
    }

    /*
     * @Param [request, response]
     * @return void
     * 支付功能
    */
    public void payment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //加载配置文件
//        Properties properties=new Properties();
//        properties.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
//        //1.准备13个参数
//        String p0_Cmd = "Buy";//业务类型，固定值Buy
//        String p1_MerId = properties.getProperty("p1_MerId");//商号编码，在易宝的唯一标识
//        String p2_Order = request.getParameter("oid");//订单编码
//        String p3_Amt = "0.01";//支付金额
//        String p4_Cur = "CNY";//交易币种，固定值CNY
//        String p5_Pid = "";//商品名称
//        String p6_Pcat = "";//商品种类
//        String p7_Pdesc = "";//商品描述
//        String p8_Url = properties.getProperty("p8_Url");//在支付成功后，易宝会访问这个地址。
//        String p9_SAF = "";//送货地址
//        String pa_MP = "";//扩展信息
//        String pd_FrpId = request.getParameter("yh");//支付通道
//        String pr_NeedResponse = "1";//应答机制，固定值1
//        /*2.计算hmac
//        * 需要13个参数 需要keyvalue 配置文件中
//        * 需要加密算法
//        * */
//        String keyValue=properties.getProperty("keyValue");
//        String hmac= PaymentUtil.buildHmac(p0_Cmd,p1_MerId,p2_Order,p3_Amt,p4_Cur,p5_Pid,p6_Pcat,
//                p7_Pdesc,p8_Url,p9_SAF,pa_MP,pd_FrpId,pr_NeedResponse,keyValue);
//
//        //3.重定向到易宝的支付网关
//        StringBuilder sb=new StringBuilder("https://www.yeepay.com/app-merchant-proxy/node");
//        sb.append("?").append("p0_Cmd=").append(p0_Cmd);
//        sb.append("&").append("p1_MerId=").append(p1_MerId);
//        sb.append("&").append("p2_Order=").append(p2_Order);
//        sb.append("&").append("p3_Amt=").append(p3_Amt);
//        sb.append("&").append("p4_Cur=").append(p4_Cur);
//        sb.append("&").append("p5_Pid=").append(p5_Pid);
//        sb.append("&").append("p6_Pcat=").append(p6_Pcat);
//        sb.append("&").append("p7_Pdesc=").append(p7_Pdesc);
//        sb.append("&").append("p8_Url=").append(p8_Url);
//        sb.append("&").append("p9_SAF=").append(p9_SAF);
//        sb.append("&").append("pa_MP=").append(pa_MP);
//        sb.append("&").append("pd_FrpId=").append(pd_FrpId);
//        sb.append("&").append("pr_NeedResponse=").append(pr_NeedResponse);
//        sb.append("&").append("hmac=").append(hmac);
//        response.sendRedirect(sb.toString());
        String oid=request.getParameter("oid");
        int status = orderService.findStatus(oid);
        if (status==1){
            orderService.updateOrderStatus(oid,2);
            request.setAttribute("code","success");
            request.setAttribute("msg","支付成功");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }else {
            request.setAttribute("code","error");
            request.setAttribute("msg","你不能支付,请重新下单");
            request.getRequestDispatcher("/jsps/msg.jsp").forward(request,response);
        }
    }

    /*
     * @Param [request, response]
     * @return void
     * 回馈方法 支付成功后易宝访问
     * 1.引导用户浏览器重定向方法 如果用户关闭了浏览器就不能访问了
     * 2.易宝会使用点对点的通讯访问这个方法  必须回馈success 不然易宝会一直调用这个方法
    */
    public void back(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * 1. 获取12个参数
         *//*
        String p1_MerId = req.getParameter("p1_MerId");
        String r0_Cmd = req.getParameter("r0_Cmd");
        String r1_Code = req.getParameter("r1_Code");
        String r2_TrxId = req.getParameter("r2_TrxId");
        String r3_Amt = req.getParameter("r3_Amt");
        String r4_Cur = req.getParameter("r4_Cur");
        String r5_Pid = req.getParameter("r5_Pid");
        String r6_Order = req.getParameter("r6_Order");
        String r7_Uid = req.getParameter("r7_Uid");
        String r8_MP = req.getParameter("r8_MP");
        String r9_BType = req.getParameter("r9_BType");
        String hmac = req.getParameter("hmac");
        *//*
         * 2. 获取keyValue
         *//*
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("payment.properties"));
        String keyValue = props.getProperty("keyValue");
        *//*
         * 3. 调用PaymentUtil的校验方法来校验调用者的身份
         *   >如果校验失败：保存错误信息，转发到msg.jsp
         *   >如果校验通过：
         *     * 判断访问的方法是重定向还是点对点，如果要是重定向
         *     修改订单状态，保存成功信息，转发到msg.jsp
         *     * 如果是点对点：修改订单状态，返回success
         *//*
        boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, r2_TrxId,
                r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType,
                keyValue);
        if(!bool) {
            req.setAttribute("code", "error");
            req.setAttribute("msg", "无效的签名，支付失败！（你不是好人）");
            req.getRequestDispatcher("/jsps/msg.jsp").forward(req,resp);
        }
        if(r1_Code.equals("1")) {
            orderService.updateOrderStatus(r6_Order, 2);
            if(r9_BType.equals("1")) {
                req.setAttribute("code", "success");
                req.setAttribute("msg", "恭喜，支付成功！");
                req.getRequestDispatcher("/jsps/msg.jsp").forward(req,resp);
            } else if(r9_BType.equals("2")) {
                resp.getWriter().print("success");
            }
        }
    }*/
    }
}
