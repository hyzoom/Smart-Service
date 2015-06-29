package smart.services.model;

public class Complain {

	private int id;
	private int serviceTypeId;
	private String complain;
	private String userId;
	
	public Complain() {
		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setServiceType(int serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}
	
	public int getServiceType() {
		return serviceTypeId;
	}
	
	public void setComplain(String complain) {
		this.complain = complain;
	}
	
	public String getComplain() {
		return complain;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
