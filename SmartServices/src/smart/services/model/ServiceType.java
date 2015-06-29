package smart.services.model;

public class ServiceType {

	private int id;
	private String typeId;
	private String typeNameAr;
	private String typeNameEn;

	// constructors
	public ServiceType() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeNameAr() {
		return typeNameAr;
	}

	public void setTypeNameAr(String typeNameAr) {
		this.typeNameAr = typeNameAr;
	}

	public String getTypeNameEn() {
		return typeNameEn;
	}

	public void setTypeNameEn(String typeNameEn) {
		this.typeNameEn = typeNameEn;
	}
	
}
