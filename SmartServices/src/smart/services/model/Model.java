package smart.services.model;

public class Model {

	private int id;
	private String typeId;
	private String typeNameAr;
	private String typeNameEn;
	private String parentBrand;

	// constructors
	public Model() {

	}

	public Model(int id, String typeId, String typeNameAr, String typeNameEn,
			String parentBrand) {
		this.id = id;
		this.typeId = typeId;
		this.typeNameAr = typeNameAr;
		this.typeNameEn = typeNameEn;
		this.parentBrand = parentBrand;
	}

	public Model(String typeId, String typeNameAr, String typeNameEn,
			String parentBrand) {
		this.typeId = typeId;
		this.typeNameAr = typeNameAr;
		this.typeNameEn = typeNameEn;
		this.parentBrand = parentBrand;
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

	public String getParentBrand() {
		return parentBrand;
	}

	public void setParentBrand(String parentBrand) {
		this.parentBrand = parentBrand;
	}
}
