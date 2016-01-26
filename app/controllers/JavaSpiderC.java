package controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;

import controllers.spider.JHSSpider;
import controllers.spider.TMSpider;
import models.base.Competitor;
import models.base.CompetitorItem;
import models.base.Plat;
import play.mvc.Controller;
import play.mvc.Result;
import tool.CommonUtil;
import tool.HttpClientHelper;

public class JavaSpiderC extends Controller{
	static HttpClientHelper httpClient = new HttpClientHelper("", "utf-8");
	
	public static Result startSpider(Map<String,String[]> param){
		//Map<String,String[]> param = request().body().asFormUrlEncoded();
		String url=param.get("startUrl")[0];
		String brand=param.get("brandName")[0];
		String plat=param.get("platName")[0];
		Map<String,String> params=new HashMap();
		
		params.put("startUrl", url);
		params.put("brandName", brand);
		params.put("platName", plat);
		
		CompetitorItem.updateBatch(brand,plat);
		JavaSpider.start(params);
		
		return ok("搜索完成");
		
	}
	
	public static void testers(Map<String,String[]> param){ 
		String ids=param.get("id")[0];
		String[] ss=ids.split(",");
		for(String s:ss){
			Competitor cp=Competitor.findById(s);
			Map<String,String> params=new HashMap<>();
			String plat=cp.platName;
			String brand=cp.brandName;
			params.put("startUrl", cp.startUrl);
			params.put("brandName", brand);
			params.put("platName", plat);
			
			
			JavaSpider.start(params);
			System.out.println(plat+brand);
		}
	}
	
	public static String tester2(Map<String,String[]> param){ 
		String rtn="";
		String ids=param.get("id")[0];
		String[] ss=ids.split(",");
		for(String s:ss){
			Competitor cp=Competitor.findById(s);
			Map<String,String> params=new HashMap<>();
			String plat=cp.platName;
			String brand=cp.brandName;
			params.put("startUrl", cp.startUrl);
			params.put("brandName", brand);
			params.put("platName", plat);
			Plat pl=Plat.findByName(plat);
			switch(plat){
			case"天猫":
				if(pl!=null){
					TMSpider ts=new TMSpider(pl);
				}
				TMSpider ts=new TMSpider();
				ts.start(params);
				rtn=plat+brand+"抓取完成";
				break;
			case"聚划算":
				if(pl!=null){
					JHSSpider ts2=new JHSSpider(pl);
				}
				JHSSpider ts2=new JHSSpider();
				ts2.start(params);
				rtn=plat+brand+"抓取完成";
				break;
			default:
				rtn=plat+"未配置爬虫算法";
				System.out.println(plat+"未配置");
				break;
			}
			System.out.println(plat+brand);
		}
		return rtn;
	}
	
	public static void getAll(){
		List<Competitor> lst=Competitor.findAll();
		for(Competitor cp:lst){
			Map<String,String> params=new HashMap<>();
			String plat=cp.platName;
			String brand=cp.brandName;
			params.put("startUrl", cp.startUrl);
			params.put("brandName", brand);
			params.put("platName", plat);
			JavaSpider.start(params);
			System.out.println(plat+brand);
		}
	}
	
	public static void getSingleProducts(Map<String,String[]> param){
		String ids=param.get("name")[0];
		String[] ss=ids.split(",");
		for(String s:ss){
			JavaSpider.getSinglgProduct(s);
		}
	}
	
	public static void retryBadPorducts(){
		JavaSpider.retryBadPorducts();
	}


}
