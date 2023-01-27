package logic;

import javafx.scene.image.Image;
/**
 * The Product class represents a product that can be sold
 */
public class Product {
	private String name;
	private String imgSrc;
    private double price;
    private String color;
    private String id;//code in gui
    private int stock;
    private byte[] byteArray;
    private Image image;
    
    
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

    /**
     * Constructor to create an instance of Product with the given id, name, image source, color, price and stock
     * @param id the product's id
     * @param name the product's name
     * @param imgSrc the image source of the product
     * @param color the product's color
     * @param price the product's price
     * @param stock the product's stock
     * @param byteArray the product's image as a byte array
     */
	public Product(String id, String name, String imgSrc, String color, double price , int stock,byte[] byteArray) {
		super();
		this.name = name;
		this.imgSrc = imgSrc;
		this.price = price;
		this.color = color;
		this.id = id;
		this.stock = stock;
		this.byteArray = byteArray;
	}
	
	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	
	
	
   

}
