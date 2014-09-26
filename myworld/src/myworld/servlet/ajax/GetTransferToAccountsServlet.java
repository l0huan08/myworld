package myworld.servlet.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myworld.dao.AccountDao;
import myworld.entity.Account;

/**
 * Servlet implementation class GetTransferToAccountsServlet
 */
@WebServlet(asyncSupported = true, description = "GetTransferToAccountsServlet", urlPatterns = { "/GetTransferToAccountsServlet" })
public class GetTransferToAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTransferToAccountsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//from account
		String acNumber = (String)request.getParameter("accountNumber");
		String username = (String)request.getSession().getAttribute("username");
		if (username==null || acNumber==null ){
			return;
		}
		if (username.isEmpty() || acNumber.isEmpty()) {
			return;
		}
		
		AccountDao accountDao = new AccountDao();
		// client's other accounts
		List<Account> accounts = accountDao.getAccountsByClient(username);
		
		PrintWriter out = response.getWriter();
		
		// output to a <select> tag
		for (Account ac : accounts){
			// ignore the fromAccount itself
			String toAcNumber = ac.getAccountNumber();
			if ( toAcNumber==null || acNumber.equals(toAcNumber)){
				continue;
			}
			out.println(String.format("<option value='%s'>%s</option>",toAcNumber,toAcNumber));
		}
		
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
