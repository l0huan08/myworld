package myworld.entity;

import java.sql.Date;

public class Client {
	
	private int clientId=0;
	private String firstName=null;
	private String middleName=null;
	private String lastName=null;
	private String gender=null; //M,F
	private Date birthday=null;
	private String tel=null;
	private String add1=null;
	private String add2=null;
	private String zip=null; //221008
	private String email=null;
	private String username=null;
	private String password=null;
	
	
	public Client(){
		
	}
	
	public Client(int clientId, String firstName, String middleName,
			String lastName, String gender, Date birthday, String tel,
			String add1, String add2, String zip, String email,
			String username, String password) {
		this.clientId = clientId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthday = birthday;
		this.tel = tel;
		this.add1 = add1;
		this.add2 = add2;
		this.zip = zip;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAdd1() {
		return add1;
	}
	public void setAdd1(String add1) {
		this.add1 = add1;
	}
	public String getAdd2() {
		return add2;
	}
	public void setAdd2(String add2) {
		this.add2 = add2;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
