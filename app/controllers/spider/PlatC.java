package controllers.spider;

import static play.data.Form.form;

import java.util.Map;

import models.base.Plat;

import org.springframework.beans.BeanUtils;

import com.avaje.ebean.Page;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import tool.CommonUtil;
import views.html.spider.toPlat;

public class PlatC extends Controller{
	
	public static Result dataview() {
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String page = param.get("page")[0];
		String rows = param.get("rows")[0];
		Map<String,String> params = null;
		if(param.get("filterRules")!=null){
			String filterRules = param.get("filterRules")[0];
			params = CommonUtil.getRequestParams(filterRules);
		}
		Page<Plat> pageeee= Plat.page(Integer.parseInt(page)-1, Integer.parseInt(rows), "", "",params);
		String json = CommonUtil.getPageJson(pageeee);
    	return ok(json);
    }
	public static Result savePlat() {
	        Form<Plat> demoForm = form(Plat.class).bindFromRequest();
	        String msg = "";
	        Plat demo=demoForm.get();
	        try{
		        demo.save();
		        msg = CommonUtil.jsosRtnMsg(true, "保存成功!");
	        }catch (Exception e) {
	        	msg = CommonUtil.jsosRtnMsg(false, "保存失败!");
			}
	        
	        return ok(msg);
	}
	public static Result updatePlat() {
		String id = request().getQueryString("id");
		Form<Plat> demoForm = form(Plat.class).bindFromRequest();
		String dd=demoForm.toString();
		
	    Plat demo=demoForm.get();
		if(id==null){
			id = demo.id.toString();
		}
        Plat db = Plat.find.where().eq("id",id).findUnique();
        BeanUtils.copyProperties(demo, db);
        db.update();
        String msg = CommonUtil.jsosRtnMsg(true, "修改成功!");
        return ok(msg);
	}
	public static Result deletePlat() {
		Map<String,String[]> param = request().body().asFormUrlEncoded();
		String id = param.get("id")[0];
		 Plat db = Plat.find.where().eq("id",id).findUnique();
        db.delete();
        String msg = CommonUtil.jsosRtnMsg(true, "删除成功!");
        return ok(msg);
	}
	public static Result toPlat() {
       
        return ok(
        		toPlat.render()
        );
    }

}
