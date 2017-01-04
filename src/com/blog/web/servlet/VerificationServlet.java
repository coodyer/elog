package com.blog.web.servlet;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.VerificationCodeUtil;

public class VerificationServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("image/gif");
		VerificationCodeUtil verificationCodeUtil = (VerificationCodeUtil) SpringContextHelper
				.getBean("verificationCodeUtil");
		String verCode = verificationCodeUtil.getCodeStr(4);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(verificationCodeUtil.outCode(100, 32,  28, verCode),
				"png", out);
		request.getSession().setAttribute("piccode", verCode);
		out.flush();
		out.close();
	}

}
