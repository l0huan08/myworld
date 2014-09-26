package myworld.servlet.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myworld.dao.TransactionDao;
import myworld.entity.Transaction;

/**
 * Servlet implementation class ShowStatementServlet
 */
@WebServlet(asyncSupported = true, description = "ShowStatementServlet", urlPatterns = { "/ShowStatementServlet" })
public class ShowStatementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowStatementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String accountNumber = request.getParameter("accountNumber");
		String strFromDate = request.getParameter("fromDate");
		String strToDate = request.getParameter("toDate");
		
		if (accountNumber==null || accountNumber.isEmpty()){
			return;
		}
		
		Date fromDate;
		Date toDate;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			fromDate = fmt.parse(strFromDate);
			toDate = fmt.parse(strToDate);
		} catch(Exception e) {
			return;
		}
		
		TransactionDao transDao = new TransactionDao();
		List<Transaction> transactions = transDao.getTransactionRecords(accountNumber, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()));
		if (transactions==null)
			return;
		
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		sb.append("<table>");
		sb.append("<tr><th>Type</th><th>Amount</th><th>Date</th><th>Description</th></tr>");
		for (Transaction t: transactions) {
			String tType = t.getTransactionType().getTypeName();
			sb.append(String.format("<tr class='%s'>",tType));
			sb.append(String.format("<td>%s</td>",tType));
			sb.append(String.format("<td>%f</td>",t.getAmount()));
			sb.append(String.format("<td>%s</td>",t.getTransactionTime().toString()));
			sb.append(String.format("<td>%s</td>",t.getDescription()));
			sb.append("</tr>");
		}
		sb.append("</table>");
		out.println(sb);
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
