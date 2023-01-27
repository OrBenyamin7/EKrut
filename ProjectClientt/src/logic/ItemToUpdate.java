package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

/**
 * The ItemToUpdate class is used to store and update the item's number, name and current amount
 */
public class ItemToUpdate 
{
	 private SimpleStringProperty number;
	 private SimpleStringProperty item;
	 private TextField currentAmount;
	
	    /**
	     * Constructor to create an instance of ItemToUpdate with the given number, name and amount
	     * @param number the item's number
	     * @param item the item's name
	     * @param amount the item's current amount
	     */
	 public ItemToUpdate(String number, String item, String amount){
	     this.number = new SimpleStringProperty(number);
	     this.item = new SimpleStringProperty(item);
	     this.currentAmount = new TextField();
	     currentAmount.setText(amount);
	     currentAmount.setPrefWidth(80);
	     currentAmount.setMaxWidth(80);
	     currentAmount.setAlignment(Pos.CENTER);

	        
	 }

	public String getNumber() {
		return number.get();
	}

	public void setNumber(String number) {
		this.number = new SimpleStringProperty(number);
	}

	public String getItem() {
		return item.get();
	}

	public void setItem(String item) {
		this.item = new SimpleStringProperty(item);
	}

	public TextField getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(TextField currentAmount) {
		this.currentAmount = currentAmount;
	}
	 
	 
}
