package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/user");
		
		String act = request.getParameter("action");
		
		if("joinForm".equals(act)) {
			System.out.println("user > joinForm");

			WebUtil.forword(request, response, "/WEB-INF/views/user/joinForm.jsp");
		
		} else if("join".equals(act)) {
			System.out.println("user > join");
			
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo uvo = new UserVo(id, pwd, name, gender);
			UserDao uDao = new UserDao();
			
			uDao.insert(uvo);
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinOk.jsp");
		
		} else if("loginForm".equals(act)){
			System.out.println("user > loginForm");
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/loginForm.jsp");
		
		} else if("login".equals(act)) {
			System.out.println("user > login");
			
			String id = request.getParameter("id");
			String pass = request.getParameter("pass");
			
			UserDao uDao = new UserDao();
			UserVo authVo = uDao.getUser(id, pass);
			
			if(authVo == null) { //로그인 실패
				System.out.println("로그인실패");
				
				WebUtil.redirect(response, "/mysite/user?action=loginForm&result=fail");
			
			} else { //로그인 성공
				System.out.println("로그인성공");
				
				//세션번호 알려달라는뜻
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				
				WebUtil.redirect(response, "/mysite/main");
			}
			
		} else if("logout".equals(act)) {
			System.out.println("user > logout");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(response, "/mysite/main");
			
		} else if("modifyForm".equals(act)) {
			System.out.println("user > modifyForm");

			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			
			UserDao uDao = new UserDao();
			UserVo uvo = uDao.getUser(no);
			
			request.setAttribute("uvo", uvo);
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/modifyForm.jsp");
			
		} else if("modify".equals(act)) {
			System.out.println("user > modify");
			
			
			int no = Integer.parseInt(request.getParameter("no"));
			String id = request.getParameter("id");
			String pass = request.getParameter("pass");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo uvo = new UserVo(no, id, pass, name, gender);
			
			UserDao uDao = new UserDao();
			uDao.UserUpdate(uvo);
			
			UserVo authVo = new UserVo(uvo.getNo(), uvo.getName());
			
			HttpSession session = request.getSession();
			session.setAttribute("authUser", authVo);
			
			WebUtil.redirect(response, "/mysite/main");
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
