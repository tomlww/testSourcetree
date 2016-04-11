package com.tiny.business.goods.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tiny.business.goods.model.GoodsModel;

public interface GoodsService {
	/**
	 * 添加商品
	 * @param request
	 * @param goods
	 * @return
	 */
	boolean add(HttpServletRequest request, GoodsModel goods) throws Exception;
	/**
	 * 获取商品列表
	 * @param catId
	 * @return
	 * @throws Exception
	 */
	List<GoodsModel> getListGoods(String catId) throws Exception;
	/**
	 * 根据商品id获取商品详情
	 * @param goodsId
	 * @return
	 */
	GoodsModel getGood(String goodsId) throws Exception;
	
}
