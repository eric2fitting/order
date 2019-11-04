package com.taotao.store.order.service;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.store.order.bean.TaotaoResult;
import com.taotao.store.order.dao.IOrder;
import com.taotao.store.order.pojo.Order;
import com.taotao.store.order.pojo.PageResult;
import com.taotao.store.order.pojo.ResultMsg;
import com.taotao.store.order.util.ValidateUtil;

@Service
public class OrderService {
	
	private static final ObjectMapper objectMapper=new ObjectMapper();
	
	@Autowired
	private IOrder orderDao;

	public TaotaoResult createOrder(String json) {
		Order order=null;
		try {
			order=objectMapper.readValue(json, Order.class);
			//校验order对象
			ValidateUtil.validate(order);
		} catch (Exception e) {
			return TaotaoResult.build(400, "请求参数有误!");
		} 
		try {
			//生成订单ID,规则为：userid+生前时间戳
			String orderId=order.getUserId()+""+System.currentTimeMillis();
			order.setOrderId(orderId);
			
			//设置订单的初始状态为未付款
			order.setStatus(1);
			//设置订单的创建时间
			order.setCreateTime(new Date());
			order.setUpdateTime(order.getCreateTime());
			//设置买件评价状态，初始为未评价
			order.setBuyerRate(0);
			//持久化order对象
			orderDao.createOrder(order);
			return TaotaoResult.ok(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("呵呵哒！");
		System.out.println("又改了！");
		return TaotaoResult.build(400, "订单保存失败");
		
	}

	public Order queryOrderById(String orderId) {
		Order order=orderDao.queryOrderById(orderId);
		return order;
	}

	public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, Integer page, Integer count) {
		return orderDao.queryOrderByUserNameAndPage(buyerNick, page, count);
	}

	public ResultMsg changeOrderStatus(String json) {
		
		return null;
	}

	
	
}
