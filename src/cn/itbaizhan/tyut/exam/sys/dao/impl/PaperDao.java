package cn.itbaizhan.tyut.exam.sys.dao.impl;

import java.util.List;

import cn.itbaizhan.tyut.exam.common.DBUnitHelper;
import cn.itbaizhan.tyut.exam.common.PageControl;
import cn.itbaizhan.tyut.exam.common.Pager;
import cn.itbaizhan.tyut.exam.model.Paper;
import cn.itbaizhan.tyut.exam.model.Subject;
import cn.itbaizhan.tyut.exam.sys.dao.interfaces.IPaperDao;

public class PaperDao implements IPaperDao {

	@Override
	public Integer addpaper(Paper paper) {  //向数据库中添加一份试卷信息
		// TODO Auto-generated method stub
		String sql = "INSERT INTO paper(pname,sid) SELECT ?,sid FROM " + 
			"subject where sstate = 1 ORDER BY rand() LIMIT ?";
		
		Integer rtn =DBUnitHelper.executeUpdate(sql,paper.getPname(),paper.getScount());
		
		return rtn;
	}
	
	public Pager<Paper> list(Paper paper,PageControl pc) {  //根据传入的试卷名称查询试卷信息，并返回一个分页对象。
		String sql = "SELECT pname,count(*) scount FROM paper GROUP BY pname" ;
//		String sql = "SELECT pid,pname,count(*) scount FROM paper GROUP BY pname" ;
		Pager<Paper> pager;
		String pid="pid";
		if(paper.getPname()!=null && !paper.getPname().equals("")){
			String sql2 = " where pname LIKE  ?  ";
			String sql3 = sql.substring(0,40) + sql2 + sql.substring(40);
			pager = DBUnitHelper.execlist(sql3, pc, Paper.class,pid, paper.getPname());
		}else{
			pager = DBUnitHelper.execlist(sql, pc, Paper.class,pid, null);	
		}
		return pager;
	}

	@Override
	public List<Subject> subjectlist(Paper paper) {  //根据试卷名称查询该试卷包含的所有题目信息，并返回一个题目对象的列表 
		// TODO Auto-generated method stub
		String sql = "SELECT subject.sid,scontent,sa,sb,sc,sd,skey FROM subject,paper WHERE paper.sid = subject.sid and paper.pname = ?";
		List<Subject> list = DBUnitHelper.executeQuery(sql,Subject.class, paper.getPname());
		return list;
	}

	@Override
	public List<Paper> list(Paper paper) {  //查询所有试卷的名称以及每个试卷包含的题目数量，并返回一个试卷对象的列表
		// TODO Auto-generated method stub
		String sql = "SELECT pname,count(*) scount FROM paper GROUP BY pname" ;
		List<Paper> list = DBUnitHelper.executeQuery(sql,Paper.class);
		return list;
	}
	@Override
	public int delete(Paper paper){   //根据试卷名称删除数据库中的试卷记录
		String sql ="DELETE  FROM paper WHERE pname=?";
		return DBUnitHelper.executeUpdate(sql, paper.getPname());

	}
}
