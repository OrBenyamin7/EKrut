package logic;

/*
 * The UpdateDeliveryCustomer class is a logic class that represents a delivery that can be updated by the user.
 * It contains the order status, order number, and delivery received information. 
 */
public class UpdateDeliveryCustomer {
	private String Orderstatus;
	private String Ordernumber;
	private String DeliveryReceived;
	
    /*
     * Constructs a new UpdateDeliveryCustomer object with the given order status, order number, and delivery received information.
     * @param ordernumber the order number of the delivery
     * @param orderstatus the order status of the delivery
     * @param deliveryReceived the delivery received information of the delivery
     */
	public UpdateDeliveryCustomer( String ordernumber,String orderstatus, String deliveryReceived) {
		Orderstatus = orderstatus;
		Ordernumber = ordernumber;
		DeliveryReceived = deliveryReceived;
	}
	public String getOrderstatus() {
		return Orderstatus;
	}
	public void setOrderstatus(String orderstatus) {
		Orderstatus = orderstatus;
	}
	public String getOrdernumber() {
		return Ordernumber;
	}
	public void setOrdernumber(String ordernumber) {
		Ordernumber = ordernumber;
	}
	public String getDeliveryReceived() {
		return DeliveryReceived;
	}
	public void setDeliveryReceived(String deliveryReceived) {
		DeliveryReceived = deliveryReceived;
	}
	@Override
	public String toString() {
		return "UpdateDeliveryCustomer [Orderstatus=" + Orderstatus + ", Ordernumber=" + Ordernumber
				+ ", DeliveryReceived=" + DeliveryReceived + "]";
	}
	

	
}
