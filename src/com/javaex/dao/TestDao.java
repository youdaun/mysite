package com.javaex.dao;

import com.javaex.vo.UserVo;

public class TestDao {

	public static void main(String[] args) {
		
		UserDao udao = new UserDao();
		UserVo uvo = new UserVo("ccc", "1234", "심재윤", "남자");
		udao.insert(uvo);

	}

}
