package cn.itbaizhan.tyut.exam.sys.dao.impl;

import java.util.List;

import cn.itbaizhan.tyut.exam.common.DBUnitHelper;
import cn.itbaizhan.tyut.exam.common.PageControl;
import cn.itbaizhan.tyut.exam.common.Pager;
import cn.itbaizhan.tyut.exam.model.SysFunction;
import cn.itbaizhan.tyut.exam.model.Sysrole;
import cn.itbaizhan.tyut.exam.model.Sysuser;
import cn.itbaizhan.tyut.exam.sys.dao.interfaces.IUserDao;

public class UserDao implements IUserDao {

	public Sysuser login(Sysuser user) {  //这个方法用于用户登录验证，接收一个Sysuser对象作为参数，其中包含了用户输入的用户名和密码

		String sql = "SELECT USERID,A.ROLEID,USERNAME,USERPWD,USERTRUENAME,USERSTATE, "
				+ "B.ROLENAME FROM SYSUSER A "
				+ "INNER JOIN SYSROLE B ON A.ROLEID=B.ROLEID "
				+ "WHERE USERSTATE=1 AND USERNAME=? AND USERPWD=? ";
		List<Sysuser> list = DBUnitHelper.executeQuery(sql, Sysuser.class, user
				.getUsername(), user.getUserpwd());
		if (list.size() > 0) {
			user = list.get(0);
		} else {
			user = null;
		}
		return user;
	}

	public List<SysFunction> initpage(Sysuser user) {

		List<SysFunction> list = null;

		if (user.getRoleid().equals(-1)) {
			String sql = "SELECT A.FUNID,A.FUNNAME,A.FUNURL,A.FUNPID FROM SYSFUNCTION A WHERE A.FUNSTATE=1";
			list = DBUnitHelper.executeQuery(sql, SysFunction.class);
		} else {
			String sql = "SELECT A.FUNID,A.FUNNAME,A.FUNURL,A.FUNPID FROM SYSFUNCTION A "
					+ "INNER JOIN ROLERIGHT B ON A.FUNID=B.FUNID WHERE B.ROLEID=? AND A.FUNSTATE=1";
			list = DBUnitHelper.executeQuery(sql, SysFunction.class, user
					.getRoleid());
		}
		return list;
	}  //这个方法用于初始化用户界面的功能列表，接收一个 Sysuser 对象作为参数，其中包含了当前用户的角色信息。
       //如果用户的角色是超级管理员（roleid 为 -1），则查询所有状态为启用的功能；否则，查询当前角色拥有权限的功能。根据查询结果返回相应的功能列表。

	public Pager<Sysuser> list(Sysuser user, PageControl pc) {
		
		String sql = "SELECT USERID,ROLEID,USERNAME,USERPWD,USERTRUENAME,USERSTATE FROM " +
				" SYSUSER WHERE USERID>0 ";
		Pager<Sysuser> pager = null;
		String userid="userid";
		if(user.getUsername()!=null && !user.getUsername().equals(""))
		{
			sql += " AND USERNAME like ?";
			pager = DBUnitHelper.execlist(sql, pc, Sysuser.class,userid, user.getUsername());
		}else{
			pager = DBUnitHelper.execlist(sql, pc, Sysuser.class,userid, null);
		}
		return pager;
	}  //这个方法用于分页查询用户信息，接收一个 Sysuser 对象作为参数，其中包含了查询条件。如果查询条件中包含用户名，则按用户名模糊查询；否则，查询所有用户信息。
	
	public Integer add(Sysuser user) {
		
		String sql = "INSERT INTO SYSUSER(ROLEID,USERNAME,USERPWD,USERTRUENAME,USERSTATE) " +
				"VALUES (?,?,?,?,?)";
		return DBUnitHelper.executeUpdate(sql, user.getRoleid(),user.getUsername(),user.getUserpwd(),user.getUsertruename(),user.getUserstate());
	}   //这个方法用于向数据库中添加用户信息。接收一个 Sysuser 对象作为参数，其中包含了要添加的用户信息。通过 SQL 语句将用户信息插入到 SYSUSER 表中，并返回操作影响的行数。

	public Sysuser detail(Sysuser user) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM " +
		" SYSUSER WHERE USERID=? ";
		List<Sysuser> list = DBUnitHelper.executeQuery(sql, Sysuser.class, user.getUserid());
		return list.get(0);
	}  //这个方法用于获取指定用户的详细信息。

	public Integer edit(Sysuser user) {
		// TODO Auto-generated method stub
		String sql = "UPDATE SYSUSER SET ROLEID=?,USERNAME=?," +
		"USERPWD=?,USERTRUENAME=?,USERSTATE=? WHERE USERID=?";
		Integer rtn = DBUnitHelper.executeUpdate(sql,user.getRoleid(),
		user.getUsername(),user.getUserpwd(),user.getUsertruename(),user.getUserstate(),user.getUserid());
		return rtn;
	}  //这个方法用于编辑用户信息。它接收一个 Sysuser 对象作为参数，其中包含了要更新的用户信息。
	   //通过执行 SQL 更新语句，将数据库中对应用户的信息更新为传入的新信息。最后返回执行更新操作所影响的行数。

	public Integer toedit(Sysuser user) {
		// TODO Auto-generated method stub
		String sql = "UPDATE SYSUSER SET USERPWD=? WHERE USERID=?";
		Integer rtn = DBUnitHelper.executeUpdate(sql,user.getUserpwd(),user.getUserid());
		return rtn;
	}  //这个方法用于更新用户密码。
	public Integer editpwd(Sysuser user) {  //这个方法用于编辑用户密码。
		// TODO Auto-generated method stub
		String sql = "UPDATE SYSUSER SET USERPWD=? WHERE USERID=?";
		Integer rtn = DBUnitHelper.executeUpdate(sql,user.getUserpwd(),user.getUserid());
		return rtn;
	}

	public Integer toeditpwd(Sysuser user) {
		// TODO Auto-generated method stub
		String sql = "UPDATE SYSUSER SET USERPWD=? WHERE USERID=?";
		Integer rtn = DBUnitHelper.executeUpdate(sql,user.getUserpwd(),user.getUserid());
		return rtn;
	}  //?????
	
	public Sysuser stulogin(Sysuser user) {
		String sql = "SELECT USERID,A.ROLEID,USERNAME,USERPWD,USERTRUENAME,USERSTATE, "
				+ "B.ROLENAME FROM SYSUSER A "
				+ "INNER JOIN SYSROLE B ON A.ROLEID=B.ROLEID "
				+ "WHERE USERSTATE=1 AND USERNAME=? AND USERPWD=? ";
		List<Sysuser> list = DBUnitHelper.executeQuery(sql, Sysuser.class, user
				.getUsername(), user.getUserpwd());
		if (list.size() > 0) {
			user = list.get(0);
		} else {
			user = null;
		}
		return user;
	}  //这个方法用于学生登录。它接受一个 Sysuser 对象作为参数，其中包含学生输入的用户名和密码。然后，该方法执行一个 SQL 查询，根据提供的用户名和密码从数据库中检索用户信息。
	   //如果找到匹配的凭据的用户，则将其信息作为 Sysuser 对象返回。否则，返回 null。
}
