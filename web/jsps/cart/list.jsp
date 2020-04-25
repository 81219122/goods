<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
	$(function () {
        showTotal();
        cleanCheckedBox();
        cleanNo();
        cleanTotal()
        $("#selectAll").click(function () {
			//1.获取全选状态
			var bool=$("#selectAll").attr("checked");
			if (bool){//如果被全选中 让所有复选框都被选中 然后设置结算按钮生效 重新计算总计
                $(":checkbox[name=checkboxBtn]").attr("checked",true);
                setJieSuan(true);
                showTotal();
			}else { //如果全选没被选中，让所有复选框都取消选中 然后设置结算按钮失效 重新计算总计
                $(":checkbox[name=checkboxBtn]").attr("checked",false);
                setJieSuan(false);
                showTotal();
			}
        });

        //给所有条目的复选框添加click事件
		$(":checkbox[name=checkboxBtn]").click(function () {
		    //获取所有复选框的数量
			var all=$(":checkbox[name=checkboxBtn]").length;
			//获取被选中的复选框数量
			var select=$(":checkbox[name=checkboxBtn][checked=true]").length;
			if (all ==select){//全部选中
			    $("#selectAll").attr("checked",true);
			    setJieSuan(true);  //结算按钮生效
			}else if (select ==0){//全部没选中 撤销全选按钮
                $("#selectAll").attr("checked",false);
                setJieSuan(false);  //结算按钮失效
			}else{
                $("#selectAll").attr("checked",false);
                setJieSuan(true);//结算按钮生效
			}
			showTotal();
        });

    	//给减号添加click事件
		$(".jian").click(function() {
            // 获取cartItemId
            var id = $(this).attr("id").substring(0, 32);
            // 获取输入框中的数量
            var quantity = $("#" + id + "Quantity").val();
            // 判断当前数量是否为1，如果为1,那就不是修改数量了，而是要删除了。
            if(quantity == 1) {
                if(confirm("您是否真要删除该条目？")) {
                    location = "/goods/cartItemServlet?method=batchDelete&cartItemIds=" + id;
                }
            } else {
                sendUpdateQuantity(id, quantity-1);
            }
        });

        // 给加号添加click事件
        $(".jia").click(function() {
            // 获取cartItemId
            var id = $(this).attr("id").substring(0, 32);
            // 获取输入框中的数量
            var quantity = $("#" + id + "Quantity").val();
            sendUpdateQuantity(id, Number(quantity)+1);
        });
    });



	/*发送ajax请求修改条目数量
	* cache:默认为true，设置为false将不会从浏览器缓存中加载请求信息。
	* asyn：默认为true，及异步请求。false为同步请求，同步请求将锁住浏览器，用户其他操作必须等待请求完成才可以执行
	*/
    function sendUpdateQuantity(id, quantity) {
        $.ajax({
            async:false,
            cache:false,
            url:"/goods/cartItemServlet",
            data:{method:"updateQuantity",cartItemId:id,quantity:quantity},
            type:"POST",
            dataType:"json",
            success:function(result) {
                //1. 修改数量
                $("#" + id + "Quantity").val(result.quantity);
                //2. 修改小计
                $("#" + id + "Subtotal").text(result.subtotal);
                //3. 重新计算总计
                showTotal();
            }
        });
    }
/*
* 计算总计
 */
function showTotal() {
	var total=0;
	$(":checkbox[name=checkboxBtn][checked=true]").each(function () {
		//2.获取复选框的值，及其他元素的前缀cartitemId
		var id=$(this).val();   /*拿到  value="${cartItem.cartItemId}"*/
		//3.再通过前缀找到小计，获取其文本
		var text=$("#"+id+"Subtotal").text();
		//4.累计计算
		total+=Number(text);
    });
	//5.把总计显示在元素上  round方法就是把total保留两位小数。
	$("#total").text(round(total,2));
}
//统一设置所有条目的复选按钮
	function setCartItemCheckBox(bool) {
        $(":checkbox[name=checkboxBtn]").attr("check", bool)
    }
//设置结算按钮样式
	function setJieSuan(bool) {
		if (bool){
		    $("#jiesuan").removeClass("kill").addClass("jiesuan");
		    $("#jiesuan").unbind("click"); //撤销当前元素的所有点击事件
		}else{//去掉样式
            $("#jiesuan").removeClass("jiesuan").addClass("kill");
            $("#jiesuan").click(function () {
                //让按钮链接失效
				return false;
            });
		}
    }
//如果购物车没有条目要取消全选复选框
function cleanCheckedBox() {
	var len=$(":checkbox[name=checkboxBtn]").length;
	if (len==0){
	    $("#selectAll").attr("checked",false);
	    setJieSuan(false);
	}
}

//默认购物车里的复选框和结算按钮都失效
function cleanNo() {
	$(":checkbox[name=checkboxBtn]").attr("checked",false);
	$("#selectAll").attr("checked",false);
	setJieSuan(false);
}

//批量删除
function batchDelete() {
    var cartItemIds=new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function () {
		cartItemIds.push($(this).val());
    })
	location="/goods/cartItemServlet?method=batchDelete&cartItemIds="+cartItemIds;
}

//结算
function jiesuan() {
    var cartItemId=new Array();
    $(":checkbox[name=checkboxBtn][checked=true]").each(function () {
        cartItemId.push($(this).val());
    });
    //把数组的值tostring，然后赋值给表单中的隐藏hidden 参数
	$("#cartItemIds").val(cartItemId.toString());
	//把总计的值也保存到表单
	$("#hiddenTotal").val($("#total").text());
	//提交表单
    $("#jiesuanForm").submit();
}
//默认总计为0
function cleanTotal() {
	$("#total").text(0);
}
</script>
  </head>
  <body>
	<c:choose>
		<c:when test="${empty cartItemList}">
			<table width="95%" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="right">
						<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
					</td>
					<td>
						<span class="spanEmpty">您的购物车中暂时没有商品</span>
					</td>
				</tr>
			</table>
		</c:when>
		<c:otherwise>
<br/>
<br/>
<table width="95%" align="center" cellpadding="0" cellspacing="0">
	<tr align="center" bgcolor="#efeae5">
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
		</td>
		<td colspan="2">商品名称</td>
		<td>单价</td>
		<td>数量</td>
		<td>小计</td>
		<td>操作</td>
	</tr>

	<c:forEach items="${cartItemList}" var="cartItem">
	<tr align="center">
		<td align="left">
			<input value="${cartItem.cartItemId}" type="checkbox" name="checkboxBtn" checked="checked"/>
		</td>
		<td align="left" width="70px">
			<a class="linkImage" href="<c:url value='/bookServlet?method=load&bid=${cartItem.book.bid}'/>"><img border="0" width="54" align="top" src="<c:url value='/${cartItem.book.image_b}'/>"/></a>
		</td>
		<td align="left" width="400px">
		    <a href="<c:url value='/bookServlet?method=load&bid=${cartItem.book.bid}'/>"><span>${cartItem.book.bname}</span></a>
		</td>
		<td><span>&yen;<span class="currPrice" id="12345CurrPrice">${cartItem.book.currPrice}</span></span></td>
		<td>
			<a class="jian" id="${cartItem.cartItemId}Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId}Quantity" type="text" value="${cartItem.quantity}"/><a class="jia" id="${cartItem.cartItemId}Jia"></a>
		</td>
		<td width="100px">
			<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId}Subtotal">${cartItem.subtotal}</span></span>
		</td>
		<td>
			<a href="<c:url value='/cartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId}'/>">删除</a>
		</td>
	</tr>

	</c:forEach>

	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete()">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jiesuan()" id="jiesuan" class="jiesuan" ></a>
		</td>
	</tr>
</table>
	<form id="jiesuanForm" action="<c:url value='/cartItemServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="total" id="hiddenTotal"/>
		<input type="hidden" name="method" value="loadCartItemList"/>
	</form>
		</c:otherwise>
	</c:choose>

  </body>
</html>
