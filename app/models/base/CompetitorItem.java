package models.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//import models.permision.SysUser;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import controllers.JavaSpider;
import tool.Constans;
//import tool.HttpClientHelper;
import tool.comfunction;
import play.data.format.Formats;
import play.db.ebean.*;
/**
 *
 * @author ncayan
 */
@Entity
@Table(name = "COMPETITOR_ITEMS")
public class CompetitorItem extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	public String brandName;//品牌
	public String platName;//平台名称
    public String itemName;// 问题
    public String itemUrl;// 网页链接
	public String imgUrl;// 图片链接
	public String dropPrice;// 售价
	public String itemPrice;// 售价
	public String totalVolume;// 销量
	public String monthVolume;//月销量
	public String rates;// 评价数
	public String season;//产品价
	public String productId;//货号
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date createTime;//获取时间
    public String itemDes;
    // 构造方法初始化数据
    public CompetitorItem() {
        itemName = "";
        itemUrl = "";
        imgUrl = "";
		itemPrice = "";
		totalVolume = "";
		monthVolume ="";
 		rates ="";
		dropPrice = "";
		brandName="";
		platName="";
		createTime=new Date();
        season="";
        productId="";
    }
	public CompetitorItem(Map<String,String> params) {
		//初始化
		brandName=params.get("brandName");
		itemName = params.get("itemName");
        itemUrl = params.get("itemUrl");
        imgUrl = params.get("imgUrl");
		itemPrice = params.get("itemPrice");
		totalVolume = params.get("totalVolume");
		monthVolume = "???";
		dropPrice =params.get("dropPrice");;
		rates=params.get("rates");
		platName=params.get("platName");
		createTime=new Date();
		season="";
        productId="";
        String url=itemUrl;
		url="http:"+url;
		System.out.println("获取详情页");
		String properties="";
		
		//================================================================================================
		//获取详情页元素
		
		dropPrice=params.get("dropPrice")!=null?params.get("dropPrice"):"???";
		monthVolume=params.get("monthVolume")!=null?params.get("monthVolume"):"???";
		rates=params.get("rates")!=null?params.get("rates"):"???";
		properties=params.get("properties")!=null?params.get("properties"):"";
		
		//=================================================================================================
		//解析并存储详情页获取的属性
		//用HttpClient获取的文本
		if(properties!=""){
		//用webdriver获取最终文本
		properties=properties.toLowerCase();
		properties=JavaSpider.replaceBlank(properties);
		
		
		System.out.println("详情页完成");
		
		String[] itemDescription=properties.split(";");
		//
		for(String t:itemDescription){
			System.out.println(t);
			if(t.contains("货号")){
				productId=t.substring((t.indexOf(":")+1));
			}else
			if(t.contains("年份")||t.contains("时间")){
				season=t.substring((t.indexOf(":")+1));
			}
		}
		}
		itemDes=properties;
		
		//===============================================================================================
		/*//获取动态页面信息
		content = JavaSpider.getPage(url);
		content=JavaSpider.replaceBlank(content);
		//吊牌价
		Pattern pattern1=Pattern.compile("<DTclass=tb-metatit>价格.+?tm-price>(.+?)</SPAN>");
		Matcher matcher1=pattern1.matcher(content);
		if(matcher1.find())
			dropPrice=matcher1.group(1);
		//月销量
		//<SPANclass=tm-label>月销量</SPAN><SPANclass=tm-count>(.+?)</SPAN>
		Pattern pattern2=Pattern.compile("<SPANclass=tm-label>月销量</SPAN><SPANclass=tm-count>(.+?)</SPAN>");
		Matcher matcher2=pattern2.matcher(content);
		if(matcher2.find())
			monthVolume=matcher2.group(1);
		//评价
		//<SPANclass=tm-label>累计评价</SPAN><SPANclass=tm-count>(.+?)</SPAN>
		Pattern pattern3=Pattern.compile("<SPANclass=tm-label>累计评价</SPAN><SPANclass=tm-count>(.+?)</SPAN>");
		Matcher matcher3=pattern3.matcher(content);
		if(matcher3.find())
			rates=matcher3.group(1);*/
		
    }
    @Override
    public String toString() {
        return "\n品牌：" + brandName +"\n名称：" + itemName + "\n货号：" + productId
        		+ "\n年份季节：" + season
        		+ "\n连接：" + itemUrl + "\n图片：" + 
        		imgUrl + "\n价格："+dropPrice+">"+ itemPrice + "\n销量："+ totalVolume + "\n评价数："+rates+ 
        		"\n时间"+createTime+"\n属性：" + "\n";
    }
    
    /**
     * 从GET到的字符串中获取详情页商品属性
     * @param str
     * @return
     */
    String getString(String str){
    	if(str.contains("&nbsp;")){
    		str=str.replace("&nbsp;", " ");
    	}
    	if(!str.contains("&#")){
    		return str;
    	}
    	String str1=str.substring(0, str.indexOf("&#"));
    	String[] str2=str.substring(str.indexOf("&#")).split(";");
    	for(String s:str2)
    	{
    		if(s.startsWith("&#"))
    			str1+=""+(char)Integer.parseInt(s.replace("&#", ""));
    		else{
    			if(s.contains("&#")){
    				s=s.substring(0, s.indexOf("&#"))+(char)Integer.parseInt(s.substring(s.indexOf("&#")).replace("&#", ""));
    			}
    			str1+=s;
    		}
    	}
    	System.out.println(str1);
    	return str1;
    }
    
    
    public static Model.Finder<Long, CompetitorItem> find = new Model.Finder<Long, CompetitorItem>(
			Long.class, CompetitorItem.class);

    /**
     * 获取PAGE
     * @param page
     * @param pageSize
     * @param sortBy
     * @param order
     * @param params
     * @return
     */
    public static Page<CompetitorItem> page(int page, int pageSize, String sortBy,
			String order,Map<String,String> params) {
		
		ExpressionList<CompetitorItem> lst = find.where();
		if(null!=params&&params.size()>0){
			Set<String> set = params.keySet();
			for(String key:set){
				if (StringUtils.isNotBlank(key)
						&& StringUtils.isNotEmpty(key))
					lst.ilike(key,params.get(key)+"%");
			}
		}
		
		return lst.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}
    
    /**
     * 根据款名称查找
     * @param num
     * @return
     */
    public static CompetitorItem findByName(String num){
    	return find.where().eq("itemName", num)
				.findUnique();
    }
    
    /**
     * 查找获取失败的款
     * @return
     */
    public static List<CompetitorItem> findBad(){
    	ExpressionList<CompetitorItem> lst = find.where().in("rates", "???");
		 List<CompetitorItem> list = lst.findList();
		 return list;
    }
    
    /**
     * 批量修改
     * @param brandName
     * @param platName
     */
	public static void updateBatch(String brandName,String platName){
		 ExpressionList<CompetitorItem> lst = find.where().in("brandName", brandName).in("platName", platName);
		 List<CompetitorItem> list = lst.findList();
		 try{
			 for(CompetitorItem d:list){// ClassCastException: models.base.CompetitorItem cannot be cast to models.base.CompetitorItem
				  Ebean.delete(d);
			 }
		 }catch(ClassCastException e){
			 //updateBatch(brandName,platName);
		 }
	}
	
	/**
	 * 单个款修改
	 * @param ci
	 */
	public static void updateSingle(CompetitorItem ci){
		CompetitorItem d=find.where().eq("itemName", ci.itemName).eq("brandName", ci.brandName).eq("platName", ci.platName)
		.findUnique();
		d.delete();
		ci.save();
	}
	
	/**
	 * 生成Excel文件
	 * @return
	 */
	public static String downloadExcel() {
		String fileName = "";
		List<CompetitorItem> itemList = CompetitorItem.find.all();		
		return downloadList(itemList);
	}
	public static String downloadBystore(Map<String,String[]> param) {
		String fileName = "";
		//List<CompetitorItem> itemList = CompetitorItem.find.all();
		String[] brands=param.get("brandName")[0].split(",");
		String[] plats=param.get("platName")[0].split(",");
		List<CompetitorItem> itemLists=new ArrayList<CompetitorItem>();
		for(int t=0;t<brands.length;t++){
			String plat=plats[t];
			String brand=brands[t];
			ExpressionList<CompetitorItem> itemList = CompetitorItem.find.where().eq("platName", plat).eq("brandName",brand);
			System.out.println(itemList.findList());
			List<CompetitorItem> item=itemList.findList();
			itemLists.addAll(item);
		}
		return downloadList(itemLists);
	}
	public static String downloadList(List<CompetitorItem> itemList) {
		String fileName = "";	
		List<String> proList=new ArrayList();
		//--
		proList.add("页面库存");
		proList.add("收藏人气");
		proList.add("材质成分");
		proList.add("销售渠道类型");
		proList.add("风格");
		proList.add("组合形式");
		proList.add("厚薄");
		proList.add("服装版型");
		proList.add("款式");
		proList.add("裙长");
		proList.add("袖长");
		proList.add("领型");
		proList.add("袖型");
		proList.add("图案");
		proList.add("厚薄");
		proList.add("流行元素/工艺");
		proList.add("使用年龄");
		proList.add("年份季节");
		proList.add("颜色分类");
		proList.add("尺码");
		//--
		if (itemList != null && itemList.size() > 0) {
			try {
				File dir = new File(Constans.DOWNLOAD_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				List<String> lstTitle = new ArrayList<String>();
				List<String> lstFloor = new ArrayList<String>();
						
				lstTitle.add("平台名称");
				lstTitle.add("品牌名称");
				lstTitle.add("款名称");
				lstTitle.add("图片链接");
				lstTitle.add("吊牌价");
				lstTitle.add("销售价");
				lstTitle.add("月销量");
				lstTitle.add("总销量");
				lstTitle.add("评论数");
				lstTitle.add("产品季");
				lstTitle.add("货号");
				lstTitle.add("获取时间");
				//--
				for(String t:proList){
					lstTitle.add(t);
				}
				//--
				lstTitle.add("属性");
				
				for (int i = 0; i < itemList.size(); i++) {
					StringBuilder sb = new StringBuilder();
					CompetitorItem item = itemList.get(i);
					sb.append(item.platName).append("$");
					sb.append(item.brandName).append("$");
					sb.append(item.itemName).append("$");
					sb.append("http:"+item.imgUrl).append("$");
					sb.append(item.dropPrice).append("$");
					sb.append(item.itemPrice).append("$");
					sb.append(item.monthVolume).append("$");
					sb.append(item.totalVolume).append("$");
					sb.append(item.rates).append("$");
					sb.append(item.season).append("$");
					sb.append(item.productId).append("$");
					sb.append(comfunction.getDateTimeStr(item.createTime)).append("$");
					//--
					for(String t:proList){
						sb.append(getSinglePro(t,item.itemDes)).append("$");
					}
					//--
					sb.append(item.itemDes).append("$");
					
					lstFloor.add(sb.toString());
				}
				String downloadTime = "爬虫文件："+comfunction.getCurFileName();
				
				fileName = downloadTime+".xlsx";
				comfunction.WriteExcel_x(dir + File.separator + fileName, "item",
						lstFloor, lstTitle);
			} catch (Exception e) {

				e.printStackTrace();

			}
		}
		return fileName;
	}
	static String getSinglePro(String proName,String pros){
		String pro="-";
		if(pros.contains(proName)){
			pro=pros.substring(pros.indexOf(proName)+proName.length()+1);
			pro=pro.substring(0, pro.indexOf(";"));
		}
		return pro;
	}
}
