package tool;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import play.Logger;

//import models.vo.ColorInfoVO;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSql.ColumnMapping;
import com.avaje.ebean.RawSql.Sql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

/**
 * JDBC 原生SQL 分页
 * @author jeff dean
 *
 * @param <T>
 */
public class PageJdbc<T> {

	private int pageIndex = 0;// page
	private int totalPages = 0;// 总页数
	private int currentPage = 0;// 当前页
	private int pageSize = 0;// 每页显示条数
	private int totalRows = 0;// 总条数
	private int pageStartRow = 0;// 开始条数
	private int pageEndRow = 0;// 结束条数
	private boolean hasNextPage = false;// 是否存在下一页
	private boolean hasPreviousPage = false;// 是否存在上一页
	private int nextPage = 0;// 下一页的页号
	private int perviousPage = 0;// 上一页的页号
	private Map<String,String> params;
	private List<T> list;

	public PageJdbc() {

	}

	public int getTotalRowCount() {
		return this.totalRows;
	}

	public PageJdbc(int page, int pageSize,Map<String,String> params) {
		this.pageIndex = page;
		this.pageSize = pageSize;
		this.currentPage = page;
		this.params = params;
	}
	@SuppressWarnings(value={"unchecked","rawtypes"})
	public PageJdbc<T> getPage(Query<T> query) {
		RawSql rawSql = query.getRawSql();
		
		ColumnMapping mapping = rawSql.getColumnMapping();
		Map<String, String> map = mapping.getMapping();
		Collection<String> values = map.values();
		String columns = "";
		for (String v : values) {
			columns += v + ",";
		}
		columns = columns.substring(0, columns.length() - 1);
		Sql sql = rawSql.getSql();
		String select = "select " + sql.getPreFrom();
		String sqlStr = select + " " + sql.getPreWhere();
		
		String countSql = "select count(*) count from (" + sqlStr + ")";
		Logger.info("jdbc sql="+countSql);
		SqlQuery sqlQ = Ebean.createSqlQuery(countSql);
		Set<Entry<String,String>> entrySet = null;
		if(params!=null){
			entrySet = params.entrySet();
			for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				sqlQ.setParameter(entry.getKey(),entry.getValue());
			}
		}
		
		List<SqlRow> row = sqlQ.findList();
		int count = row.get(0).getInteger("count");
		this.totalRows = count;
		if(count==0)
			return this;
		configure();
		String pageSql = "select " + columns + " from (select /*+ FIRST_ROWS("
				+ pageStartRow + ") */ rownum rn_, a.* from (" + sqlStr
				+ ")  a  where rownum <= " + pageEndRow + " ) where rn_ >="
				+ pageStartRow;
		RawSqlBuilder rawSqlB = RawSqlBuilder.parse(pageSql);

		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String> entry = (Entry<String, String>) iterator
					.next();
			rawSqlB.columnMapping(entry.getValue(), entry.getKey());
		}
		RawSql rawSqlObj = rawSqlB.create();
		query = query.setRawSql(rawSqlObj);
		if(null!=entrySet){
			for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
				Entry<String, String> entry = (Entry<String, String>) iterator
						.next();
				query.setParameter(entry.getKey(),entry.getValue());
			}
		}
		
		List<T> rowList = query.findList();
		this.list = rowList;

		return this;
	}

	public void configure() {
		++currentPage;
		/*
		 * 计算并设置总页数： 若总记录数是每页记录数的整数倍，则 总页数 = 总记录数 /每页记录数； 若总记录数不是每页记录数的整数位，则 总页数
		 * = 总记录数/每页记录数 +1;
		 */
		int intTotalPage = this.totalRows % this.pageSize == 0 ? this.totalRows
				/ this.pageSize : this.totalRows / this.pageSize + 1;
		this.totalPages = intTotalPage;

		/*
		 * 计算并设置当前页面的起始记录位置
		 */
		if (this.currentPage <= intTotalPage) { // 若当前页码不大于总页数
			this.pageStartRow = (this.currentPage - 1) * this.pageSize + 1; // 当前页面的起始记录位置=
																			// (当前页码数-1)*每页记录数
		}

		/*
		 * 计算并设置当前页面的结束记录位置
		 */
		int intPageEndRow = this.pageStartRow + this.pageSize - 1; // 下一页的起始记录位置
		this.pageEndRow = intPageEndRow >= this.totalRows ? this.totalRows
				: intPageEndRow;

		if (this.currentPage <= 1)// 当前页码小于等于1
			this.hasPreviousPage = false; // 无上一页
		else { // 当前页的页码大于1
			this.hasPreviousPage = true; // 存在上一页
			this.perviousPage = this.currentPage - 1; // 设置上一页页码
		}

		if (this.currentPage >= intTotalPage)// 当前页大于等于总页数
			this.hasNextPage = false; // 无下一页
		else {
			this.hasNextPage = true; // 存在下一页
			this.nextPage = this.currentPage + 1; // 设置下一页页码
		}

	}

	// 2,9 11-19
	public String getDisplayXtoYofZ(String to, String of) {

		return pageStartRow + to + pageEndRow + " " + of + totalRows;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public boolean hasNext() {
		return hasNextPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public boolean hasPrev() {
		return hasPreviousPage;
	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/*public static void main(String[] args) {
		PageJdbc<ColorInfoVO> page = new PageJdbc<ColorInfoVO>(3, 10,null);
		page.totalRows = 39;
		page.configure();
		String s = page.getDisplayXtoYofZ("-", "总共");
		System.out.println(s);
		System.out.println("hasNext=" + page.hasNextPage);
		System.out.println("haspre=" + page.hasPreviousPage);
	}*/

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}
