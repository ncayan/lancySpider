package controllers.spider;

abstract class OMSSpider{
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
		
		public OMSSpider(){
			/*//area 
			areaRegular="<divid=\"content\">(.+?)</div>";
			//url 
			urlRegular="<aclass=\"link-boxhover-avil\"target=\"_blank\"href=\"(.+?)\">";
			//img
			imgRegular="<imgclass=\"item-pic\".+?src=\"(.+?)\".+?>";
			//name
			nameRegular="<h3class=\"nowrap\"title=\"(.+?)\">";
			//price
			priceRegular="<emclass=\"J_actPrice\"><spanclass=\"yen\">(.+?)</span><spanclass=\"cent\">(.+?)</span></em>";
			//totalVolume
			volumeRegular="<divclass=\"sold-num\"><em>(.+?)</em>";
			//rates->dropPrice
			ratesRegular="<delclass=\"orig-price\">¥(.+?)</del>";

			//详情页获取元素所用类名
			//dropPrice
			dropPriceClass="oprice,originPrice";
			//monthVolume
			monthVolumeClass="noclass,noclass";//聚划算不可用
			//rates
			ratesClass="J_TabEval,triangle";
			//AttrList
			attrListClass="noid,noclass";//聚划算不可用

			brandName="聚划算";//当前名称
			brandUrl="";//当前Url*/
		}
}
