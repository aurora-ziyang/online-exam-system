package cn.itbaizhan.tyut.exam.sys.servlets;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import cn.itbaizhan.tyut.exam.common.PageControl;
import cn.itbaizhan.tyut.exam.common.Pager;
import cn.itbaizhan.tyut.exam.common.Tools;
import cn.itbaizhan.tyut.exam.model.SysFunction;
import cn.itbaizhan.tyut.exam.sys.services.impl.FunService;
import cn.itbaizhan.tyut.exam.sys.services.interfaces.IFunService;

public class FunServlet extends HttpServlet {

	IFunService service = new FunService();
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String cmd = request.getParameter("cmd");
		if(cmd.equals("add")){
			addfun(request,response);
		}else if(cmd.equals("list")){
			funlist(request,response);
		}else if(cmd.equals("toedit")){
			toedit(request,response);
		}else if(cmd.equals("edit")){
			edit(request,response);
		}
	}

	//这是一个用于处理 HTTP 请求的 service 方法。它首先从 HTTP 请求中获取名为 "cmd" 的参数，然后根据参数的值执行不同的操作。具体来说：
//如果 "cmd" 参数的值是 "add"，则调用 addfun(request, response) 方法来处理添加操作。
//如果 "cmd" 参数的值是 "list"，则调用 funlist(request, response) 方法来处理列出操作。
//如果 "cmd" 参数的值是 "toedit"，则调用 toedit(request, response) 方法来处理准备编辑操作。
//如果 "cmd" 参数的值是 "edit"，则调用 edit(request, response) 方法来处理编辑操作。
//每个操作都会调用相应的方法来执行特定的逻辑，并向客户端返回相应的结果。
	
	
	
	
	
	/**
	 * 修改系统功能
	 * @param request
	 * @param response
	 */
	private void edit(HttpServletRequest request, HttpServletResponse response) {
		
		SysFunction fun = new SysFunction();
		
		try {
			BeanUtils.populate(fun, request.getParameterMap());
			Integer rtn = service.edit(fun);
			if(rtn>0){			
				response.sendRedirect(Tools.Basepath(request, response)+"sys/fun?cmd=list");
			}else{
				request.setAttribute("msg", "保存系统功能失败！");
				request.getRequestDispatcher("/sys/function/edit.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//这是一个用于编辑系统功能的Java Servlet方法。
//首先创建了一个新的 SysFunction 对象。
//使用 Apache Commons 的 BeanUtils.populate() 方法来填充 SysFunction 对象，填充的数据来自请求参数。
//调用某个服务对象的 edit() 方法（可能是用来保存编辑后的 SysFunction）。
//如果编辑操作成功（rtn > 0），就重定向用户到系统功能列表页面。
//如果编辑操作失败，就设置一个错误消息属性，然后将请求转发回编辑页面。
//如果在整个过程中发生异常，就简单地打印出异常的堆栈信息。
	//这个方法用于处理系统功能的编辑，很可能是通过网页表单提交的。它使用请求参数填充一个Java对象，然后通过一个服务保存它，并根据成功或失败进行相应的处理。


	/**
	 * 初始化修改页面
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void toedit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SysFunction fun = new SysFunction();
		fun.setFunid(Integer.parseInt(request.getParameter("id")));
		fun = service.detail(fun);
		if(fun!=null){
			request.setAttribute("item",fun);
			request.getRequestDispatcher("/sys/function/edit.jsp").forward(request, response);
		}else{
			request.setAttribute("msg", "需要修改的系统功能不存在。");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

	/**
	 * 查询功能列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void funlist(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String sname = request.getParameter("sname");
		SysFunction fun = new SysFunction();
		if(sname!=null && !sname.equals("")){
			fun.setFunname(sname);
		}
		
		PageControl pc = new PageControl();
		Integer currindex = 1;
		if(request.getParameter("index")!=null){
			currindex = Integer.parseInt(request.getParameter("index"));
		}
		pc.setCurrentindex(currindex);
		//pc.setPagesize(5);
		
		Pager<SysFunction> pager = service.list(fun, pc);
		request.setAttribute("pager", pager);
		request.getRequestDispatcher("/sys/function/list.jsp").forward(request, response);
	}
//这个方法用于根据搜索关键字和分页信息获取系统功能的列表，并将其显示在页面上供用户查看。



	/**
	 * 增加系统功能
	 * @param request
	 * @param response
	 */
	private void addfun(HttpServletRequest request, HttpServletResponse response) {
		
		SysFunction fun = new SysFunction();
		try {
			BeanUtils.populate(fun,request.getParameterMap());
			Integer rtn = service.addfun(fun);
			if(rtn>0){			
				response.sendRedirect(Tools.Basepath(request, response)+"sys/fun?cmd=list");
			}else{
				request.setAttribute("msg", "保存系统功能失败！");
				request.getRequestDispatcher("/sys/function/add.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}	
	}  //这个方法用于接收用户提交的系统功能信息，然后调用服务将其添加到系统中，处理成功或失败的情况，并根据结果进行相应的页面跳转。

	
	
}
