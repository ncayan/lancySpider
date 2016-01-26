package controllers;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import tool.HttpClientHelper;
import models.base.CompetitorItem;

/**
 *
 * @author ncayan
 */

public class JavaSpider {
	
	String brandName;//当前名称
	String brandUrl;//当前Url

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
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
	
	static void start(Map<String,String> params){
		String url=params.get("startUrl");
		String brand=params.get("brandName");
		String plat=params.get("platName");
		String result ="";
		result = getPage(url);
		result=result.toLowerCase();
		result=result.replace("\'", "");
		ArrayList<CompetitorItem> myItem = GetItems(result,params);
		if(myItem.size()>=4)
		saveCompetitorItems(myItem,brand,plat);
	}
	
	/**
	 * 重试单个款
	 * @param num
	 */
	static void getSinglgProduct(String num){
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
	static void retryBadPorducts(){
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
	
	static void saveCompetitorItems(ArrayList<CompetitorItem> myItem,String brand,String plat){
		CompetitorItem.updateBatch(brand,plat);
		for(CompetitorItem c:myItem){
			c.save();
		}
	}
	static void test(){
		String url = "https://elegantprosper.tmall.com/view_shop.htm?spm=a220m.1000858.0.0.zQAAnj&shop_id=60616656&rn=a618c3fa8f5f859f90b9c3e85ae92e49";
		String result = Get(url);
	
	} 
	
	/**
	 * 通过httpclinet获取整页
	 * @param url
	 * @return
	 */
    public static String Get(String url){
		HttpClientHelper httpClient = new HttpClientHelper("", "utf-8");
		String content = httpClient.spiderHelper(url);	
  		return replaceBlank(content);
	}
    
    /**
     * 通过webdriver获取整页
     * @param url
     * @return
     */
    public static String getPage(String url){
		Long t1=System.currentTimeMillis();
		//创建一个WebDriver实例
		System.setProperty("webdriver.ie.driver",".\\public\\webDriver\\IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
        // 访问url
        driver.get(url);
        // 另一种访问方法
        // driver.navigate().to("http://www.google.com");
        // 显示查询结果title
        String s=driver.getPageSource();
        s=replaceBlank(s);
        Long t2=System.currentTimeMillis();
        System.out.println("unit Time is: " + (t2-t1));
        //关闭浏览器
        driver.quit();
        return s;
	}
    public static Map<String,String> getTMItmes(String url){
    	Map<String,String> params=new HashMap();
    	return params;
    }
    
    /**
     * 详情页
     * 通过webdriver获取内容
     * @param url
     * @return
     */
    public static Map<String,String> getTMPageDrop(String url){
		Long t1=System.currentTimeMillis();
		Map<String,String> params=new HashMap();
		params.put("properties", "???");
       	params.put("MonthVolume", "???");
       	params.put("rates", "???");
       	params.put("rates", "???");
		//创建一个WebDriver实例
		System.setProperty("webdriver.ie.driver",
			    ".\\public\\webDriver\\IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
        // 访问
        //driver.get("//detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.vDcAup&id=523363605394&skuId=3115720509868&areaId=110100&cat_id=50025135&rn=07a7c2b5d48311cd90b93319290c3160&user_id=378537552&is_b=1");
        driver.manage().timeouts().pageLoadTimeout(65, TimeUnit.SECONDS);
        
        // 另一种访问方法
        // driver.navigate().to("http://www.google.com");
        // 显示查询结果title
        //String s=driver.getPageSource();
        
        try{
        	driver.get(url);
        WebElement e = ( new WebDriverWait( driver, 20 )) .until(
        	     new ExpectedCondition<WebElement>(){
        	         @Override
        	         public WebElement apply(WebDriver d) {
        	             return d.findElement(By.className( "tb-wrap" )).findElement(By.className( "tm-price" ));
        	         }	     
       		}
       	);
        String s1=e.getText();
        
        e = ( new WebDriverWait( driver, 20 )) .until(
       	     new ExpectedCondition<WebElement>(){
       	         @Override
       	         public WebElement apply(WebDriver d) {
       	             return d.findElement(By.className( "tm-ind-sellCount" )).findElement(By.className( "tm-count" ));
       	         }	     
      		}
      	);
        String s2=e.getText();
       
       	e = ( new WebDriverWait( driver, 20 )) .until(
      	     new ExpectedCondition<WebElement>(){
      	         @Override
      	         public WebElement apply(WebDriver d) {
      	             return d.findElement(By.id( "J_ItemRates" )).findElement(By.className( "tm-count" ));
      	         }	     
     		}
     	);
       	
       	String s3=e.getText();
       	
       	//List<WebElement>	el=driver.findElement(By.id( "J_AttrList" )).findElements(By.tagName("li"));
       	List<WebElement> el = ( new WebDriverWait( driver, 30 )) .until(
         	     new ExpectedCondition<List<WebElement>>(){
         	         @Override
         	         public List<WebElement> apply(WebDriver d) {
         	             return d.findElement(By.id( "J_AttrList" )).findElements(By.tagName("li"));
         	         }	     
        		}
        	);
       	StringBuilder sb=new StringBuilder();
        for(WebElement we:el){
        	sb.append(we.getText()+";");
        }
        String s4=sb.toString();
       	s4=replaceBlank(s4);
       	System.out.println(s1+" "+s2+" "+s3);
       	params.clear();
       	params.put("properties", s4);
       	params.put("dropPrice", s1);
       	params.put("monthVolume", s2);
       	params.put("rates", s3);
        }catch(Exception e){
        	driver.quit();
        }
        finally{
        	driver.quit();
        }
        Long t2=System.currentTimeMillis();
        System.out.println("unit Time is: " + (t2-t1));
        return params;
	}
    
    static String RegexString(String targetStr, String patternStr) {
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
        // 相当于埋好了陷阱匹配的地方就会掉下去
        Pattern pattern = Pattern.compile(patternStr);
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(targetStr);
        // 如果找到了
        if (matcher.find()) {
            // 打印出结果
            return matcher.group(1);
        }
        return "";
    }
    
    static ArrayList<String> RegexString_s(String targetStr, String patternStr) {
        // 预定义一个ArrayList来存储结果
        ArrayList<String> results = new ArrayList<String>();
        // 定义一个样式模板，此中使用正则表达式，括号中是要抓的内容
        Pattern pattern = Pattern.compile(patternStr);
        // 定义一个matcher用来做匹配
        Matcher matcher = pattern.matcher(targetStr);
        // 如果找到了
        boolean isFind = matcher.find();
        // 使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
        while (isFind) {
            //添加成功匹配的结果
            results.add(matcher.group(1));
            // 继续查找下一个匹配对象
            isFind = matcher.find();
        }
        return results;
    }
    
    /**
     * 列表页
     * 通过正则表达式获取内容
     * @param content
     * @param param
     * @return
     */
    static ArrayList<CompetitorItem> GetItems(String content,Map<String,String> param) {
        // 预定义一个ArrayList来存储结果
        ArrayList<CompetitorItem> results = new ArrayList<CompetitorItem>();
        String brand=param.get("brandName");
        String plat=param.get("platName");
        //减小匹配范围
        Pattern pattern = Pattern
                .compile("j_titems(.+?)pagination");
              Matcher matcher = pattern.matcher(content);
        if(matcher.find())
        	content= matcher.group(1);
        System.out.println(content);
        // 用来匹配url，也就是问题的链接
		//url
		Pattern pattern1 = Pattern
          .compile("class=photo.+?href=\"(.+?)\"");
        Matcher matcher1 = pattern1.matcher(content);
		//img
		Pattern pattern2 = Pattern
          .compile("class=photo.+?data-ks-lazyload=\"(.+?)\".+?</dt>");
        Matcher matcher2 = pattern2.matcher(content);
		//name
		Pattern pattern3 = Pattern
          .compile("class=photo.+?imgalt=(.+?)src");
        Matcher matcher3 = pattern3.matcher(content);
		//price
		Pattern pattern4 = Pattern
          .compile("class=detail.+?c-price>(.+?)</span>");
        Matcher matcher4 = pattern4.matcher(content);
		//totalVolume
		Pattern pattern5 = Pattern
          .compile("class=detail.+?sale-num>(.+?)</span>");
        Matcher matcher5 = pattern5.matcher(content);
        //rates
        Pattern pattern6 = Pattern
          .compile("class=rates.+?评价:(.+?)</span>");
        Matcher matcher6 = pattern6.matcher(content);
		
        // 是否存在匹配成功的对象
        Boolean isFind = matcher1.find();
        if(isFind)
        matcher1.group(1);
        Boolean b2 = matcher2.find();
        Boolean b3 = matcher3.find();
        Boolean b4 = matcher4.find();
        Boolean b5= matcher5.find();
        Boolean b6=matcher6.find();
        
        while (isFind) {
            // 定义一个知乎对象来存储抓取到的信息
        	/*System.out.println(matcher1.group(1));
        	System.out.println(matcher2.group(1));
        	System.out.println(matcher3.group(1));
        	System.out.println(matcher4.group(1));
        	System.out.println(matcher5.group(1));*/
        	Map<String,String> params=new HashMap();
        	params.put("itemUrl", matcher1.group(1));
        	params.put("imgUrl", b2?matcher2.group(1):"?");
        	params.put("itemName", b3?matcher3.group(1):"?");
        	params.put("itemPrice", b4?matcher4.group(1):"?");
        	params.put("totalVolume", b5?matcher5.group(1):"?");
        	params.put("rates", b6?matcher6.group(1):"?");
        	params.put("brandName", brand);
        	params.put("platName", plat);
            //CompetitorItem zhihuTemp = new CompetitorItem(matcher1.group(1),
            		//matcher2.group(1),matcher3.group(1),matcher4.group(1),matcher5.group(1),matcher6.group(1),brand);
        	System.out.println("搜索页+1");
            CompetitorItem zhihuTemp = new CompetitorItem(params);
        	//CompetitorItem zhihuTemp = new CompetitorItem(matcher1.group(1));
            // 添加成功匹配的结果
            results.add(zhihuTemp);
            // 继续查找下一个匹配对象
            isFind= matcher1.find();
            b2=matcher2.find();
            b3=matcher3.find();
            b4=matcher4.find();
            b5=matcher5.find();
            b6=matcher6.find();
        }
        return results;
    }
    
    /**
     * 清除回车等特殊字符
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
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
