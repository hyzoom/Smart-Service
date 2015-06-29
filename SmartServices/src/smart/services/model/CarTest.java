package smart.services.model;

public class CarTest {

	private int id;
	private String carId;
	private String carBrand;
	private String carModelId;
	private String carModel;
	private String year;
	private String carColor;
	private String chase;
	private String plateNumber;
	private String userId;
	private int insuranceId;

	// constructors
	public CarTest() {

	}

	public CarTest(String carId, String userId, String carBrand,
			String carModelId, String carModel, String year, String carColor,
			String chase) {
		this.carId = carId;
		this.userId = userId;
		this.carBrand = carBrand;
		this.carModelId = carModelId;
		this.carModel = carModel;
		this.year = year;
		this.carColor = carColor;
		this.chase = chase;
	}

	public CarTest(String carBrand, String carModelId, String carModel,
			String year, String carColor, String chase) {
		this.carBrand = carBrand;
		this.carModelId = carModelId;
		this.carModel = carModel;
		this.year = year;
		this.carColor = carColor;
		this.chase = chase;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarModelId() {
		return carModelId;
	}

	public void setCarModelId(String carModelId) {
		this.carModelId = carModelId;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getChase() {
		return chase;
	}

	public void setChase(String chase) {
		this.chase = chase;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public int getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(int insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
