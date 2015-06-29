package smart.services.model;

public class SignUpInfo {
	private int id;
	private String verCode;
	private String mobNum;
	private String name;
	private String email;
	private String password;
	private String address;
	private String isLoggedOut;

	// constructor

	public SignUpInfo() {
	}

	public SignUpInfo(int id, String verCode, String mobNum, String name,
			String email, String password, String address, String isLoggedOut) {
		this.id = id;
		this.verCode = verCode;
		this.mobNum = mobNum;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.isLoggedOut = isLoggedOut;
	}

	public SignUpInfo(String verCode, String mobNum, String name, String email,
			String password, String address, String isLoggedOut) {
		this.verCode = verCode;
		this.mobNum = mobNum;
		this.name = name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.isLoggedOut = isLoggedOut;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVerCode() {
		return verCode;
	}

	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}

	public String getMobNum() {
		return mobNum;
	}

	public void setMobNum(String mobNum) {
		this.mobNum = mobNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsLoggedOut() {
		return isLoggedOut;
	}

	public void setIsLoggedOut(String isLoggedOut) {
		this.isLoggedOut = isLoggedOut;
	}

}
