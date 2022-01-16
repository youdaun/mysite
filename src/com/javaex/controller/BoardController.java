package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String act = request.getParameter("action");

		if ("list".equals(act)) {
			System.out.println("board > list");

			BoardDao bDao = new BoardDao();
			List<BoardVo> bList = bDao.getList();

			request.setAttribute("bList", bList);

			WebUtil.forword(request, response, "/WEB-INF/views/board/list.jsp");

		} else if ("writeForm".equals(act)) {
			System.out.println("board > writeForm");

			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");

			if (authUser != null) {
				WebUtil.forword(request, response, "/WEB-INF/views/board/writeForm.jsp");
			} else {
				System.out.println("잘못된접근(writeForm)");
				WebUtil.redirect(response, "/mysite/board?action=list");
			}

		} else if ("write".equals(act)) {
			System.out.println("board > write");

			int userNo = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			BoardVo bvo = new BoardVo(title, content, userNo);

			BoardDao bDao = new BoardDao();
			bDao.write(bvo);

			WebUtil.redirect(response, "/mysite/board?action=list");

		} else if ("delete".equals(act)) {
			System.out.println("board > delete");

			int userNo = Integer.parseInt(request.getParameter("no"));

			BoardDao bDao = new BoardDao();
			bDao.delete(userNo);

			WebUtil.redirect(response, "/mysite/board?action=list");

		} else if ("read".equals(act)) {
			System.out.println("board > read");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao bDao = new BoardDao();
			bDao.updateHit(no);
			BoardVo bvo = bDao.getBoardVo(no);

			request.setAttribute("bvo", bvo);

			WebUtil.forword(request, response, "/WEB-INF/views/board/read.jsp");
			
		} else if ("modifyForm".equals(act)) {
			System.out.println("board > modifyForm");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao bDao = new BoardDao();
			BoardVo bvo = bDao.getBoardVo(no);
			
			request.setAttribute("bvo", bvo);

			WebUtil.forword(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		} else if("modify".equals(act)) {
			System.out.println("board > modify");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			BoardVo bvo = new BoardVo(no, title, content);
			BoardDao bDao = new BoardDao();
			bDao.updateBoard(bvo);
			
			WebUtil.redirect(response, "/mysite/board?action=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
