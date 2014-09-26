package myworld.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import myworld.dao.ClientDao;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(description = "LoginServlet", urlPatterns = { "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		ClientDao clientDao = new ClientDao();
		boolean bLoginSuccess = clientDao.loginClient(username, password);
		PrintWriter writer = response.getWriter();
		if (!bLoginSuccess) {
			writer.println("<h1>Wrong username or password!</h1>");
			writer.println("<a href='login.jsp'>Return</a>");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			session.setAttribute("role", "client");
			session.setAttribute("islogin", "true");
			
			writer.println("<h1>Login...</h1>");
			writer.println("<script>alert('Success Login!')</script>");
			response.addHeader("REFRESH", "1;URL=clientMain.jsp");
			//response.sendRedirect("clientMain.jsp");

		}
	}

}
