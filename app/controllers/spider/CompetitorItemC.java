package controllers.spider;

import java.io.File;
import java.util.Map;

//import models.TaobaoProduct;
import models.base.CompetitorItem;

import com.avaje.ebean.Page;

import controllers.JavaSpiderC;
import play.mvc.Controller;
import play.mvc.Result;
import tool.CommonUtil;
import tool.Constans;
import views.html.spider.*;

public class CompetitorItemC extends Controller {
	public static Result dataview() {
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String page = param.get("page")[0];
		String rows = param.get("rows")[0];
		Map<String,String> params = null;
		if(param.get("filterRules")!=null){
			String filterRules = param.get("filterRules")[0];
			params = CommonUtil.getRequestParams(filterRules);
		}
		Page<CompetitorItem> pageeee= CompetitorItem.page(Integer.parseInt(page)-1, Integer.parseInt(rows), "", "",params);
		String json = CommonUtil.getPageJson(pageeee);
    	return ok(json);
    }
	public static Result toCompetitorItems() {
	       
        return ok(
        		toCompetitorItems.render()
        );
    }
	public static Result getExcel() {
		String fileName = CompetitorItem.downloadExcel();
		File dir = new File(Constans.DOWNLOAD_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File downloadfile = new File(dir+File.separator+fileName);
		
		response().setContentType("application/x-download");  
		response().setHeader("Content-disposition","attachment; filename=spider.xlsx");
		
		return ok(downloadfile);
	}
	public static Result getSingleProducts(){
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		
		JavaSpiderC.getSingleProducts(param);
		String msg = CommonUtil.jsosRtnMsg(true, "爬虫成功!");
        return ok(msg);
	}
	public static Result retryBadPorducts(){
		JavaSpiderC.retryBadPorducts();
		String msg = CommonUtil.jsosRtnMsg(true, "爬虫成功!");
        return ok(msg);
	}

}
