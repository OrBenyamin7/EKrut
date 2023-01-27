package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import logic.MyListener;
import logic.Product;
/**
* This class is a controller class for the Item GUI.
* It sets the data for the item and handles the click event.
* @author Your Name
*/
public class ItemController {
    /**
     * Label for the name of the product.
     */
    @FXML
    private Label nameLabel;

    /**
     * Label for the price of the product.
     */
    @FXML
    private Label priceLable;

    /**
     * ImageView for the product image.
     */
    @FXML
    private ImageView img;

    /**
     * Image object for the product image.
     */
    Image image ;

    /**
     * This method handles the click event on the item.
     * @param mouseEvent The click event.
     */
    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }

    /**
     * Private variable for the product object.
     */
    private Product product;
    /**
     * Private variable for the MyListener object.
     */
    private MyListener myListener;

    /**
     * This method sets the data for the item.
     * @param product The product object.
     * @param myListener The MyListener object.
     */
    public void setData(Product product, MyListener myListener) {
        this.product = product;
        this.myListener = myListener;
        nameLabel.setText(product.getName());
        priceLable.setText(product.getPrice() + "$" );
        
        img.setImage(product.getImage());
    }
    
}
