package myworld.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myworld.dao.AccountDao;

/**
 * Servlet implementation class MakeTransferServlet
 */
@WebServlet(description = "MakeTransferServlet", urlPatterns = { "/MakeTransferServlet" })
public class MakeTransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeTransferServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fromAccount = request.getParameter("fromAccount");
		String toAccount = request.getParameter("toAccount");
		String amount = request.getParameter("amount");
		String memo = request.getParameter("memo");
		
		boolean suc=false;
		double dAmount=0;
		try {
			dAmount = Double.parseDouble(amount);
			if (dAmount<=0) {
				suc=false;
			}
			suc=true;
		} catch (Exception e){
			suc=false;
		}
		
		if (suc) {
			AccountDao accountDao = new AccountDao();
			suc = accountDao.makeTransfer(fromAccount, toAccount, dAmount, memo);
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		if (suc) {
			out.println("<h1>Success Transfer Money!</h1>");
		} else {
			out.println("<h1>Failed Transfer Money!</h1>");
		}
		out.println("<h1><a href='clientAccount.jsp'>Return</a></h1>");
		
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
