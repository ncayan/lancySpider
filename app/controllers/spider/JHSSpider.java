package controllers.spider;

import java.io.*;

import play.db.ebean.*;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.HttpClientHelper;
import models.base.CompetitorItem;
import models.base.Plat;

/**
 *
 * @author ncayan
 */

public class JHSSpider{
	
	//列表页获取对应值的正则表达式
	//area 
	String areaRegular;
	//url 
	String urlRegular;
	//img
	String imgRegular;
	//name
	String nameRegular;
	//price
	String priceRegular;
	//totalVolume
	String volumeRegular;
	//rates
	String ratesRegular;
	
	//详情页获取元素所用类名
	//dropPrice
	String dropPriceClass;
	//monthVolume
	String monthVolumeClass;
	//rates
	String ratesClass;
	//AttrList;
	String attrListClass;
	
	String brandName;//当前名称
	String brandUrl;//当前Url
	
	WebDriver driver = null;
	public JHSSpider(){
		
		//area 
		areaRegular="<divid=content>(.+?)更多心动品牌";
		//url 
		urlRegular="<aclass=\"link-boxhover-avil\"href=\"(.+?)\"";
		//img
		imgRegular="<imgclass=item-picsrc=\"(.+?)\"";
		//name
		nameRegular="<h3class=nowraptitle=(.+?)>";
		//price
		priceRegular="<emclass=j_actprice><spanclass=yen>(.+?)</span>";
		//totalVolume
		volumeRegular="<divclass=sold-num><em>(.+?)</em>";
		//rates->dropPrice
		ratesRegular="<delclass=orig-price>¥(.+?)</del>";
		
		//item

		//详情页获取元素所用类名
		//dropPrice
		dropPriceClass="oprice,originprice";
		//monthVolume
		monthVolumeClass="noclass,noclass";//聚划算不可用
		//rates
		ratesClass="j_tabeval";
		//AttrList
		attrListClass="noid,noclass";//聚划算不可用

		brandName="聚划算";//当前名称
		brandUrl="";//当前Url
	}
	
	public JHSSpider(Plat pl){
		
		//area 
		areaRegular=pl.areaRegular;
		//url 
		urlRegular=pl.urlRegular;
		//img
		imgRegular=pl.imgRegular;
		//name
		nameRegular=pl.nameRegular;
		//price
		priceRegular=pl.priceRegular;
		//totalVolume
		volumeRegular=pl.volumeRegular;
		//rates
		ratesRegular=pl.ratesRegular;

		//详情页获取元素所用类名
		//dropPrice
		dropPriceClass=pl.dropPriceClass;
		//monthVolume
		monthVolumeClass=pl.monthVolumeClass;
		//rates
		ratesClass=pl.ratesClass;
		//AttrList;
		attrListClass=pl.areaRegular;

		brandName="天猫";//当前名称
		brandUrl="";//当前Url
	}
	
	/*public OMSSpider(String brand){
		//area 
		areaRegular="<divid=\"content\">(.+?)</div>";
		//url 
		urlRegular="<aclass=\"link-boxstatus-avil\".+?href=\"(.+?)\".+?>";
		//img
		imgRegular="<imgclass=\"item-pic\".+?src=\"(.+?)\".+?>";
		//name
		nameRegular="<h3class=\"shortname\"title=\"(.+?)\".+?>";
		//price
		priceRegular="<spanclass=\"actPrice\"><em><spanclass=\"yen\">(.+?)</span><spanclass=\"cent\">(.+?)</span></em></span>";
		//totalVolume
		volumeRegular="<divclass=\"soldcount\"><em>(.+?)</em>";
		//rates->dropPrice
		ratesRegular="<spanclass=\"dock\"><delclass=\"oriPrice\">¥(.+?)</del>";

		//详情页获取元素所用类名
		//dropPrice
		dropPriceClass="oprice,originPrice";
		//monthVolume
		//monthVolumeClass="noclass,noclass";聚划算不可用
		//rates
		ratesClass="J_TabEval,triangle";
		//AttrList
		//attrListClass="noid,noclass";聚划算不可用

		brandName="聚划算";//当前名称
		brandUrl="";//当前Url
	}*/

    /**
     * @param args the command line arguments
     */
    /*public  void main(String[] args) {
        // TODO code application logic here
        // 定义即将访问的链接
        //String url = "http://www.zhihu.com/explore/recommendations";
        //String url ="https://list.tmall.com/search_product.htm?spm=a220m.1000858.1000724.4.lytXmA&cat=50025135&brand=3856489&q=%D1%C5%D3%A8&sort=d&style=g&from=mallfp..pc_1_searchbutton&smAreaId=110100&tmhkmain=0#J_Filter";
        String url ="https://list.tmall.com/search_product.htm?spm=a220m.1000858.1000724.4.zmrpM2&cat=50025135&brand=3856489&q=%D1%C5%D3%A8&sort=d&style=g&from=mallfp..pc_1_searchbutton&industryCatId=50025135&smAreaId=110100&tmhkmain=0#J_Filter";
        // 访问链接并获取页面内容
        //String result = sendGet(url);
		String result = Get(url);
        // 使用正则匹配图片的src内容
        //ArrayList<String> imgSrc = RegexString_s(result, "question_link.+?>(.+?)<");
        // 获取该页面的所有的知乎对象
        ArrayList<CompetitorItem> myItem = GetItems(result);
        System.out.println(myItem);
       
    }*/
	
	public void start(Map<String,String> params){
		System.setProperty("webdriver.ie.driver",".\\public\\webDriver\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		String url=params.get("startUrl");
		String brand=params.get("brandName");
		String plat=params.get("platName");
		String result ="";
		result = getPage(url);
		result=result.toLowerCase();
		result=result.replace("\'", "");
		ArrayList<CompetitorItem> myItem = GetItems2(result,params);
		ArrayList<CompetitorItem> myHotItem = GetHotItems2(result,params);
		myItem.addAll(myHotItem);
		if(myItem.size()>=4)
		saveCompetitorItems(myItem,brand,plat);
		driver.quit();
	}
	
	/**
	 * 重试单个款
	 * @param num
	 */
	 void getSinglgProduct(String num){
		CompetitorItem ci=CompetitorItem.findByName(num);
		System.out.println(num);
		Map<String,String> params=new HashMap();
    	params.put("itemUrl", ci.itemUrl);
    	params.put("imgUrl", ci.imgUrl);
    	params.put("itemName", ci.itemName);
    	params.put("itemPrice", ci.itemPrice);
    	params.put("totalVolume", ci.totalVolume);
    	params.put("rates", ci.rates);
    	params.put("brandName",ci.brandName);
    	params.put("platName", ci.platName);
    	ci=new CompetitorItem(params);
    	
    			//ci.save();
		CompetitorItem.updateSingle(ci);
	}
	
	/**
	 * 重试获取失败的款
	 */
	 void retryBadPorducts(){
		List<CompetitorItem> cl=CompetitorItem.findBad();
		for(CompetitorItem ci:cl){
			Map<String,String> params=new HashMap();
	    	params.put("itemUrl", ci.itemUrl);
	    	params.put("imgUrl", ci.imgUrl);
	    	params.put("itemName", ci.itemName);
	    	params.put("itemPrice", ci.itemPrice);
	    	params.put("totalVolume", ci.totalVolume);
	    	params.put("rates", ci.rates);
	    	params.put("brandName",ci.brandName);
	    	params.put("platName", ci.platName);
	    	ci=new CompetitorItem(params);
	    	
	    			//ci.save();
			CompetitorItem.updateSingle(ci);
		}
	}
	
	 void saveCompetitorItems(ArrayList<CompetitorItem> myItem,String brand,String plat){
		CompetitorItem.updateBatch(brand,plat);
		for(CompetitorItem c:myItem){
			c.save();
		}
	}
	
	/**
	 * 通过httpclinet获取整页
	 * @param url
	 * @return
	 */
    public  String Get(String url){
		HttpClientHelper httpClient = new HttpClientHelper("", "utf-8");
		String content = httpClient.spiderHelper(url);	
  		return replaceBlank(content);
	}
    
    /**
     * 通过webdriver获取整页
     * @param url
     * @return
     */
    public  String getPage(String url){
		Long t1=System.currentTimeMillis();
		//创建一个WebDriver实例
		//System.setProperty("webdriver.ie.driver",".\\public\\webDriver\\IEDriverServer.exe");
        //WebDriver driver = new InternetExplorerDriver();
        // 访问url
        driver.get(url);
        driver.manage().timeouts().pageLoadTimeout(150, TimeUnit.SECONDS);
        //WebDriverWait wait = new WebDriverWait(driver,150);
        /*String s = ( new WebDriverWait( driver, 150 )) .until(
       	     new ExpectedCondition<String>(){
       	         @Override
       	         public String apply(WebDriver d) {
       	             return d.getPageSource();
       	         }	     
      		}
      	);*/
        int height=0;
        for(int x=0	;x<200;x++){
        	try {
        	   String setscroll = "document.documentElement.scrollTop=" + height;  
        	   height+=300;
        	   JavascriptExecutor jse=(JavascriptExecutor) driver;
        	   jse.executeScript(setscroll);
        	   Thread.sleep(400);
        	} catch (Exception e) {
        		System.out.println("Fail to set the scroll.");
        	}   
        }
        // 另一种访问方法
        // driver.navigate().to("http://www.google.com");
        String s=driver.getPageSource();
        s=replaceBlank(s);
        Long t2=System.currentTimeMillis();
        System.out.println("unit Time is: " + (t2-t1));
        //关闭浏览器
        //driver.quit();
        return s;
	}
    public  Map<String,String> getTMItmes(String url){
    	Map<String,String> params=new HashMap();
    	return params;
    }
    
    /**
     * 详情页
     * 通过webdriver获取内容
     * @param url
     * @return
     */
    public  Map<String,String> getTMPageDrop(String url){
		Long t1=System.currentTimeMillis();
		Map<String,String> params=new HashMap();
		params.put("properties", "???");
       	params.put("MonthVolume", "???");
       	params.put("rates", "???");
       	params.put("rates", "???");
		//创建一个WebDriver实例
		//System.setProperty("webdriver.ie.driver",".\\public\\webDriver\\IEDriverServer.exe");
        //WebDriver driver = new InternetExplorerDriver();
        // 访问
        //driver.get("//detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.vDcAup&id=523363605394&skuId=3115720509868&areaId=110100&cat_id=50025135&rn=07a7c2b5d48311cd90b93319290c3160&user_id=378537552&is_b=1");
        driver.manage().timeouts().pageLoadTimeout(65, TimeUnit.SECONDS);
        
        // 另一种访问方法
        // driver.navigate().to("http://www.google.com");
        // 显示查询结果title
        //String s=driver.getPageSource();
        
		/*
		//dropPrice
		dropPriceClass="noclass,noclass";
		//monthVolume
		monthVolumeClass="noclass,noclass";
		//rates
		ratesClass="noid,noclass";
		//AttrList;
		attrListClass="noid,noclass";
		*/
        try{
        	driver.get(url);
        	String setscroll = "document.documentElement.scrollTop=500";  
	     	JavascriptExecutor jse=(JavascriptExecutor) driver;
	     	jse.executeScript(setscroll);
		//获取吊牌价
        /*WebElement e = ( new WebDriverWait( driver, 20 )) .until(
        	     new ExpectedCondition<WebElement>(){
        	         @Override
        	         public WebElement apply(WebDriver d) {
        	             return d.findElement(By.className(dropPriceClass.split(",")[0])).findElement(By.className(dropPriceClass.split(",")[1]));
        	         }	     
       		}
       	);*/
        //String s1=e.getText();
        
		//获取月销量，聚划算中不可用
        /*e = ( new WebDriverWait( driver, 20 )) .until(
       	     new ExpectedCondition<WebElement>(){
       	         @Override
       	         public WebElement apply(WebDriver d) {
       	             return d.findElement(By.className(monthVolumeClass.split(",")[0])).findElement(By.className(monthVolumeClass.split(",")[1]));
       	         }	     
      		}
      	);
        String s2=e.getText();*/
		
        //获取评价
	    WebElement e = ( new WebDriverWait( driver, 20 )) .until(
      	     new ExpectedCondition<WebElement>(){
      	         @Override
      	         public WebElement apply(WebDriver d) {
      	             return d.findElement(By.className(ratesClass));
      	         }	     
     		}
     	);
       	
       	String s3=e.getText();
       	
       	//List<WebElement>	el=driver.findElement(By.id( "J_AttrList" )).findElements(By.tagName("li"));
		
		//获取商品属性，聚划算中不可用
       	/*List<WebElement> el = ( new WebDriverWait( driver, 30 )) .until(
         	     new ExpectedCondition<List<WebElement>>(){
         	         @Override
         	         public List<WebElement> apply(WebDriver d) {
         	             return d.findElement(By.id(attrListClass.split(",")[0])).findElements(By.tagName(attrListClass.split(",")[1]));
         	         }	     
        		}
        	);
       	StringBuilder sb=new StringBuilder();
        for(WebElement we:el){
        	sb.append(we.getText()+";");
        }
        String s4=sb.toString();
       	s4=replaceBlank(s4);*/

       	params.clear();
       	params.put("properties", "???");//获取商品属性，聚划算中不可用
       	//params.put("dropPrice", "???");
       	params.put("monthVolume", "???");//获取月销量，聚划算中不可用
       	params.put("rates", s3);
       	System.out.println("rates"+s3);
        }catch(Exception e){
        	//driver.quit();
        }
        finally{
        	//driver.quit();
        }
        Long t2=System.currentTimeMillis();
        System.out.println("unit Time is: " + (t2-t1));
        return params;
	}
    public  Map<String,String> getTMPageDrop2(String url){
    	Map<String,String> params=new HashMap();
    	//创建一个WebDriver实例
		//System.setProperty("webdriver.ie.driver",".\\public\\webDriver\\IEDriverServer.exe");
        //WebDriver driver = new InternetExplorerDriver();
        try{
        	driver.get(url);
        	String setscroll = "document.documentElement.scrollTop=500";  
	     	JavascriptExecutor jse=(JavascriptExecutor) driver;
	     	jse.executeScript(setscroll);
	     	Thread.sleep(400);
	     	String s=driver.getPageSource();
	     	params=dealItemDetail(s);
        }catch(Exception e){
        	//driver.quit();
        }
        finally{
        	//driver.quit();
        }
    	return params;
    }
      
    /**
     * 列表页
     * 通过正则表达式获取内容
     * @param content
     * @param param
     * @return
     */
	//聚划算添加，获取爆款
	ArrayList<CompetitorItem> GetHotItems2(String content,Map<String,String> param){
		//重新定义正则表达式
		//area 
		areaRegular="<divid=content>(.+?)</div>";
		//url 
		urlRegular="<aclass=\"link-boxstatus-avil\".+?href=\"(.+?)\".+?>";
		//img
		imgRegular="<imgclass=item-pic.+?src=\"(.+?)\".+?>";
		//name
		nameRegular="<h3.+?title=(.+?)>";
		//price
		priceRegular="<spanclass=actprice><em><spanclass=yen>(.+?)</span>";
		//totalVolume
		volumeRegular="<divclass=soldcount><em>(.+?)</em>";
		//rates->dropPrice
		ratesRegular="<spanclass=dock><delclass=oriprice>¥(.+?)</del>";
		return GetItems2(content,param);
	 }
	 
     ArrayList<CompetitorItem> GetItems2(String content,Map<String,String> param) {
         // 预定义一个ArrayList来存储结果
         ArrayList<CompetitorItem> results = new ArrayList<CompetitorItem>();
         String brand=param.get("brandName");
         String plat=param.get("platName");
         //减小匹配范围
         Pattern pattern = Pattern
                 .compile(areaRegular);
               Matcher matcher = pattern.matcher(content);
         if(matcher.find())
         	content= matcher.group(1);
         System.out.println(content);
         // 用来匹配url，也就是问题的链接
 		//url
        Pattern patternX = Pattern
                 .compile("<divclass=\"status-avil\">(.+?)</li>");
               Matcher matcherX = patternX.matcher(content);
        Boolean isFind = matcherX.find();
        while (isFind) {
        	String singleItem = matcherX.group(1);
        	
        	Map<String,String> params=dealItem(singleItem);
        	params.put("brandName", brand);
          	params.put("platName", plat);
          	Map<String,String> paramd=getTMPageDrop2(params.get("itemUrl"));
          	System.out.println("haveRates?"+paramd);
          	params.put("rates", paramd.get("rates"));
          	/*if(null==params.get("itemUrl")){
    
          		params.put("properties", "???");//获取商品属性，聚划算中不可用
             	params.put("dropPrice", "???");
             	params.put("monthVolume", "???");//获取月销量，聚划算中不可用
             	params.put("rates", "???");
        			
              	params.put("itemUrl", "???");
              	params.put("imgUrl", "???");
              	params.put("itemName", "???");
              	params.put("itemPrice", "???");
              	params.put("totalVolume", "???");
              	
              	params.put("dropPrice", "???");
          	}*/
          	CompetitorItem zhihuTemp = new CompetitorItem(params);
          	results.add(zhihuTemp);
          	isFind=matcherX.find();
        }
 		
         return results;
     }
     
     Map<String,String> dealItemDetail(String content){
    	 Map<String,String> params=new HashMap();
    	 content=replaceBlank(content);
    	 content=content.toLowerCase();
    	 Pattern pattern1 = Pattern.compile("历史评价<strong>(.+?)</strong>");
         Matcher matcher1 = pattern1.matcher(content);
         Boolean isFind = matcher1.find();
         if(isFind)
        	 params.put("rates", matcher1.group(1));
    	 return params;
     }
     
     Map<String,String> dealItem(String content){
    	
    	Map<String,String> params=new HashMap();
    	
    	Pattern pattern1 = Pattern.compile(urlRegular);
         Matcher matcher1 = pattern1.matcher(content);
 		//img
 		Pattern pattern2 = Pattern.compile(imgRegular);
         Matcher matcher2 = pattern2.matcher(content);
 		//name
 		Pattern pattern3 = Pattern.compile(nameRegular);
         Matcher matcher3 = pattern3.matcher(content);
 		//price
 		Pattern pattern4 = Pattern.compile(priceRegular);
         Matcher matcher4 = pattern4.matcher(content);
 		//totalVolume
 		Pattern pattern5 = Pattern.compile(volumeRegular);
         Matcher matcher5 = pattern5.matcher(content);
         //rates
         Pattern pattern6 = Pattern.compile(ratesRegular);
         Matcher matcher6 = pattern6.matcher(content);
         System.out.println(content);
    	/*此处用到参数列表
 		//area 
 		String areaRegular;
 		//url 
 		String urlRegular;
 		//img
 		String imgRegular;
 		//name
 		String nameRegular;
 		//price
 		String priceRegular;
 		//totalVolume
 		String volumeRegular;
 		//rates
 		String ratesRegular;*/
		 Boolean isFind = matcher1.find();
		 Boolean b2 = matcher2.find();
		 Boolean b3 = matcher3.find();
		 Boolean b4 = matcher4.find();
		 Boolean b5 = matcher5.find();
		 Boolean b6 = matcher6.find();
		 
		 if(isFind){
		String itemUrl=matcher1.group(1);
      	
      	//params=getTMPageDrop(itemUrl);
      	
      	params.put("properties", "???");//获取商品属性，聚划算中不可用
     	params.put("dropPrice", "???");
     	params.put("monthVolume", "???");//获取月销量，聚划算中不可用
     	params.put("rates", "???");
			
      	params.put("itemUrl", itemUrl);
      	params.put("imgUrl", b2?matcher2.group(1):"?");
      	params.put("itemName", b3?matcher3.group(1):"?");
      	params.put("itemPrice", b4?matcher4.group(1):"?");
      	params.put("totalVolume", b5?matcher5.group(1):"?");
      	
      	params.put("dropPrice", b6?matcher6.group(1):"?");
      	}
      	
      	

          //CompetitorItem zhihuTemp = new CompetitorItem(matcher1.group(1),
          		//matcher2.group(1),matcher3.group(1),matcher4.group(1),matcher5.group(1),matcher6.group(1),brand);
      	System.out.println("搜索页+1");
      	System.out.println(params.toString());
    	return params;
    }
    /**
     * 清除回车等特殊字符
     * @param str
     * @return
     */
    public  String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            dest=dest.replaceAll("\\\\\"","\"");
        }
        return dest;
    }
}
