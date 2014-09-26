package myworld.servlet.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myworld.dao.ClientDao;
import myworld.entity.Client;

/**
 * Servlet implementation class ClientProfileServlet
 */
@WebServlet(asyncSupported = true, description = "ClientProfileServlet", urlPatterns = { "/ClientProfileServlet" })
public class ClientProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String)request.getParameter("username");
		ClientDao clientDao = new ClientDao();
		Client client = clientDao.getClient(username);
		StringBuffer jsonData = new StringBuffer();
		jsonData.append("{");
		
		jsonData.append(String.format("\"fname\":\"%s\",", client.getFirstName() ) );
		jsonData.append(String.format("\"mname\":\"%s\",", client.getMiddleName()) );
		jsonData.append(String.format("\"lname\":\"%s\",", client.getLastName()) );
		
		jsonData.append(String.format("\"gender\":\"%s\",", client.getGender()) );
		jsonData.append(String.format("\"birthday\":\"%s\",", client.getBirthday().toString() ) );
		jsonData.append(String.format("\"tel\":\"%s\",", client.getTel()) );
		jsonData.append(String.format("\"add1\":\"%s\",", client.getAdd1()) );
		jsonData.append(String.format("\"add2\":\"%s\",", client.getAdd2()) );
		jsonData.append(String.format("\"zip\":\"%s\",", client.getZip()) );
		jsonData.append(String.format("\"email\":\"%s\",", client.getEmail()) );
		jsonData.append(String.format("\"username\":\"%s\",", client.getUsername()) );
		jsonData.append(String.format("\"pw\":\"%s\"", client.getPassword()) );
		
		jsonData.append("}");
		
		PrintWriter out = response.getWriter();
		out.println(jsonData);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
}
