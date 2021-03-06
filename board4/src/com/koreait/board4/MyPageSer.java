package com.koreait.board4;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.board4.model.UserDAO;
import com.koreait.board4.vo.UserChangeVO;
import com.koreait.board4.vo.UserVO;

@WebServlet("/myPage") // 하나의 값으로 여러가지 일을 처리 (비밀번호 변경, 이름변경 등등) 뒤에오는 typ의 값으로 구분한다.
public class MyPageSer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String typ = request.getParameter("typ"); // 넘어오는 값에 따라 정보 수정
		String jsp = "/WEB-INF/jsp/";

		switch (typ) {
		case "1":
			jsp += "changePw.jsp";
			break;
		}

		request.getRequestDispatcher(jsp).forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { // post에서는 jsp파일을 열 일이 없다!

		UserVO loginuser = Utils.getLoginUser(request);

		String typ = request.getParameter("typ");
		
		try {
			
			switch (typ) {
			case "1": // 비밀번호 수정
				procTyp1(loginuser, request, response);
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
			

	}
	
	
	//proc 비밀번호 수정
	private void procTyp1(UserVO loginuser, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String currentPw = request.getParameter("currentPw");
		String changePw = request.getParameter("changePw");

		UserChangeVO param = new UserChangeVO();
		param.setChangePw(changePw);
		param.setCurrentPw(currentPw);
		param.setI_user(loginuser.getI_user());

		int result = UserDAO.changePw(param);

		if (result == 1) {
			Utils.logout(request);
			response.sendRedirect("/login");
			return;
		}

		String msg = "";
		switch (result) {
		case 0:
			msg = "기존 비밀번호를 확인해주세요";
			break;
		case 2:
			msg = "통신 에러 발생";
			break;

		}

		request.setAttribute("msg", msg);
		doGet(request, response);
		
	}

}
