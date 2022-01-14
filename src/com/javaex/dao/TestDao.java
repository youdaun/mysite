package com.javaex.dao;

import java.util.List;

import com.javaex.vo.BoardVo;

public class TestDao {

	public static void main(String[] args) {
		
		BoardDao bDao = new BoardDao();
		List<BoardVo> bList = bDao.getList();
		
		for(int i=0; i<bList.size(); i++){
			System.out.println(bList.get(i).getTitle());
		}

	}

}
