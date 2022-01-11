package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDao {

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

	public GuestbookVo getGuest(int index) {

		GuestbookVo guestbookVo = null;

		try {
			getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select   no, ";
			query += " 		    name, ";
			query += " 		    password, ";
			query += " 			content, ";
			query += " 		    to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date ";
			query += " from     guestbook ";
			query += " where    no = ? ";
			query += " order by reg_date desc ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, index);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				guestbookVo = new GuestbookVo(no, name, password, content, regDate);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return guestbookVo;

	}

	public List<GuestbookVo> getList() {

		List<GuestbookVo> guestbookList = new ArrayList<GuestbookVo>();


		try {
			getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " select   no, ";
			query += " 		    name, ";
			query += " 		    password, ";
			query += " 			content, ";
			query += " 		    to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date ";
			query += " from     guestbook ";
			query += " order by reg_date desc ";

			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				GuestbookVo guestbookVo = new GuestbookVo(no, name, password, content, regDate);
				guestbookList.add(guestbookVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

			// 5. 자원정리
			close();
		return guestbookList;

	}

	public void guestInsert(GuestbookVo gvo) {

		try {
			
			getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into guestbook ";
			query += " values (seq_guestbook_no.nextval, ?, ?, ?, sysdate) ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, gvo.getName());
			pstmt.setString(2, gvo.getPassword());
			pstmt.setString(3, gvo.getContent());

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건이 추가되었습니다.}");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 

			// 5. 자원정리
			close();

	}

	public void guestDelete(int no, String password) {

		try {
			getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? and password = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			pstmt.setString(2, password);

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건이 삭제되었습니다.}");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

			// 5. 자원정리
			close();

	}

}
