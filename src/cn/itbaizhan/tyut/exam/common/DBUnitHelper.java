package cn.itbaizhan.tyut.exam.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class DBUnitHelper {

	/**
	 * 获取数据库链接
	 * @return
	 */
	public static Connection getConn(){
		Connection conn = null;	
		try {
			
			DbUtils.loadDriver("com.mysql.cj.jdbc.Driver"); //load MySql JDBC driver
			conn = DriverManager.getConnection(  //DriverManager.getConnection method is to establish a connection to the database
					"jdbc:mysql://localhost:3306/exam?characterEncoding=utf-8&serverTimezone=GMT%2B8", "root", "123456");
			
		} catch (SQLException e) {
			e.printStackTrace();  //When catching the exception, the stack trace is printed 
		}
		return conn;
	}
	
	public static Integer executeUpdate(String sql,Object ...objects){
		
		Connection conn = getConn();  //establish connection
		QueryRunner qr = new QueryRunner();  //create an instance of Apache
		Integer rtn = 0;
		try {
			if(objects == null){
				rtn = qr.update(conn, sql);  // no projects are provided
			}else{
				rtn = qr.update(conn, sql, objects);  // ...are provided
			}     
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				DbUtils.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	//the finally ensures the database connection is closed	
		return rtn;
	}
	
	public static Integer executeUpdate(String sql){     // do not understand!!!
		return executeUpdate(sql, null);
	}     
	
	public static <T> List<T> executeQuery(String sql,Class<T> cls,Object ...objects){
		Connection conn = getConn();
		List<T> list = null;
		try{
			QueryRunner rq = new QueryRunner();
			if(objects == null){
				list = rq.query(conn, sql,new BeanListHandler<T>(cls)); 
			}else{
				list = rq.query(conn, sql,new BeanListHandler<T>(cls),objects); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				DbUtils.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;       //this methon is designed to execute a SQL query and return the resultsa a list
	}
	
	public static <T> List<T> executeQuery(String sql,Class<T> cls){
		return executeQuery(sql,cls,null);
	}
	
	/**
	 * 带分页的查询
	 * @param sql SQL语句
	 * @param map SQL参数
	 * @param pc 分页控制对象，需要传递参数：当前第几页（currentindex）,每页显示多少行：(pagesize)
	 * 分页控件显示多少也：showpcount
	 * @return
	 */
	public static <T> Pager<T> execlist(String sql,PageControl pc,Class cls,String pk,Object...object){

		//获取总记录数sql		 
		String sqlcount = "select count(*) as count from ("+sql+") a";
		//获取具体数据的SQL语句
		Integer min = (pc.getCurrentindex()-1)*pc.getPagesize();   //getCurrentindex 获取当前页的索引
		                                                           //getPagesize 获取每页最大
		Integer max = pc.getPagesize();
		String sqllist = "select * from ("+sql+") a  limit "+min+","+max;
//		String sqllist = "select * from ("+sql+") a where a."+pk+" limit "+min+","+max;
		
		Connection conn = getConn();
		Pager<T> pager = new Pager<T>();
		try {
			
			QueryRunner rq = new QueryRunner();
			Object count = rq.query(conn, sqlcount, new ScalarHandler<Object>("count"), object);
			List<T> list = executeQuery(sqllist,cls,object);
			//设置总记录数
			Integer c = 0;
			if(count!=null){
				c=Integer.parseInt(count.toString());
			}
			pc.setRscount(c);
			
			pager.setList(list);
			pc = dealpage(pc);
			pager.setPagectrl(pc);	
			DbUtils.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}					    
		return pager;        //The method retrieves a paginated list of results from a database using Apache Commons DbUtils. 
	}
	/**
	 * 处理分页参数
	 * @param pc
	 * @return
	 */
	private static PageControl dealpage(PageControl pc){
		//获取总页数
		Integer pagecount = pc.getRscount()/pc.getPagesize();
		if(pc.getRscount()%pc.getPagesize()>0){
			pagecount++;
		}
		pc.setPagecount(pagecount);
		
		//计算最大(最小)显示页数
		Integer showpcount = pc.getShowpcount();//分页一次显示多少页
		Integer maxpage = 0;//当前显示最大页码
		Integer minpage = 0;
		Integer index = pc.getCurrentindex();//当前第几页
		if(pagecount<=showpcount){//当总页数小于等于显示的页数时
			maxpage = pagecount;
			minpage = 1;
		}else{
			Integer buff = showpcount/2; //取中间数。maxpage=index+buff
			buff = index+buff;
			if(buff<=showpcount){
				maxpage = showpcount;
				minpage = 1;
			}else if(buff<pagecount){
				maxpage = buff;
				minpage = maxpage - showpcount + 1;
				
			}else if(buff>=pagecount){
				maxpage = pagecount;
				minpage = maxpage - showpcount + 1;
			}
		}
		pc.setMaxpage(maxpage);	
		pc.setMinpage(minpage);
		return pc;
	}
	/*public static void main(String args[]) throws SQLException
	{
		Connection con=getConn();
		for(int i=0;i<=700;i++){
		String sql="insert into r values(?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, i);
		pstmt.executeUpdate();
		System.out.println("插入成功"+i);
		}
	}*/
}
