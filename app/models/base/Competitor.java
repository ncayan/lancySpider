package models.base;

import java.util.List;
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
@Table(name = "COMPETITOR")
public class Competitor extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	public String brandName;//品牌名称
    public String startUrl;// 起始Url
    public String platName;//平台名称
    
    public static Model.Finder<Long, Competitor> find = new Model.Finder<Long, Competitor>(
			Long.class, Competitor.class);

    public static Page<Competitor> page(int page, int pageSize, String sortBy,
			String order,Map<String,String> params) {
		
		ExpressionList<Competitor> lst = find.where();
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
    
    public static Competitor findById(String cid){
    	return find.where().eq("id", Long.parseLong(cid))
				.findUnique();
    }
    
    public static List<Competitor> findAll(){
    	ExpressionList<Competitor> lst = find.where();
    	return lst.findList();
    }
    
    
}
