package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String password = "webdb";

	private void getConnection() {

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, password);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {

		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 게시글 보기 정보
	public BoardVo getBoardVo(int index) {
		
		BoardVo bvo = new BoardVo();
		getConnection();
		
		try {

			String query = "";
			query += " select bo.no ";
			query += "		  ,title ";
			query += "		  ,content ";
			query += "   	  ,to_char(reg_date, 'yy-mm-dd hh24:mi') reg_date ";
			query += " 		  ,user_no ";
			query += " 		  ,ue.name ";
			query += " from   board bo, users ue ";
			query += " where  bo.user_no = ue.no ";
			query += " and bo.no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, index);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");

				bvo.setNo(no);
				bvo.setTitle(title);
				bvo.setContent(content);
				bvo.setRegDate(regDate);
				bvo.setUserNo(userNo);
				bvo.setUserName(userName);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return bvo;
	}
	
	// 게시판 리스트 출력
	public List<BoardVo> getList() {

		List<BoardVo> bList = new ArrayList<BoardVo>();
		getConnection();

		try {

			String query = "";
			query += " select bo.no ";
			query += "		  ,title ";
			query += "		  ,content ";
			query += "   	  ,to_char(reg_date, 'yy-mm-dd hh24:mi') reg_date ";
			query += " 		  ,user_no ";
			query += " 		  ,ue.name ";
			query += " from   board bo, users ue ";
			query += " where  bo.user_no = ue.no ";
			query += " order by reg_date desc ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				int no = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");

				BoardVo bvo = new BoardVo(no, title, content, regDate, userNo, userName);
				bList.add(bvo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return bList;
	}

	// 게시판 등록
	public void write(BoardVo bvo) {

		getConnection();

		try {

			String query = "";
			query += " insert into board ";
			query += " values(seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, bvo.getTitle());
			pstmt.setString(2, bvo.getContent());
			pstmt.setInt(3, bvo.getUserNo());

			int count = pstmt.executeUpdate();

			System.out.println("[" + count + "개의 글이 등록되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
	}

	// 게시글 삭제
	public void delete(int no) {
		
		getConnection();

		try {

			String query = "";
			query += " delete from board ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			int count = pstmt.executeUpdate();

			System.out.println("[" + count + "개의 글이 삭제되었습니다.]");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
	}
}
