package controllers.spider;

import static play.data.Form.form;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import models.interfacetrace.InterfaceLog;
import models.base.Competitor;
import models.base.CompetitorItem;

import org.springframework.beans.BeanUtils;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import tool.CommonUtil;
import tool.Constans;
import tool.comfunction;
import views.html.spider.*;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;

import controllers.JavaSpider;
import controllers.JavaSpiderC;
import controllers.spider.TMSpider;

public class CompetitorC extends Controller{
	public static Result dataview() {
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String page = param.get("page")[0];
		String rows = param.get("rows")[0];
		Map<String,String> params = null;
		if(param.get("filterRules")!=null){
			String filterRules = param.get("filterRules")[0];
			params = CommonUtil.getRequestParams(filterRules);
		}
		Page<Competitor> pageeee= Competitor.page(Integer.parseInt(page)-1, Integer.parseInt(rows), "", "",params);
		String json = CommonUtil.getPageJson(pageeee);
    	return ok(json);
    }
	public static Result saveCompetitor() {
	        Form<Competitor> demoForm = form(Competitor.class).bindFromRequest();
	        String msg = "";
	        Competitor demo=demoForm.get();
	        try{
		        demo.save();
		        msg = CommonUtil.jsosRtnMsg(true, "保存成功!");
	        }catch (Exception e) {
	        	msg = CommonUtil.jsosRtnMsg(false, "保存失败!");
			}
	        
	        return ok(msg);
	}
	public static Result updateCompetitor() {
		String id = request().getQueryString("id");
		Form<Competitor> demoForm = form(Competitor.class).bindFromRequest();
		String dd=demoForm.toString();
		
	    Competitor demo=demoForm.get();
		if(id==null){
			id = demo.id.toString();
		}
        Competitor db = Competitor.find.where().eq("id",id).findUnique();
        BeanUtils.copyProperties(demo, db);
        db.update();
        String msg = CommonUtil.jsosRtnMsg(true, "修改成功!");
        return ok(msg);
	}
	public static Result deleteCompetitor() {
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String id = param.get("id")[0];
		 Competitor db = Competitor.find.where().eq("id",id).findUnique();
        db.delete();
        String msg = CommonUtil.jsosRtnMsg(true, "删除成功!");
        return ok(msg);
	}
	public static Result toCompetitor() {
       
        return ok(
        		toCompetitor.render()
        );
    }
	public static Result startSpider(){
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		
		JavaSpiderC.testers(param);
		String msg = CommonUtil.jsosRtnMsg(true, "爬虫成功!");
        return ok(msg);
	}
	public static Result startSpider2(){
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String rtn=JavaSpiderC.tester2(param);
		String msg = CommonUtil.jsosRtnMsg(true, rtn);
        return ok(msg);
	}
	public static Result getSingleProducts(){
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		
		JavaSpiderC.getSingleProducts(param);
		String msg = CommonUtil.jsosRtnMsg(true, "爬虫成功!");
        return ok(msg);
	}
	public static Result downloadBtStore(){
		Map<String,String[]> param = request().queryString();
		String fileName = CompetitorItem.downloadBystore(param);
		File dir = new File(Constans.DOWNLOAD_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File downloadfile = new File(dir+File.separator+fileName);
		
		response().setContentType("application/x-download");  
		response().setHeader("Content-disposition","attachment; filename=sipder.xlsx");
		return ok(downloadfile);
	}

}
