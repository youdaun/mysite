package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

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

	// 저장 메소드(회원가입)
	public int insert(UserVo uvo) {

		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into users ";
			query += " values(seq_users_no.nextval, ?, ?, ?, ?) ";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, uvo.getId());
			pstmt.setString(2, uvo.getPassword());
			pstmt.setString(3, uvo.getName());
			pstmt.setString(4, uvo.getGender());
			
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + "건이 추가되었습니다(UserDao)]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		
		return count;
	}
	
	//회원정보1명 가져오기(로그인용)
	public UserVo getUser(String id, String pass) {
		
		UserVo uvo = null;
		getConnection();
		
		try {
		String query = "";
		query += " select no ";
		query += "		  ,name ";
		query += " from   users ";
		query += " where  id = ? ";
		query += " and    password = ? ";
		
		pstmt = conn.prepareStatement(query);
		
		pstmt.setString(1, id);
		pstmt.setString(2, pass);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			int no = rs.getInt("no");
			String name = rs.getString("name");
			
			uvo = new UserVo(no, name);
			
		}
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return uvo;
				
	}
	
	//회원정보1명 가져오기(회원정보수정용)
	public UserVo getUser(int num) {
		
		UserVo uvo = null;
		getConnection();
		
		try {
		String query = "";
		query += " select no"
				+ "		  ,id"
				+ "		  ,password ";
		query += "		  ,name ";
		query += "		  ,gender ";
		query += " from   users ";
		query += " where  no = ? ";
		
		pstmt = conn.prepareStatement(query);
		
		pstmt.setInt(1, num);
		
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			int no = rs.getInt("no");
			String id = rs.getString("id");
			String pass = rs.getString("password");
			String name = rs.getString("name");
			String gender = rs.getString("gender");
			
			uvo = new UserVo(no, id, pass, name, gender);
			
		}
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return uvo;
				
	}
	
	public void UserUpdate(UserVo uvo) {

		getConnection();

		try {
			String query = "";
			query += " update users ";
			query += " set    password = ?, ";
			query += "        name = ?, ";
			query += "        gender = ? ";
			query += " where  no = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, uvo.getPassword());
			pstmt.setString(2, uvo.getName());
			pstmt.setString(3, uvo.getGender());
			pstmt.setInt(4, uvo.getNo());

			int count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println("[" + count + " 건이 수정되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

	}

}
