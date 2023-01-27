package logic;
/**
 * The Delivery class is used to hold data of an item in order  
 * It contains various SimpleStringProperties such as amount, price, name
 * These properties can be set and retrieved via getters and setters.
 */
public class productInOrder {
	private int amount;
	private double price;
	private String name;
	
	/**
	 * Constructor for the productInOrder class
	 * @param name
	 * @param amount
	 * @param price
	 */
	public productInOrder(String name,int amount, double price) {
		super();
		this.name = name;
		this.amount = amount;
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public String getAmountString() {
		return String.valueOf(amount);
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return  name  +","+ amount ;
	}
	
	
	
}
