package logic;

/**
 * The Sale class represents a sale.
 */
public class Sale {
	private String saleNumber;
	private String discountLevel;
	private String area;
	private String discription;
	private String isActive;
	
    /**
     * Constructs a new Sale object.
     * 
     * @param saleNumber the sale number
     * @param discountLevel the discount level
     * @param area the area
     * @param discription the description
     * @param isActive the active status
     */
	public Sale(String saleNumber, String discountLevel, String area, String discription, String isActive) {
		super();
		this.saleNumber = saleNumber;
		this.discountLevel = discountLevel;
		
		this.area = area;
		this.discription = discription;
		this.isActive = isActive;
	}
	public String getSaleNumber() {
		return saleNumber;
	}
	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}
	public String getDiscountLevel() {
		return discountLevel;
	}
	public void setDiscountLevel(String discountLevel) {
		this.discountLevel = discountLevel;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDiscription() {
		return discription;
	}
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "Sale [saleNumber=" + saleNumber + ", discountLevel=" + discountLevel + ", area=" + area
				+ ", discription=" + discription + ", isActive=" + isActive + "]";
	}
	
	
	
	
	

}
