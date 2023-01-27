package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.Product;
/**
* This class is a controller class for the Item Cart GUI.
* It sets and updates the data for the item cart.
* @author Your Name
*/
public class ItemCartController {
    /**
     * Label for the name of the product.
     */
    @FXML
    private Label nameLabel;   
    /**
     * Label for the amount of the product.
     */
    @FXML
    private Label amountLabel;
    /**
     * ImageView for the product image.
     */
    @FXML
    private ImageView productImage;
    
    /**
     * Private variable for the amount of the product.
     */
    private int amount;
    /**
     * Private variable for the product object.
     */
    private Product product;
    /**
     * This method sets the data for the item cart.
     * @param product The product object.
     * @param amount The amount of the product.
     */
    public void setData(Product product,int amount) {
        this.product = product;
        this.amount = amount;
        nameLabel.setText(product.getName());
        amountLabel.setText("	 " + String.valueOf(amount) + "            " +product.getPrice()*amount +"$");
        Image image = product.getImage();
        productImage.setImage(image);
    }
    public void updateData(int amount) {
    	this.amount = amount;
    	setData(this.product,amount);
    	
    }

}
