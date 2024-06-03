package cn.itbaizhan.tyut.exam.sys.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itbaizhan.tyut.exam.common.PageControl;
import cn.itbaizhan.tyut.exam.common.Pager;
import cn.itbaizhan.tyut.exam.common.Tools;
import cn.itbaizhan.tyut.exam.model.Paper;
import cn.itbaizhan.tyut.exam.model.Subject;
import cn.itbaizhan.tyut.exam.sys.services.impl.PaperService;
import cn.itbaizhan.tyut.exam.sys.services.interfaces.IPaperService;

public class PaperServlet extends HttpServlet {
	
	//在类的成员变量中声明了一个 IPaperService 接口类型的对象 service，并将其实例化为 PaperService 的对象。这个对象用于处理与试卷相关的业务逻辑。
	//service() 方法是 HttpServlet 类的重写方法，它处理所有的请求，并根据请求参数中的命令来调用相应的方法。根据传入的 cmd 参数，它会调用不同的私有方法来执行相应的操作。
	//addsubject() 方法用于处理添加试题的请求。它通过 BeanUtils.populate() 方法将请求参数填充到 Paper 对象中，然后调用服务的 addpaper() 方法将试题添加到系统中。
	//list() 方法用于查询试题列表。它从请求参数中获取试题名称关键字，并根据关键字查询试题列表，然后将查询结果存入请求属性中，最后转发到试题列表页面进行展示。
	//slist() 方法用于查询某张试卷包含的试题列表。它从请求参数中获取试卷名称，然后调用服务的 subjectlist() 方法查询与该试卷相关的试题列表，并将查询结果存入请求属性中，最后转发到试题列表页面进行展示。
	
	
	
	IPaperService service = new PaperService();
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	
		String cmd = request.getParameter("cmd");
		if(cmd.equals("add")){
			addsubject(request,response);
		}else if(cmd.equals("list")){
			list(request,response);
		}else if(cmd.equals("slist")){
			slist(request,response);
		}else if(cmd.equals("delete")){
			String pname = new String(request.getParameter("pname"));
			Paper paperDelete = new Paper();
			paperDelete.setPname(pname);
		service.delete(paperDelete);
			response.sendRedirect(Tools.Basepath(request, response)+"sys/paper?cmd=list");

		}
	}	
	
	/**
	 * 生成试题功能
	 * @param request
	 * @param response
	 */
	private void addsubject(HttpServletRequest request, HttpServletResponse response) {
		
		Paper paper = new Paper();
		try {
			BeanUtils.populate(paper,request.getParameterMap());
			Integer rtn = service.addpaper(paper) ;
			if(rtn>0){			
				response.sendRedirect(Tools.Basepath(request, response)+"sys/paper?cmd=list");
			}else{
				request.setAttribute("msg", "增加试题功能失败！");
				request.getRequestDispatcher("/sys/paper/add.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 查询试题列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void list(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pname = request.getParameter("pname");
		Paper paper = new Paper();
		if(pname!=null && !pname.equals("")){
			paper.setPname("%"+pname+"%");
//			paper.setPname(pname);
		}
		PageControl pc = new PageControl();
		Integer currindex = 1;
		if(request.getParameter("index")!=null){
			currindex = Integer.parseInt(request.getParameter("index"));
		}
		pc.setCurrentindex(currindex);
		//pc.setPagesize(5);
		Pager<Paper> pager = service.list(paper, pc);
//		System.out.println(pager.getList());;
		request.setAttribute("pager", pager);
		request.getRequestDispatcher("/sys/paper/list.jsp").forward(request, response);
	}
	private void slist(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Paper paper = new Paper();
//		String pname = new String(request.getParameter("pname").getBytes("iso-8859-1"), "utf-8");
		String pname = new String(request.getParameter("pname"));

		paper.setPname(pname);
		List<Subject> subjects = service.subjectlist(paper);
		request.setAttribute("subjects", subjects);
		request.getRequestDispatcher("/sys/paper/subjects.jsp").forward(request, response);
	}
}
