package tool;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

//import models.stock.StcStockItemVO;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
//import org.jdom.Element;
//import org.jdom.JDOMException;
//import org.jdom.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import play.libs.Json;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	/**
	 * JSON 分页
	 * @param page
	 * @return
	 */
	public static <T> String getPageJson(Page<T> page){
		JSONArray arr = new JSONArray();
		List<T> lst =  page.getList();
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(T demo:lst){
			ObjectMapper mapper = new ObjectMapper();
		    mapper.setDateFormat(outputFormat);
		    Json.setObjectMapper(mapper);   
		    JsonNode demoJson = Json.toJson(demo);
		    Iterator<Entry<String,JsonNode>> nodes = demoJson.fields();
		    JSONObject jsonObj = new JSONObject();
		    while(nodes.hasNext()){
		    	Entry<String,JsonNode> one = nodes.next();
		    	String k = one.getKey();
		    	String v = one.getValue().asText();
		    	try {
					jsonObj.put(k, v.equals("null")?"":v);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
			arr.put(jsonObj);
		}
		int total = page.getTotalRowCount();
		JSONObject obj = new JSONObject();
		try{
		obj.put("total",total+"");
		obj.put("rows",arr);
		}catch (Exception e) {
		}
		return obj.toString();
	}
	/**
	 * JDBC JSON 分页
	 * @param page
	 * @return
	 */
	public static <T> String getJdbcPageJson(PageJdbc<T> page){
		JSONObject obj = new JSONObject();
		if(null==page||page.getList()==null){
			try {
				obj.put("total","0");
				obj.put("rows","");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return obj.toString();
		}
		JSONArray arr = new JSONArray();
		List<T> lst =  page.getList();
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(T demo:lst){
			ObjectMapper mapper = new ObjectMapper();
		    mapper.setDateFormat(outputFormat);
		    Json.setObjectMapper(mapper);   
		    JsonNode demoJson = Json.toJson(demo);
		    Iterator<Entry<String,JsonNode>> nodes = demoJson.fields();
		    JSONObject jsonObj = new JSONObject();
		    while(nodes.hasNext()){
		    	Entry<String,JsonNode> one = nodes.next();
		    	String k = one.getKey();
		    	String v = one.getValue().asText();
		    	try {
					jsonObj.put(k, v.equals("null")?"":v);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		    }
			arr.put(jsonObj);
		}
		int total = page.getTotalRowCount();
		
		try{
		obj.put("total",total+"");
		obj.put("rows",arr);
		}catch (Exception e) {
		}
		return obj.toString();
	}
	public static String jsosRtnMsg(boolean success,String msg){
		JSONObject rtn = new JSONObject();
        try{
	        rtn.put("success", success);
	        rtn.put("msg", msg);
        }catch (Exception e) {
        	e.printStackTrace();
		}
        return rtn.toString();
	}
	/**
	 * 获取JSON查询参数，[{"field":"seqId","op":"contains","value":"8"}]
	 * @param filterRules
	 * @return
	 */
	public static Map<String,String> getRequestParams(String filterRules){
		Map<String,String> params = new HashMap<String, String>();
		
		try{
			JSONArray obj = new JSONArray(filterRules);
			for(int i=0;i<obj.length();i++){
				
					JSONObject json = obj.optJSONObject(i);
					String key = json.getString("field");
					String v = json.getString("value");
					if(StringUtils.isNotEmpty(v))
						params.put(key, v);
					
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	/** 
     * 对象属性转换为字段  例如：userName to user_name 
     * @param property 字段名 
     * @return 
     */  
    public static String propertyToField(String property) {  
        if (null == property) {  
            return "";  
        }  
        char[] chars = property.toCharArray();  
        StringBuffer sb = new StringBuffer();  
        for (char c : chars) {  
            if (CharUtils.isAsciiAlphaUpper(c)) {  
                sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));  
            } else {  
                sb.append(c);  
            }  
        }  
        return sb.toString();  
    }  
   
    /** 
     * 字段转换成对象属性 例如：user_name to userName 
     * @param field 
     * @return 
     */  
    public static String fieldToProperty(String field) {  
        if (null == field) {  
            return "";  
        }  
        char[] chars = field.toCharArray();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < chars.length; i++) {  
            char c = chars[i];  
            if (c == '_') {  
                int j = i + 1;  
                if (j < chars.length) {  
                    sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));  
                    i++;  
                }  
            } else {  
                sb.append(c);  
            }  
        }  
        return sb.toString();  
    }  
    //首字母大写
    public static String firstCharUpperCase(String field) {  
        if (null == field) {  
            return "";  
        }  
        char[] chars = field.toCharArray();  
        String first = CharUtils.toString(chars[0]);
        String upcase = StringUtils.upperCase(first);  
        field = field.replaceFirst(first, upcase);
        return field;  
    } 
    /**
     * 解析XML文件
     * @param xmlDoc
     * @return
     */
    /*public static List<Map<String, String>> xmlElements(String xmlDoc) {
		// 创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		List<Map<String, String>> list = null;
		try {
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();
			list = getMap(root);
		} catch (JDOMException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return list;
	}
    /**
     * 把XML节点封装成MAP
     * @param et
     * @return
     */
	/*private static List<Map<String, String>> getMap(Element et) {
		List<Map<String, String>> list = new ArrayList<>();
		// 得到根元素所有子元素的集合
		List<Object> jiedian = et.getChildren();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < jiedian.size(); i++) {
			Element element = (Element) jiedian.get(i);
			if (element.getChildren().size() != 0) {
				list.addAll(getMap(element));
			} else {
				map.put(element.getName(), element.getValue());
				if (i == jiedian.size() - 1) {
					list.add(map);
				}
			}
		}
		return list;
	}*/
	 /**
     * 解析XML文件
     * @param xmlDoc
     * @return
     */
   /* public static Map<String,List<Map<String,String>>> xmlElementsForName(String xmlDoc) {
		// 创建一个新的字符串
		StringReader read = new StringReader(xmlDoc);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		Map<String,List<Map<String,String>>> nameNodes = new HashMap<String, List<Map<String,String>>>();
		try {
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();
			
			getMapForName(root,nameNodes);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nameNodes;
	}*/
    /**
     * 把XML节点封装成MAP
     * @param et
     * @return
     */
	/*private static void getMapForName(Element et,Map<String,List<Map<String,String>>> nameNodes) {
		// 得到根元素所有子元素的集合
		List<Map<String,String>> listItem = new ArrayList<Map<String,String>>();
		List<Map<String,String>> listHead = new ArrayList<Map<String,String>>();
		List<Object> jiedian = et.getChildren();
		Map<String, String> map = new HashMap<>();
		for (int i = 0; i < jiedian.size(); i++) {
			Element element = (Element) jiedian.get(i);
			String nodeName = element.getName();
			if (element.getChildren().size() != 0) {
				if(nodeName.equals("ProductList")){
					for (int k = 0; k < element.getChildren().size(); k++) {
						Map<String, String> map2 = new HashMap<>();
						Element itemList = (Element) element.getChildren().get(k);
						for (int j = 0; j < itemList.getChildren().size(); j++) {
							Element element3 = (Element) itemList.getChildren().get(j);
							map2.put(element3.getName(), element3.getValue());
						}
						listItem.add(map2);
					}
				}
			} else {
				map.put(element.getName(), element.getValue());
				if (i == jiedian.size() - 1) 
					listHead.add(map);
			}
		}
		nameNodes.put("head", listHead);
		nameNodes.put("item", listItem);
	}*/
	public static  String getUUID(){
		return StringUtils.upperCase(UUID.randomUUID().toString().replace("-", ""));
	}
	//计算一个SKU的可用库存
	public static Long calUsableStockNum(Long total,Long used,Long frozen){
		 total = total==null?0:total;
		 used = used==null?0:used;
		 frozen = frozen==null?0:frozen;
		 
		 Long usable = total-used-frozen;
		 return usable;
	}
	//计算一个SKU的线下店铺或总仓可用库存
	/*public static Map<String,StcStockItemVO> calUsableStockNum2(Collection<StcStockItemVO> stocks){
		 Iterator<StcStockItemVO> iter =  stocks.iterator();
		 Map<String,StcStockItemVO> oneItemOfAllStoreNum = new LinkedHashMap<String, StcStockItemVO>();
		 while(iter.hasNext()){
			 StcStockItemVO vo = iter.next();
			 oneItemOfAllStoreNum.put(vo.OfflineStoreId, vo);
		 }
		 return oneItemOfAllStoreNum;
	}*/
	//10位数字流水号
	public static String getNumberSN(String s){	
		String rs=s;	
		for (int j = rs.length(); j <10; j++) {
			rs="0"+rs;		
		}           
		return rs;  	
	}
	//2位数字流水号
	public static String getNextTwoSN(String s){
		String rs=s;
		rs = Integer.toString((Integer.parseInt(rs)+1));
		for (int j = rs.length(); j <2; j++) {
			rs="0"+rs;		
		}           
		return rs;  	
	}
	//4位数字流水号
	public static String getNext4SN(String s){
		String rs=s;
		rs = Integer.toString((Integer.parseInt(rs)+1));
		for (int j = rs.length(); j <4; j++) {
			rs="0"+rs;		
		}           
		return rs;  	
	}
	public static String makeList2SqlInStr(List<String> in){
		if(in==null)
			return null;
		String inStr = "(";
		for(String s:in){
			inStr += "'"+s+"',";
		}
		inStr = inStr.substring(0,inStr.length()-1);
		inStr += ")";
		return inStr;
	}
	public static String makeList2SqlInLong(List<Long> in){
		if(in==null)
			return null;
		String inStr = "(";
		for(Long s:in){
			inStr += s+",";
		}
		inStr = inStr.substring(0,inStr.length()-1);
		inStr += ")";
		return inStr;
	}
	/** 
     * 生成订单编号 
     * @return 
     */  
	private static String lastDate;
	private static AtomicLong orderNo;
    public static String getOrderNo(Date now){
        String nowDate = new SimpleDateFormat("yyMMddHHmm").format(now);  
        if(lastDate==null||!lastDate.equals(nowDate)){
        	lastDate = nowDate;
        	orderNo  = new AtomicLong(0);
        }else
        	orderNo.incrementAndGet();
        return lastDate+getNext4SN(orderNo.get()+"");  
    }
    public static String getPrefixOrderNo(String prefix,Date now){
        String nowDate = new SimpleDateFormat("yyMMddHHmm").format(now);  
        if(lastDate==null||!lastDate.equals(nowDate)){
        	lastDate = nowDate;
        	orderNo  = new AtomicLong(0);
        }else
        	orderNo.incrementAndGet();
        return prefix+lastDate+getNext4SN(orderNo.get()+"");  
    }  
	 public static void main(String[] args) {
		 //System.out.println(getNext4SN("100"));
		 for(int i=0;i<10000;i++){
			 try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 getOrderNo(new Date());
		 }
	}
}
