package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;


@WebServlet("/guest")
public class GuestbookController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("guestbook");
		String act = request.getParameter("action");
		
		if("add".equals(act)) {
			System.out.println("guest > add");
			
			String name = request.getParameter("name");
			String pass = request.getParameter("pass");
			String content = request.getParameter("content");
			
			GuestbookVo gvo = new GuestbookVo(name, pass, content);
			GuestbookDao gDao = new GuestbookDao();
			gDao.guestInsert(gvo);
			
			WebUtil.redirect(response, "/mysite/guest");
			
		} else if("deleteForm".equals(act)) {
			System.out.println("guest > deleteForm");
			
			WebUtil.forword(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			
		} else if("delete".equals(act)) {
			System.out.println("guest > delete");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String pass = request.getParameter("pass");
			
			GuestbookDao gDao = new GuestbookDao();
			gDao.guestDelete(no, pass);
			
			WebUtil.redirect(response, "/mysite/guest");
			
		} else {
			System.out.println("guest > addlist");
			
			GuestbookDao gDao = new GuestbookDao();
			List<GuestbookVo> gList = gDao.getList();
			
			request.setAttribute("gList", gList);
			
			WebUtil.forword(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
