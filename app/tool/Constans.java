package tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

/*import models.order.ConOrstatusMapping;
import models.order.OrderHead;
import models.permision.SysDict;
import models.store.BaseBrand;
import models.store.StoreInfo;*/

import play.Play;

public class Constans {

	public static final int CACHE_TIME = 1800;//缓存时间 30m
	public static final String DICT_PLATFORM= "1";//平台类型
	public static final String DICT_TEMPGROUP= "模板组";//模板组
	public static final String DICT_XIDI= "洗涤";//洗涤
	public static final String DICT_CLOTHING= "服装";//服装类型
	public static final String DICT_HAOXING= "号型";//号型
	public static final String SIZE_DATA_FLAG= "/";//号型
	public static final String SYS_DICT_BRAND = "品牌";
	public static final String SYS_DICT_PLAT_BRAND = "平台品牌";
	public static final String SYS_DICT_CAT = "类目";
	public static final String SYS_CONFIG_RFC = "RFC";
	public static final String ORDER_QUESTION_TYPE = "问题类型";
	public static final String ORDER_QUESTION_RESULT = "处理结果";
	public static final String ORDER_RTN_CHANGE_REASON = "退换货原因";
	public static final String ORDER_REJECT_REASON = "拒单原因";
	/** 订单字典 */
	public static final String ORDER_OMS_STATUS = "OMS订单状态";
	public static final String ORDER_PAY_WAY = "付款方式";
	public static final String ORDER_TYPE = "抬头-订单类型";
	public static final String ORDER_APPROVAL_STATUS = "审批状态";
	public static final String ORDER_PAY_STATUS = "付款状态";
	public static final String ORDER_PAY_OUT = "货到付款";
	public static final String ORDER_KAIPIAO_STATUS = "开票状态";
	public static final String ORDER_OFF_CLOSE_REASON = "线下关闭原因";
	public static final String ORDER_SHIPORDER_STATUS = "发货通知单状态";
	public static final String ORDER_ITEM_SHIP_STATUS = "项目-发货状态";
	
	public static final String DICT_DESC_MODULE= "描述模板";//描述模板类型
	public static final String STATUS_SUCCESS= "S";//成功
	public static final String STATUS_FAILTURE= "E";//失败
	public static final String STATUS_NULL= "N";//空，未开始
	public static final String STATUS_WAITING= "W";//进行中
	
	/** 产品商品规则相关 */
	public static final String TMALL_ITEM_URL = "http://detail.tmall.com/item.htm?id=";
	public static final String DUTY_FREE= "true";//提供发票
	public static final String SERVICE_VERSION= "11100";//天猫系统服务版本
	
	public static String CACHE_NAME_PREFIX;
	public static final String WAP_PREFIX = "_wap";//手机端图片后缀 localhost
	public static final String IMG_SERVER = "http://192.168.13.24:8080/servlet/ImgListServlet";//192.168.13.24
	public static final String IMG_ALL_SERVER = "http://192.168.13.24:8080/servlet/ImgListAllServlet";//localhost
	public static final String IMG_DEPT = "电商";
	public static final String PLAT_TMALL_ID = "PLAT_TMALL";
	public static final String PLAT_JD_ID = "PLAT_JD";

	public static final String DOWNLOADEXCEL_PATH = Play.application().path()+File.separator+"public"+ File.separator+"downLoadExcel";
	
	public static final String DOWNLOAD_PATH = Play.application().path()+File.separator+"public"+ File.separator+"download";
	public static final String UPLOAD_PATH = Play.application().path()+File.separator+"public"+ File.separator+"upload";
	public static final String UPLOAD_PATH_PRODUCT = Play.application().path()+File.separator+"public"+ File.separator+"upload"+File.separator+"product";
	
	public static String TMP_FILE_PATH = Play.application().path()+File.separator+"public"+ File.separator+"upload";
	public static String TMALL_URL;
	public static String TMALL_APPKEY;
	public static String TMALL_SECRETKEY;
	public static String TMALL_SESSIONKEY;
	public static Map<String,String> SIZE_BUWEI_MAP;
	public static String [] SIZE_BUWEI;
	
	public static final String  JD_PLAT="JD001";
	public static final String  DD_PLAT="DD001";
	public static final String  TM_PLAT="TM001";
	public static final String  VIP_PLAT="VIP01";
	
	public static final String LANCYUser="ecws_test";
	public static final String LANCYPassWord="ecws";
	public static final int THREAD_POOL_SIZE = 5;
	
	public static final String ConOfflineStockDefaultY="Y";
	public static final String ConOfflineStockDefaultN="N";
	
	//初始化尺寸试穿
/*	public static void initSizeBuWei(){
		List<SysDict> buweiList = SysDict.find.where().eq("dictType", "部位").findList();
		SIZE_BUWEI_MAP = new LinkedHashMap<String, String>();
		SIZE_BUWEI = new String[buweiList.size()];
		for(int i=0;i<buweiList.size();i++){
			SysDict dict = buweiList.get(i);
			String code = dict.dictCode;
			String name = dict.dictName;
			SIZE_BUWEI[i] = code;
			SIZE_BUWEI_MAP.put(code, name);
		}
	}*/
	
	/**
	 * OMS订单状态转平台订单状态
	 * @param shopId 线上店铺ID
	 * @param omsStatus OMS订单状态
	 * @return
	 */
	/*public static String getPlatOrderStatusByOmsStatus(String shopId,String omsStatus){
		StoreInfo store  = StoreInfo.findByID(shopId);
		String platId = store.platId;
		ConOrstatusMapping mappingL =  ConOrstatusMapping.find.where().eq("platId", platId).eq("omsOrderStatus", omsStatus).findUnique();
		if(mappingL==null)
			return null;
		return mappingL.platOrderStatus;
	}*/
	/**
	 * OMS订单状态转平台订单状态
	 * @param shopId 线上店铺ID
	 * @param omsStatus OMS订单状态
	 * @return
	 */
	/*public static List<String> getPlatOrderStatusListByOmsStatus(String omsStatus){
		
		List<ConOrstatusMapping> mappingL =  ConOrstatusMapping.find.where().eq("omsOrderStatus", omsStatus).findList();
		if(mappingL==null)
			return null;
		List<String> statusL = new ArrayList<String>();
		for(ConOrstatusMapping mapp:mappingL){
			statusL.add(mapp.platOrderStatus);
		}
		return statusL;
	}*/
	/**
	 * 平台订单状态转OMS订单状态
	 * @param shopId 线上店铺ID
	 * @param omsStatus OMS订单状态
	 * @return
	 */
	/*public static String getOmsStatusByPlatOrderStatus(String shopId,String platOrderStatus){
		StoreInfo store  = StoreInfo.findByID(shopId);
		String platId = store.platId;
		ConOrstatusMapping mappingL =  ConOrstatusMapping.find.where().eq("platId", platId).eq("platOrderStatus", platOrderStatus).findUnique();
		if(mappingL==null)
			return null;
		return mappingL.omsOrderStatus;
	}*/
	//获取OMS品牌
	/*public static List<SysDict> getOmsBrand(){
		List<SysDict> allBrand = SysDict.find.where().eq("dictType",SYS_DICT_BRAND).findList();
		return allBrand;
	}*/
	//获取平台品牌
	/*public static List<SysDict> getPLatBrand(){
		List<SysDict> allBrand = SysDict.find.where().eq("dictType",SYS_DICT_PLAT_BRAND).findList();
		return allBrand;
	}*/
	//获取平台类目ID 根据 OMS 类目ID
	/*public static String getPlatCid(String omsCid,String storeId){
		SqlQuery sqlq = Ebean.createSqlQuery(" select plat_cat_id from STORE_CATALOG_MAPPING where cat_id=:cid and store_id=:sid");
		sqlq.setParameter("cid", omsCid);
		sqlq.setParameter("sid", storeId);
		List<SqlRow> rows =  sqlq.findList();
		if(rows==null)
			return "";
		SqlRow row = rows.get(0);
		String pcid = row.getString("plat_cat_id");
		return pcid;
	}*/
	/**
	 * 根据平台ID,OMS id  获取平台映射ID
	 * @param omsBrandId
	 * @param platId
	 * @return
	 */
	/*public static String getPlatBrandId(String omsBrandId,String platId){
		BaseBrand allBrand = BaseBrand.find.where().eq("brandId",omsBrandId).eq("platId", platId).findUnique();
		return allBrand==null?"":allBrand.platBrandId;
	}
	public static String getOmsBrandName(String omsBrandId){
		SysDict allBrand = SysDict.find.where().eq("dictCode",omsBrandId).eq("dictType", Constans.SYS_DICT_BRAND).findUnique();
		return allBrand==null?"":allBrand.dictCode;
	}*/
	/**
	 * 根据OMS订单状态查询订单
	 * @param omsOrderStatus OMS订单状态
	 * @return
	 */
	/*public static List<OrderHead> getAssignedOrder(String omsOrderStatus){
		StringBuilder sql = new StringBuilder();

		sql.append(" select f.* from doc_order_head f  join con_orstatus_mapping g on f.order_status=g.plat_order_status ");
		sql.append(" and g.oms_order_status='A' ");
		return null;
	}*/
}
