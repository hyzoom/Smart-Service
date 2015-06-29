package smart.services.model;

public class Color {

	private int id;
	private String colorId;
	private String colorAr;
	private String colorEn;

	// constructors
	public Color() {

	}

	public Color(int id, String colorId, String colorAr, String colorEn) {
		this.id = id;
		this.colorId = colorId;
		this.colorAr = colorAr;
		this.colorEn = colorEn;
	}

	public Color(String colorId, String colorAr, String colorEn) {
		this.colorId = colorId;
		this.colorAr = colorAr;
		this.colorEn = colorEn;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getColorAr() {
		return colorAr;
	}

	public void setColorAr(String colorAr) {
		this.colorAr = colorAr;
	}

	public String getColorEn() {
		return colorEn;
	}

	public void setColorEn(String colorEn) {
		this.colorEn = colorEn;
	}
	
}
