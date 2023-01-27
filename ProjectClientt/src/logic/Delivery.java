package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * The Delivery class is used for tracking and managing delivery data of an order.
 * It contains various SimpleStringProperties such as orderNumber, status, address, deliveryDetails,
 * daysLeft, notified, received, approve and area, along with a button for approve request.
 * These properties can be set and retrieved via getters and setters.
 */
public class Delivery {

	private Button ReqApprove; //Delivery employee approves new delivery request, clientDelStatus will change from new to handled -> afterwards to completed
	private SimpleStringProperty approve;
	private SimpleStringProperty orderNumber;
	private SimpleStringProperty status;//client delivery status:{new, handled, completed }
	private SimpleStringProperty address;
	private SimpleStringProperty Area;
	private SimpleStringProperty deliveryDetails;//estimated delivery date and time private String Area;
	private SimpleStringProperty daysLeft;
	private SimpleStringProperty notified;
	private SimpleStringProperty received;
	
	/**
	 * Constructor for the Delivery class
	 * @param orderNumber the order number of the delivery
	 * @param status the client delivery status of the order (can be "new", "handled", or "completed")
	 * @param address the address of the delivery
	 * @param deliveryDetails the estimated delivery date and time
	 * @param daysLeft remaining days for delivery
	 * @param notified client notification status
	 * @param received delivery received status
	 * @param approve approve status of the delivery request
	 * @param area area of the delivery
	 */
	public Delivery(String orderNumber, String status, String address, String deliveryDetails, String daysLeft, String notified ,String received,String approve, String area) {
		this.orderNumber = new SimpleStringProperty(orderNumber);
		this.status = new SimpleStringProperty(status);
		this.address = new SimpleStringProperty(address);
		this.deliveryDetails = new SimpleStringProperty(deliveryDetails);
		this.daysLeft = new SimpleStringProperty(daysLeft);
		this.notified = new SimpleStringProperty(notified);
		this.Area = new SimpleStringProperty(area);
		this.received = new SimpleStringProperty(received);
		this.approve = new SimpleStringProperty(approve);
		this.ReqApprove = new Button("Approve");
	}
	
	public void setReqApprovalBTN(Button reqApprove) {
		this.ReqApprove = reqApprove;
	}
	
	public Button getReqApprovalBTN() {
		return ReqApprove;
	}
	
	public void setOrdernumber(String ordernumber) {
		orderNumber = new SimpleStringProperty(ordernumber);
	}
	
	public String getOrdernumber() {
		return orderNumber.get();
	}

	
	public void setAddress(String address) {
		this.address =new SimpleStringProperty(address);
	}
	
	public String getAddress() {
		return address.get();
	}
	
	public String getDaysLeftForDelivery() {
		return daysLeft.get();
	}

	public void setDaysLeftForDelivery(String daysLeftForDelivery) {
		daysLeft = new SimpleStringProperty(daysLeftForDelivery);
	}

	public String getArea() {
		return Area.get();
	}
	public void setArea(String area) {
		Area = new SimpleStringProperty(area);
	}

	public void setClientNotified(String clientNotified) {
		notified = new SimpleStringProperty(clientNotified);
	}
	
	public String getDeliveryReceived() {
		return received.get();
	}

	public void setDeliveryReceived(String deliveryReceived) {
		received =  new SimpleStringProperty(deliveryReceived);
	}


	public String getApproveRequest() {
		return approve.get();
	}

	public void setApproveRequest(String approveRequest) {
		approve = new SimpleStringProperty(approveRequest);
	}

	public String getClientDelStatus() {
		return status.get();
	}

	public void setClientDelStatus(String clientDelStatus) {
		status = new SimpleStringProperty(clientDelStatus);
	}

	public String getDeliveryDetails() {
		return deliveryDetails.get();
	}

	public void setDeliveryDetails(String deliveryDetails) {
		this.deliveryDetails = new SimpleStringProperty(deliveryDetails);
	}

	public String getClientNotified() {
		return notified.get();
	}

	@Override
	public String toString() {
		return "Delivery [ReqApprove=" + ReqApprove + ", approve=" + approve + ", orderNumber=" + orderNumber
				+ ", status=" + status + ", address=" + address + ", Area=" + Area + ", deliveryDetails="
				+ deliveryDetails + ", daysLeft=" + daysLeft + ", notified=" + notified + ", received=" + received
				+ "]";
	}

	
	

}
