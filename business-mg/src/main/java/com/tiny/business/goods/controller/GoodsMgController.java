package com.tiny.business.goods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tiny.business.goods.model.CategoryModel;
import com.tiny.business.goods.model.GoodsModel;
import com.tiny.business.goods.service.CategoryService;
import com.tiny.business.goods.service.GoodsService;


/**
 * 商品controller
 * @author ThinkPad
 *
 */
@Controller
@RequestMapping("/mgGoods")
public class GoodsMgController {
	private static final Log logger = LogFactory.getLog(GoodsMgController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GoodsService goodsService;
	
	@RequestMapping("/init")
	public ModelAndView init(){
		return new ModelAndView("goods/mgCategory");
	}
	
	/**
	 * 添加商品分类
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addCategory(HttpServletRequest request,String categoryListString) {
		Map<String, Object> rmap = new HashMap<String, Object>();
		try {
			rmap = categoryService.add(rmap,categoryListString,request);//添加商品分类
		} catch (Exception e) {
			logger.error("====添加商品分类异常===",e);
			rmap.put("status", "fail");//添加失败
		}
		return rmap;
	}
	/**
	 * 商品分类列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCategoryList")
	@ResponseBody
	public Map<String, Object> getCategoryList(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<CategoryModel> list = categoryService.getCategoryList(request);
			map.put("result", list);//结果集
			map.put("status", "success");//状态
		} catch (Exception e) {
			logger.error("====查询商品分类异常===",e);
		}
		return map;
	}
	
	/**
	 * 删除分类 进行逻辑删除
	 * @param catId
	 * @return
	 */
	@RequestMapping("/det")
	@ResponseBody
	public Map<String, Object> delete(String catId){
		Map<String, Object> rmap = new HashMap<String, Object>();
		try {
			int count = categoryService.delete(catId);
			if(count>0)
				rmap.put("status", "success");
			else
				rmap.put("status", "fail");
		} catch (Exception e) {
			logger.error("====删除商品分类异常===catId=="+catId,e);
		}
		return rmap;
	}
	
	/**
	 * 跳转到添加商品的页面
	 * @return
	 */
	@RequestMapping("/addGoodView")
	public ModelAndView addGood(String catId){
		ModelAndView model = new ModelAndView("goods/mgGoodDatil");
		model.addObject("catId", catId);//分类id
		return model;
	}
	/**
	 * 添加商品
	 * @return
	 */
	@RequestMapping("/addgoods")
	public String addGoods(HttpServletRequest request,GoodsModel goods){
		try {
			boolean bl = goodsService.add(request,goods);
		} catch (Exception e) {
			logger.error("====添加商品异常===goods=="+goods,e);
		}
		return null;
	}
	
	/**
	 * 获取商家自己所以的商品列表商品列表
	 * @return
	 */
	@RequestMapping("/listGoods")
	@ResponseBody
	public Map<String, Object> listGoods(String catId){
		Map<String, Object> rmap = new HashMap<String, Object>();
		 try {
			 List<GoodsModel> list = goodsService.getListGoods(catId);
			 rmap.put("status", "success");
			 rmap.put("list", list);
		} catch (Exception e) {
			logger.error("====商品列表商品列表异常===listGoods()==",e);
			rmap.put("status", "error");
		}
		 return rmap;
	}
	/**
	 * 获取商品详情
	 * @return
	 */
	public Map<String, Object> getGood(String goodsId){
		Map<String, Object> rmap = new HashMap<String, Object>();
		try {
			GoodsModel goodModel = goodsService.getGood(goodsId);
			 rmap.put("status", "success");
		} catch (Exception e) {
			logger.error("====商品详情异常===getGood()==goodsId=="+goodsId,e);
			rmap.put("status", "error");
		}
		return rmap;
	}
	
	public void testSour(){
		System.out.println("orger分支");
	}
	
	public void user(){
		System.out.println("wangwu的分支");
	}
	
}
