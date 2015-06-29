package smart.services.model;

public class Brand {

	private int id;
	private String typeId;
	private String typeNameAr;
	private String typeNameEn;
	private String carModels;

	// constructors
	public Brand() {

	}

	public Brand(int id, String typeId, String typeNameAr, String typeNameEn,
			String carModels) {
		this.id = id;
		this.typeId = typeId;
		this.typeNameAr = typeNameAr;
		this.typeNameEn = typeNameEn;
		this.carModels = carModels;
	}

	public Brand(String typeId, String typeNameAr, String typeNameEn,
			String carModels) {
		this.typeId = typeId;
		this.typeNameAr = typeNameAr;
		this.typeNameEn = typeNameEn;
		this.carModels = carModels;
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

	public String getCarModels() {
		return carModels;
	}

	public void setCarModels(String carModels) {
		this.carModels = carModels;
	}

}
