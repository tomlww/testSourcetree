package com.tiny.business.goods.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tiny.business.goods.dao.GoodsDetailImgMapper;
import com.tiny.business.goods.dao.GoodsGalleryMapper;
import com.tiny.business.goods.dao.GoodsMapper;
import com.tiny.business.goods.model.GoodsDetailImgModel;
import com.tiny.business.goods.model.GoodsGalleryModel;
import com.tiny.business.goods.model.GoodsModel;
import com.tiny.business.goods.service.GoodsService;
import com.tiny.business.sys.service.DictitemService;
import com.tiny.business.util.CommonUtil;
import com.tiny.business.util.FtpUtil;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService{
	private static final Log logger = LogFactory.getLog(GoodsServiceImpl.class);
	
	@Autowired
	private DictitemService dictitemService;
	
	@Autowired
	private GoodsGalleryMapper goodsGalleryMapper;
	
	@Autowired
	private GoodsDetailImgMapper goodsDetailImgMapper;
	
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public boolean add(HttpServletRequest request, GoodsModel goods)
			throws Exception {
		String suppliersId = "2";//店主微信号
		String providerName ="芒果店主";//店名
		Map<String, Object> rmap = new HashMap<String, Object>();
		//Map<String, Object> rmap = dictitemService.getFtpInfo();
		rmap = getUploadPath(request);//获取上传的路径
		//添加商品型号
		
		String goodsId = CommonUtil.createRandomNumber();//产生8位随机数
		goods.setGoodsId(goodsId);
		goods.setProviderName(providerName);
		goods.setSuppliersId(suppliersId);//商品id
		int count = goodsMapper.insertData(goods);//添加商品信息
		//添加商品图片
		List<String> listtypeGoodimg = (List<String>) rmap.get("typeGoodimg"); //获取商品图片
		List<String> listtypeDatailImg = (List<String>) rmap.get("typeDatailImg"); //获取商品图片
		GoodsGalleryModel goodsGallery =  new GoodsGalleryModel();
		GoodsDetailImgModel goodsDetailImg = new GoodsDetailImgModel();
		if(null!=listtypeGoodimg && listtypeGoodimg.size()>0){
			for(String s:listtypeGoodimg){
				goodsGallery.setGoodsId(goodsId);
				goodsGallery.setImgUrl(s);
				goodsGalleryMapper.insertData(goodsGallery);//添加商品图片
			}
			
		}
		if(null!=listtypeDatailImg && listtypeDatailImg.size()>0){
			for(String s:listtypeDatailImg){
				goodsDetailImg.setGoodsId(goodsId);
				goodsDetailImg.setImgUrl(s);
				goodsDetailImgMapper.insertData(goodsGallery);//添加商品详情图片
			}
		}
		
		return true;
	}
	
	/**
	 * 获取上传的路径
	 * @param request
	 * @return
	 */
	private Map<String, Object> getUploadPath(HttpServletRequest request){
		Map<String, Object> rmap = new HashMap<String, Object>();
		rmap = dictitemService.getFtpInfo();
		//rmap = FtpUtil.upload(rmap, request);//上传图片
		List<String> typeGoodimg = new ArrayList<String>();//商品图片集合
		List<String> typeDatailImg = new ArrayList<String>();//商品详情图片
		try {
			//String suppliersId = "2";//从session中获取
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  //创建一个通用的多部分解析器  
			if(multipartResolver.isMultipart(request)){  //判断 request 是否有文件上传,即多部分请求  
			    MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  //转换成多部分request  
			    Iterator<String> iter = multiRequest.getFileNames();  //取得request中的所有文件名  
			    while(iter.hasNext()){  
			    MultipartFile file = multiRequest.getFile(iter.next());    //取得上传文件  
			    String path = FtpUtil.upload(file, multiRequest);
			    if("typeGoodimg".equals(file.getName())){
			    	typeGoodimg.add(path);
			    }else{
			    	typeDatailImg.add(path);
			    }
			  }
			}
		} catch (Exception e) {
			logger.error("==== CommonUploadController 上传异常===",e);
			rmap.put("msg", "上传失败");
			rmap.put("status", false);
			return  rmap;
		}  
		rmap.put("typeGoodimg", typeGoodimg);
		rmap.put("typeDatailImg", typeDatailImg);
		return rmap;
	}
	
	/**
	 * 获取商品列表
	 */
	@Override
	public List<GoodsModel> getListGoods(String catId) throws Exception{
		GoodsModel goods = new GoodsModel();
		String suppliersId = "2";//店主微信号
		goods.setSuppliersId(suppliersId);
		goods.setCatId(catId);//分类id
		List<GoodsModel> listGoods = goodsMapper.queryForList(goods);
		for(GoodsModel g:listGoods){
			String goodsId = g.getGoodsId();
			List goodsGallery = goodsGalleryMapper.queryForList(goodsId); //获取商品图片
			g.setGoodsGallery(goodsGallery);
			List goodsDetailImg = goodsDetailImgMapper.queryForList(goodsId);//获取商品详情图片
			g.setGoodsDetailImg(goodsDetailImg);
		}
		return listGoods;
	}
	
	/**
	 * 获取商品详情
	 */
	@Override
	public GoodsModel getGood(String goodsId) throws Exception{
		 String suppliersId = "2";
		 GoodsModel pGood = new GoodsModel();
		 pGood.setGoodsId(goodsId);
		 pGood.setSuppliersId(suppliersId);
		 GoodsModel good = (GoodsModel) goodsMapper.queryObject(pGood);
		 List goodsGallery = goodsGalleryMapper.queryForList(goodsId); //获取商品图片
		 good.setGoodsGallery(goodsGallery);
		 List goodsDetailImg = goodsDetailImgMapper.queryForList(goodsId);//获取商品详情图片
		 good.setGoodsDetailImg(goodsDetailImg);
		 return good;
	}
	
	

}
