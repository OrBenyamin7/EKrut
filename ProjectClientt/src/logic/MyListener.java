package logic;

/**
 * The MyListener interface defines a method for listening to clicks on a product
 */
public interface MyListener {
	
    /**
     * This method will be called when a product is clicked
     * @param product the product that was clicked
     */
    public void onClickListener(Product product);
}