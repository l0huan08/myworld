package myworld.servlet.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myworld.dao.ClientDao;
import myworld.entity.Client;

/**
 * Servlet implementation class UpdateClientProfile
 */
@WebServlet(asyncSupported = true, description = "UpdateClientProfileServlet", urlPatterns = { "/UpdateClientProfileServlet" })
public class UpdateClientProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateClientProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String fname = request.getParameter("fname");
		String mname = request.getParameter("mname");
		String lname = request.getParameter("lname");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String tel = request.getParameter("tel");
		String add1 = request.getParameter("add1");
		String add2 = request.getParameter("add2");
		String zip = request.getParameter("zip");
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String pw = request.getParameter("pw");
		
		SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-mm-dd");
		java.util.Date dateBirthday;
		try {
			dateBirthday = dateFormat.parse(birthday);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		Client client = new Client();
		client.setFirstName(fname);
		client.setMiddleName(mname);
		client.setLastName(lname);
		client.setGender(gender);
		client.setBirthday( new java.sql.Date(dateBirthday.getTime()) );
		client.setTel(tel);
		client.setAdd1(add1);
		client.setAdd2(add2);
		client.setZip(zip);
		client.setEmail(email);
		client.setUsername(username);
		client.setPassword(pw);
		
		ClientDao clientDao = new ClientDao();
		boolean suc = clientDao.updateClientProfile(username, client);
		if (suc){
			PrintWriter out = response.getWriter();
			out.println("{\"result\":\"true\"}");
			out.flush();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
