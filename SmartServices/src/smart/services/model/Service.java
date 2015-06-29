package smart.services.model;

public class Service {
	private int id;
	private String serviceId;
	private String descAr;
	private String descEn;
	private String titleAr;
	private String titleEn;
	
	// constructors
	public Service() {

	}
	
	public int getId() {
		return id;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescAr() {
		return descAr;
	}

	public void setDescAr(String descAr) {
		this.descAr = descAr;
	}
	
	public String getDescEn() {
		return descEn;
	}

	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}
	
	public String getTitleAr() {
		return titleAr;
	}

	public void setTitleAr(String titleAr) {
		this.titleAr = titleAr;
	}
	
	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
}
