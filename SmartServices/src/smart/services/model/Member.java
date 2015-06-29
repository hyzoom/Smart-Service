package smart.services.model;

public class Member {
	private int id;
	private String userId;
	private String email;
	private String loyaltyId;
	private String msisdn;
	private String name;
	private String address;

	// constructor

	public Member() {
	}

	public Member(int id, String userId, String email, String loyaltyId,
			String msisdn, String name) {
		this.id = id;
		this.userId = userId;
		this.msisdn = msisdn;
		this.name = name;
		this.email = email;
		this.loyaltyId = loyaltyId;
	}

	public Member(String userId, String email, String loyaltyId, String msisdn,
			String name) {
		this.userId = userId;
		this.email = email;
		this.loyaltyId = loyaltyId;
		this.msisdn = msisdn;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

	public String getLoyaltyId() {
		return loyaltyId;
	}

	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
