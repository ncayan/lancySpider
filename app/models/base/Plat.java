package models.base;

import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import play.db.ebean.Model;

@Entity
@Table(name="plat")
public class Plat extends Model{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 545643838019929473L;
	
	@Id
	public Long id;
	public String platName;
	
	//列表页获取对应值的正则表达式
		//area 
	public	String areaRegular;
		//url 
	public	String urlRegular;
		//img
	public	String imgRegular;
		//name
	public	String nameRegular;
		//price
	public	String priceRegular;
		//totalVolume
	public	String volumeRegular;
		//rates
	public	String ratesRegular;
		
		//详情页获取元素所用类名
		//dropPrice
	public	String dropPriceClass;
		//monthVolume
	public	String monthVolumeClass;
		//rates
	public	String ratesClass;
		//AttrList;
	public	String attrListClass;
	
	public static Model.Finder<Long, Plat> find = new Model.Finder<Long, Plat>(
			Long.class, Plat.class);

    public static Page<Plat> page(int page, int pageSize, String sortBy,
			String order,Map<String,String> params) {
		
		ExpressionList<Plat> lst = find.where();
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
    
    public static Plat findByName(String name){
    	return find.where().eq("platName", name).findUnique();
    }

}
