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
 * Servlet implementation class ClientAccountServlet
 */
@WebServlet(asyncSupported = true, description = "ClientAccountServlet", urlPatterns = { "/ClientAccountServlet" })
public class ClientAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AccountDao acDao = new AccountDao();
		String username = (String)request.getSession().getAttribute("username");
		List<Account> accounts = acDao.getAccountsByClient(username);
		
		StringBuffer tb=new StringBuffer();
		tb.append("<table>");
		tb.append("<tr>");
		tb.append("<th> </th>");
		tb.append("<th>number</th>");
		tb.append("<th>type</th>");
		tb.append("<th>status</th>");
		tb.append("<th>balance</th>");
		tb.append("</tr>");

		if (accounts != null) {
			for (Account ac : accounts) {
				String row = createTableRow(ac);
				tb.append(row);
			}
		} else {
			tb.append("<tr>No Account</tr>");
		}
		
		tb.append("</table>");
		
		PrintWriter out = response.getWriter();
		out.println(tb);
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
	
	/**
	 * Create a table row by an account
	 * @param account
	 * @return
	 */
	private String createTableRow(Account account) {
		//account number
		//account type
		//status: Frozen, Active
		StringBuffer sb=new StringBuffer();
		if (account!=null){
			String status = account.isActive()?"Active":"Frozen";
			sb.append(String.format("<tr class='%s'>",status));
			sb.append(String.format(
					"<td><input type='radio' name='selAccount' value='%s' /></td>",
					account.getAccountNumber()));
			sb.append("<td>"+account.getAccountNumber()+"</td>");
			sb.append("<td>"+account.getAccountType().getTypeName()+"</td>");
			sb.append(String.format("<td class='%s'>%s</td>",status,status));
			sb.append("<td>"+ account.getBalance() + "</td>");
			sb.append("</tr>");
		}
		return sb.toString();
	}

}
