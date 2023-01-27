package gui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.ClientUI;
import common.MyFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.MyListener;
import logic.Product;
import logic.productInOrder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EKrutFinalMarketController implements Initializable {
	////////////// Variables Start ////////////////////

    @FXML
    private TextField FinalOrderSum;

    @FXML
    private Button PayAsMember;

    @FXML
    private Label comboSaleLeft;
    @FXML
    private Label comboSaleRight;

	@FXML
    private Button Purchasebtn;
    @FXML
    private GridPane gridCart;

    @FXML
    private ScrollPane scrollCart;

    @FXML
    private Label SaleLeft1;

	@FXML
	private Label EkrutBasketLabel;

	@FXML
	private Button backbt;

	@FXML
	private Button cancelOrder;

	@FXML
	private Button exitbtn;

	@FXML
	private GridPane grid;

	@FXML
	private Button minosBtnLeft;

	@FXML
	private Button plusBtnLeft;
	@FXML
	private AnchorPane chosenProductCard;

	@FXML
	private TextField productAmountValueLefft;

	@FXML
	private TextField productCodeValueLeft;

	@FXML
	private TextField productCostValueLeft;

	@FXML
	private ImageView productLeftPicture;

	@FXML
	private Label productNameLableLeft;

	@FXML
	private Button reviewOrder;

	@FXML
	private ScrollPane scroll;
	@FXML
	private Button addToCartBTN;
	@FXML
	private ImageView saleImage;

	@FXML
    private Label outOfStockLabel;
	@FXML
    private Label rightSaleLabel;
	@FXML
    private Label leftSaleLabel;
	@FXML
    private Label ol1;
    @FXML
    private Label ol2;
    @FXML
    private Label ek;
	private static String machineName;

	private static ArrayList<String> ekOrOLFromConnect;
	
	private Image image;  
	private MyListener myListener;
	private double xoffset;
	private double yoffset;
	private int discountLevel;
	private int row = 1;
	private double totalPrice = 0;
	private Map<String, AnchorPane> itemAndAnchorMap = new HashMap<>();
	private ArrayList<productInOrder> currentOrderProducts = new ArrayList<>();
	private static ArrayList<MyFile> productsFromDBMyFile;///!!!!!!///
	private List<Product> products = new ArrayList<>();
	private Map<String,ItemCartController> itemAndControllerMap = new HashMap<>();
	private static boolean isAMember;
	private static String method=null;
	private static String address=null;
	private static String area=null;
	private static String date=null;
	private static String isfirst="";
	private static boolean firstBuyDiscount=false;
	private static ArrayList<String> arrFromServerRet;
	private boolean isDeferdPayment = false;
	private static String orderCode;
	private static String confirmedPurchese;
	private Timer timer = new Timer();
//////////////Variables End ////////////////////
	
	//Set the timer inside the controller 
	private TimerTask task = new TimerTask() {

		
		@Override
		public void run() {
			ArrayList<String> msg = new ArrayList<>();
			msg.add("quit");
			try {
				ClientUI.chat.accept(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(1);
		}
	};
	//Static methods for getting info from server via chatClient
	
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }

	public static void getProductsFromOrderType(ArrayList<MyFile> productsFromDB1) {
		productsFromDBMyFile = productsFromDB1;
	}
	public static void getUserData(ArrayList<String> arrFromServer){
    	arrFromServerRet = arrFromServer;
    }
	
	public static void setIsAMember(boolean type) {
		isAMember = type;
	}
	
	public static void setMacineName(String name) {
		machineName = name;
	}
	public static void setMethod(String name) {
		method = name;
	}
	public static void setAddress(String name) {
		address = name;
	}
	public static void setArea(String name) {
		area = name;
	}
	
	public static void setOrderCode(ArrayList<String> massageFromServer) {		
		orderCode=massageFromServer.get(0);
	}
	
	public static void isFirstMemberBur(ArrayList<String> massageFromServer) {		
		isfirst=massageFromServer.get(0);
	}
	
	public static void isConfirmedPurches(String data) {
		confirmedPurchese = data;
	}
	
	/**

	The method PressPurchase is a method that activated when the "Purchase" button is pressed.
	This method updates the stock in the database and opens the OrderAcceptedController window.
	If any of the products in the order are already bought by other customer, an error message will appear.
	@param event - the event that triggered this method (pressing the "Purchase" button)
	@throws Exception - if there is a problem with the updateStock method or addReservationToDB method.
	*/
	
	@FXML
    void PressPurchase(ActionEvent event) throws Exception {
		
		String answerFromDb = updateStock();
		if(answerFromDb.equals("Items Already Bought By Other Customer")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
			    alert.setTitle("Problem In Order");
				alert.setHeaderText("Items Already Bought By Other Customer ");
				alert.showAndWait();
				Stage primaryStage = new Stage();
				timer.cancel();
				String role = arrFromServerRet.get(2);
				switch(role) {
				case "Customer":
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					primaryStage.initStyle(StageStyle.UNDECORATED);
					CustomerMainScreenController cmc = new CustomerMainScreenController();
					CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
					CustomerMainScreenController.getUserData(arrFromServerRet);
					cmc.start(primaryStage);	
					break;
				case "AreaManager":
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					primaryStage.initStyle(StageStyle.UNDECORATED);
					AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
					AreaManagerHomePageController.detailsOnUser(arrFromServerRet);
					AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);

					amgpc.start(primaryStage);
					break;
					
				case "ServiceRepresentative":
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					primaryStage.initStyle(StageStyle.UNDECORATED);
					ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
					ServiceRepresentativeHomePageController.getUserData(arrFromServerRet);
					ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
					
					srhpc.start(primaryStage);
					break;
				case "MarketingManager":
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					primaryStage.initStyle(StageStyle.UNDECORATED);
		
					MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
					MarketingManagerHomePageController.getUserData(arrFromServerRet);
					MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
					mmhpc.start(primaryStage);
					break;

				case "OperationWorker":
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					primaryStage.initStyle(StageStyle.UNDECORATED);
					OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
					OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
					OperationWorkerHomePageController.detailsOnUser(arrFromServerRet);
					owhpc.start(primaryStage);
					break;
					
					case "MarketingAreaWorker":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
						MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
						MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet);
						
						mawhpc.start(primaryStage);
						break;
					case "DeliveryEmployee":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
						DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
						DeliveryEmployeeHomePageController.getUserData(arrFromServerRet);//Details about user
						dehpc.start(primaryStage);
						break;
					case "Ceo":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						CeoHomePageController chpc = new CeoHomePageController();
						CeoHomePageController.detailsOnUser(arrFromServerRet);
						CeoHomePageController.ekOrOl(ekOrOLFromConnect);
							
						chpc.start(primaryStage);
						break;
				
					default:
						
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
						primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.UNDECORATED);
						CustomerMainScreenController cmsc = new CustomerMainScreenController();
						CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
						cmsc.start(primaryStage);
						break;
				}
			
		}else {
			if (currentOrderProducts.size()>0) {
				timer.cancel();
				
				
				if(method==null) {
					method="local";
				}
				addResrvationToDB();
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				Stage primaryStage = new Stage();
				primaryStage.initStyle(StageStyle.UNDECORATED);
				OrderAcceptedController oac = new OrderAcceptedController();
				OrderAcceptedController.setMethodForAccepted(method);
				OrderAcceptedController.ekOrOl(ekOrOLFromConnect);
				OrderAcceptedController.getUserData(arrFromServerRet);
				//OrderPickedController.getOrderFromPickUpConroller(arrFromServerRet);
				oac.start(primaryStage);

			}
			else {
				outOfStockLabel.setText("empty order!");
			}
		}
		
		
    }

	/**

	This method is responsible for adding the current order to the database as a reservation. It collects the necessary data from the order, such as the products and their amounts, the total price, the method of payment and delivery, and the date of the reservation.
	Additionally, it checks if the payment is deferred or not, and sends the data to the server via the chat object of the ClientUI class.
	@throws IOException if there is a problem with the communication with the server.
	*/
	void addResrvationToDB() throws IOException {
		ArrayList<String> res = new ArrayList<>();
		res.add("addResrvation");
		String reservation=currentOrderProducts.toString();
		res.add(reservation.substring(1,reservation.length()-1).replaceAll(" ", ""));
		res.add(String.valueOf(totalPrice));
		res.add(machineName);
		res.add(method);
		if(method.equals("Delivery")) {
			res.add(address);
		}else {
			res.add("-");
		}
		if(method.equals("local")) {
			res.add("Completed");
		}
		else {
			res.add("NotCompleted");
		}
		
		res.add(area);
		if(method.equals("local")) {
			res.add(String.valueOf(java.time.LocalDate.now()));
		}else {
			res.add(String.valueOf(java.time.LocalDate.now()));
		}
		if(isDeferdPayment) {
			res.add("1");
		}else {
			res.add("0");
		}
		
		ClientUI.chat.accept(res);
		OrderAcceptedController.setOrderCode(orderCode);
		OrderAcceptedController.setResrvation(reservation);
	}
	
	/**

	This method is responsible for handling the "Pay as Member" button press event.
	When the button is pressed, the method cancels the timer, sets isDeferdPayment to true,
	calls the updateStock() method, adds the reservation to the DB, hides the current window and opens
	the OrderAcceptedController window. The method also sets the payment method and passes on the ekOrOLFromConnect and arrFromServerRet variables.
	If the currentOrderProducts is empty, the method will set a label to indicate the empty order.
	@param event the ActionEvent object that triggered this method call
	@throws Exception if an exception occurs while creating or showing the new window
	*/
	@FXML
    void PressPayAsMember(ActionEvent event) throws Exception {
		if (currentOrderProducts.size()>0) {
		timer.cancel();
		isDeferdPayment = true;
		updateStock();
		if (method == null) {
			method = "local";
		}
		addResrvationToDB();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		OrderAcceptedController oac = new OrderAcceptedController();
		OrderAcceptedController.setMethodForAccepted(method);
		OrderAcceptedController.ekOrOl(ekOrOLFromConnect);
		OrderAcceptedController.getUserData(arrFromServerRet);
		oac.start(primaryStage);}else {
			outOfStockLabel.setText("empty order!");
			}
			
    }

	  

	/**

	This method updates the stock of products that were ordered in the catalog by sending a request to the server.

	@throws IOException if there is an error connecting to the server

	@return a String representing the answer for the purchase confirmation
	*/
	private String updateStock() throws IOException {
		ArrayList<String> updateDbStock = new ArrayList<>();
		for(productInOrder prod:currentOrderProducts) {
			updateDbStock.add(prod.getName());
			updateDbStock.add(prod.getAmountString());
		}
		updateDbStock.add(0, "updateStockFromCatalogOrder");
		updateDbStock.add(1, machineName);
		
		ClientUI.chat.accept(updateDbStock);
		String answerForRet = confirmedPurchese;
		return answerForRet;
	}
	
	


	/**
	 * Sets the chosen product to display on the left side of the screen.
	 * 
	 * @param product The product to display.
	 */
	/*This method will set the chosen product to display on the left side of the screen by updating
	 *  the product name, cost, code, amount, and image of the product, and also it will check if 
	 *  the product already in the currentOrderProduct list and update the amount of this product if 
	 *  it exist in the list.
	 */
	
	private List<Product> getData() {
		
		List<Product> products = new ArrayList<>();
		Product product;
		double price;
		ArrayList<String> productsFromDB = productsFromDBMyFile.get(0).getData();
		discountLevel = Integer.parseInt(productsFromDB.get(0));
		Double areaDiscount = Double.parseDouble(productsFromDB.remove(0));

		if(isAMember && discountLevel> 0) {
			
			URL url = getClass().getResource("/images/saleGif.gif");
			Image im = new Image(url.toExternalForm());
			saleImage.setImage(im);
			rightSaleLabel.setText(areaDiscount + "%");
			SaleLeft1.setText(areaDiscount + "%");
		}
		if(firstBuyDiscount) {
			URL url = getClass().getResource("/images/saleGif.gif");
			Image im = new Image(url.toExternalForm());
			saleImage.setImage(im);
			rightSaleLabel.setText("First buy 20%");
			leftSaleLabel.setText("First buy 20%");
		}
		if(firstBuyDiscount&&isAMember && discountLevel> 0) {
			URL url = getClass().getResource("/images/saleGif.gif");
			Image im = new Image(url.toExternalForm());
			
			saleImage.setImage(im);
			rightSaleLabel.setText("");
			leftSaleLabel.setText("");
			comboSaleLeft.setText(" First buy 20% \n Member Sale "+areaDiscount + "%");
			comboSaleRight.setText(" First buy 20% \n Member Sale "+areaDiscount + "%");
		}
		int j=0;
	
		for (int i = 0; i < productsFromDB.size(); i += 6) {
			 price =  Double.parseDouble(productsFromDB.get(i + 4));
			if(isAMember) {
				price = price*((100-areaDiscount)/100);
			}
			if(firstBuyDiscount) {
				price = price*0.8;
			}
			price=Math.round(price*100.0)/100.0;
			product = new Product(productsFromDB.get(i), productsFromDB.get(i + 1), productsFromDB.get(i + 2),
					productsFromDB.get(i + 3),price,
					Integer.parseInt(productsFromDB.get(i + 5)),productsFromDBMyFile.get(j++).getMybytearray());
					InputStream inputStream = new ByteArrayInputStream(product.getByteArray());
					Image image = null;
					image = new Image(inputStream);
					product.setImage(image);
					products.add(product);
		}

		return products;
	}
	

	
	/**

	This method sets the product details on the left side of the UI.
	It sets the name, cost, code, amount and image of the product.
	It also checks whether the product is already in the cart and sets the amount accordingly.
	@param product The product which is selected by the user
	*/
	private void setChosenProduct(Product product) {
		productNameLableLeft.setText(product.getName());
		productCostValueLeft.setText(product.getPrice()+"$");
		productCodeValueLeft.setText(product.getId());
		productAmountValueLefft.setText("0");
		for(int i=0;i<currentOrderProducts.size();i++) {
			if(currentOrderProducts.get(i).getName().equals(product.getName())) {
				int currentAmmountOfProductInOrder = currentOrderProducts.get(i).getAmount();
				productAmountValueLefft.setText(String.valueOf(currentAmmountOfProductInOrder));
				break;
			}
		}
		// Create an input stream from the product image data
	    InputStream inputStream = new ByteArrayInputStream(product.getByteArray());///!!!!!!///
	    Image image = null;///!!!!!!///
	    image = new Image(inputStream);///!!!!!!///
	    product.setImage(image);///!!!!!!///
		outOfStockLabel.setText("");
		//image = new Image(getClass().getResourceAsStream(product.getImgSrc()));
		productLeftPicture.setImage(image);
		chosenProductCard
				.setStyle("-fx-background-color: #" + product.getColor() + ";\n" + "    -fx-background-radius: 30;");
	}
	/**

	The initialize method is used to initialize the values and components of the scene.
	It checks if the user is an EK or OL user, and sets the visibility of certain components accordingly.
	A timer is scheduled and a check is performed to see if the user is a first time buyer or not.
	The products are loaded and the first product is set as the chosen product.
	The products are added to the grid using a loop and FXML loading.
	If the user is not a member, the option to pay as a member is hidden.
	*/
 
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(ekOrOLFromConnect.get(0).equals("EK")) {
			ol1.setVisible(false);
			ol2.setVisible(false);
			ek.setVisible(true);
		}
		else {
			ol1.setVisible(true);
			ol2.setVisible(true);
			ek.setVisible(false);
		}
		timer.schedule(task, 1000000);
		if(isAMember) {
			ArrayList<String> msg = new ArrayList<>();
			msg.add(0, "checkMemberFirstBuy");
			try {
				ClientUI.chat.accept(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isfirst.equals("FirstBuy")) {
				firstBuyDiscount=true;
			}
			else {
				firstBuyDiscount=false;
			}
		}
		outOfStockLabel.setText("");
		products.addAll(getData());
		
		if (products.size() > 0) {
			setChosenProduct(products.get(0));
			myListener = new MyListener() {
				@Override
				public void onClickListener(Product product) {
					setChosenProduct(product);
				}
			};
		}
		int column = 0;
		int row = 1;
		try {
			for (int i = 0; i < products.size(); i++) {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/gui/item.fxml"));
				
				AnchorPane anchorPane = fxmlLoader.load();

				ItemController itemController = fxmlLoader.getController();
				itemController.setData(products.get(i), myListener);

				if (column == 3) {
					column = 0;
					row++;
				}

				grid.add(anchorPane, column++, row); // (child,column,row)
				// set grid width
				grid.setMinWidth(Region.USE_COMPUTED_SIZE);
				grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
				grid.setMaxWidth(Region.USE_PREF_SIZE);

				// set grid height
				grid.setMinHeight(Region.USE_COMPUTED_SIZE);
				grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
				grid.setMaxHeight(Region.USE_PREF_SIZE);

				GridPane.setMargin(anchorPane, new Insets(10));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!isAMember) {
			PayAsMember.setVisible(false);
		}
		
		
		
	}

	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/EKrutFinalMarket.fxml"));

		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});

		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMouseDragged(event1 -> {
			primaryStage.setX(event1.getScreenX() - xoffset);
			primaryStage.setY(event1.getScreenY() - yoffset);
		});
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	
	/**

	The pressBack method is used to handle the back button press event. It takes 
	an ActionEvent event as a parameter and returns nothing.
	This method is responsible for redirecting the user to the appropriate page based on their role.
	It cancels the current timer, hides the current window, and opens a new one based on the user's role.
	@param event ActionEvent object representing the button press event
	@throws Exception when an error occurs while loading the FXML file or opening a new window
	*/
	@FXML
	void pressBack(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		timer.cancel();
		String role = arrFromServerRet.get(2);
		switch(role) {
		case "Customer":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmc = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet);
			cmc.start(primaryStage);	
			break;
	
		
		case "User":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmcu = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet);
			cmcu.start(primaryStage);	
			break;
		
		
			
			case "MarketingAreaWorker":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
				MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
				MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet);
				
				mawhpc.start(primaryStage);
				break;
			case "DeliveryEmployee":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
				DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
				DeliveryEmployeeHomePageController.getUserData(arrFromServerRet);//Details about user
				dehpc.start(primaryStage);
				break;
			
		case "ServiceRepresentative":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
			ServiceRepresentativeHomePageController.getUserData(arrFromServerRet);
			ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
			srhpc.start(primaryStage);
			break;
			/////////////////////////////////////////////////////////////////////
		case "MarketingManager":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
			MarketingManagerHomePageController.getUserData(arrFromServerRet);
			MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			mmhpc.start(primaryStage);
			break;
			
		case "AreaManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
			AreaManagerHomePageController.detailsOnUser(arrFromServerRet);
			AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			amgpc.start(primaryStage);
			break;
			
		case "OperationWorker":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
			OperationWorkerHomePageController.detailsOnUser(arrFromServerRet);
			OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
			owhpc.start(primaryStage);
			break;
			
		case "Ceo":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CeoHomePageController chpc = new CeoHomePageController();
			CeoHomePageController.detailsOnUser(arrFromServerRet);
			CeoHomePageController.ekOrOl(ekOrOLFromConnect);
			chpc.start(primaryStage);
			break;	
			default:
				
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				primaryStage = new Stage();
				primaryStage.initStyle(StageStyle.UNDECORATED);
				CustomerMainScreenController cmsc = new CustomerMainScreenController();
				CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
				cmsc.start(primaryStage);
				break;
		}
		
		
	}

	/**

	This method is responsible for handling the cancel order action.
	It cancels the current order by removing all the products that were added to the cart and returning to the main screen.
	@param event This event is triggered when the user clicks on the cancel order button.
	@throws Exception An exception is thrown in case of an error during the process.
	*/
	@FXML
	void presscancelOrder(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		ArrayList<productInOrder> currentOrderProductsTemp = new ArrayList<>(currentOrderProducts);
		timer.cancel();
		for (productInOrder prod : currentOrderProductsTemp) {
			currentOrderProducts.remove(prod);
				gridCart.getChildren().remove(itemAndAnchorMap.get(prod.getName()));	
		}
	
		String role = arrFromServerRet.get(2);
		switch(role) {
		
		case "Customer":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmc = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet);
			cmc.start(primaryStage);	
			break;
	
		
		case "User":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmcu = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet);
			cmcu.start(primaryStage);	
			break;
		
		
			
			case "MarketingAreaWorker":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
				MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
				MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet);
				
				mawhpc.start(primaryStage);
				break;
			case "DeliveryEmployee":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
				DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
				DeliveryEmployeeHomePageController.getUserData(arrFromServerRet);//Details about user
				dehpc.start(primaryStage);
				break;
			
		case "ServiceRepresentative":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
			ServiceRepresentativeHomePageController.getUserData(arrFromServerRet);
			ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
			srhpc.start(primaryStage);
			break;
			/////////////////////////////////////////////////////////////////////
		case "MarketingManager":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
			MarketingManagerHomePageController.getUserData(arrFromServerRet);
			MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			mmhpc.start(primaryStage);
			break;
			
		case "AreaManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
			AreaManagerHomePageController.detailsOnUser(arrFromServerRet);
			AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			amgpc.start(primaryStage);
			break;
			
		case "OperationWorker":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
			OperationWorkerHomePageController.detailsOnUser(arrFromServerRet);
			OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
			owhpc.start(primaryStage);
			break;
			
		case "Ceo":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CeoHomePageController chpc = new CeoHomePageController();
			CeoHomePageController.detailsOnUser(arrFromServerRet);
			CeoHomePageController.ekOrOl(ekOrOLFromConnect);
			chpc.start(primaryStage);
			break;	
			default:
				
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				primaryStage = new Stage();
				primaryStage.initStyle(StageStyle.UNDECORATED);
				CustomerMainScreenController cmsc = new CustomerMainScreenController();
				CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
				cmsc.start(primaryStage);
				break;
		}
		
	}

	/**
	* The 'pressExitBTN' method is used to handle the press action of the 'exitBTN' button.
	*
	* @param event - the event that triggered the method call
	*/
	@FXML
	void pressedExit(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
	}

	/**

	pressminosBtnLeft method is a event handler for the minos button on the left side of the product
	when the button is pressed it will check the stock of the product and increment the amount value by 1
	if the stock is 0 it will display a message that the product is out of stock.
	@param event the button press event
	*/
	@FXML
	void pressminosBtnLeft(ActionEvent event) {
		outOfStockLabel.setText("");
		if(Integer.parseInt(productAmountValueLefft.getText())>0 ) {
			productAmountValueLefft.setText(String.valueOf(Integer.parseInt(productAmountValueLefft.getText())-1) );
		}
		
	}
	
	/**

	pressplusBtnLeft method is a event handler for the plus button on the left side of the product
	when the button is pressed it will check the stock of the product and increment the amount value by 1
	if the stock is 0 it will display a message that the product is out of stock.
	@param event the button press event
	*/

	@FXML
	void pressplusBtnLeft(ActionEvent event) {
		outOfStockLabel.setText("");
		String productName=productNameLableLeft.getText();
		int stockProduct=0;
		for(int i=0;i<products.size();i++) {
			if(products.get(i).getName().equals(productName)) {
				stockProduct=products.get(i).getStock();
			}
		}
		if(Integer.parseInt(productAmountValueLefft.getText())<stockProduct ) {
			productAmountValueLefft.setText(String.valueOf(Integer.parseInt(productAmountValueLefft.getText())+1) );
		}else {
			outOfStockLabel.setText("Out Of Stock!");
		}
		
	}
	
	
	
	/**
	 * This method handles the press of the "add to cart" button.
	 * It adds the selected product and its chosen amount to the current order, 
	 * and also updates the item in cart in case of this product already added
	 * 
	 * @param event the event of the button press
	 * @throws IOException if there is an error loading the FXML file for the item in cart
	 */
	@FXML
	  void pressaddToCartBTN(ActionEvent event) throws IOException {
		

		int column = 0;
		double productPrice = 0;
		String productName = productNameLableLeft.getText();
		String chosenAmount = productAmountValueLefft.getText();
		int chosenAmountInt = Integer.parseInt(chosenAmount);
		boolean isIn = false;

		if (Integer.parseInt(productAmountValueLefft.getText()) > 0) {

			for (productInOrder prod : currentOrderProducts) {
				if (prod.getName().equals(productName)) {
					prod.setAmount(chosenAmountInt);
					itemAndControllerMap.get(productName).updateData(chosenAmountInt);
					isIn = true;
					break;
				}
			}
			/**
			 * Removing the product from current order and also from the cart
			 */

			if (!isIn) {
				for (int i = 0; i < products.size(); i++) {
					if (products.get(i).getName().equals(productName)) {
						productPrice = products.get(i).getPrice();
						productInOrder productInOrderCur = new productInOrder(productName, chosenAmountInt,
								productPrice);
						currentOrderProducts.add(productInOrderCur);
						/**
						 *  Loading the FXML file for the item in cart and 
						 *  removing the item from the cart
						 */
						try {
							FXMLLoader fxmlLoader = new FXMLLoader();
							fxmlLoader.setLocation(getClass().getResource("/gui/itemInCart.fxml"));
							AnchorPane anchorPane = fxmlLoader.load();

							ItemCartController itemController = fxmlLoader.getController();
							itemAndControllerMap.put(productName, itemController);
							itemController.setData(products.get(i), chosenAmountInt);
							itemAndAnchorMap.put(productName, anchorPane);
							gridCart.add(anchorPane, column, row); // (child,column,row)
							// set grid width
							gridCart.setMinWidth(Region.USE_COMPUTED_SIZE);
							gridCart.setPrefWidth(Region.USE_COMPUTED_SIZE);
							gridCart.setMaxWidth(Region.USE_PREF_SIZE);

							// set grid height
							gridCart.setMinHeight(Region.USE_COMPUTED_SIZE);
							gridCart.setPrefHeight(Region.USE_COMPUTED_SIZE);
							gridCart.setMaxHeight(Region.USE_PREF_SIZE);
							GridPane.setMargin(anchorPane, new Insets(10));
							row++;

						} catch (IOException e) {
							e.printStackTrace();
						}

						break;
					}
				}

			}
			outOfStockLabel.setText("Product Added To Cart");
		} else {
			for (productInOrder prod : currentOrderProducts) {
				if (prod.getName().equals(productName)) {
					isIn = true;
					if (isIn) {
						currentOrderProducts.remove(prod);
						
						try {
							FXMLLoader fxmlLoader = new FXMLLoader();
							fxmlLoader.setLocation(getClass().getResource("/gui/itemInCart.fxml"));
							AnchorPane anchorPane = fxmlLoader.load();

							gridCart.getChildren().remove(itemAndAnchorMap.get(prod.getName()));
							// set grid width
							gridCart.setMinWidth(Region.USE_COMPUTED_SIZE);
							gridCart.setPrefWidth(Region.USE_COMPUTED_SIZE);
							gridCart.setMaxWidth(Region.USE_PREF_SIZE);

							// set grid height
							gridCart.setMinHeight(Region.USE_COMPUTED_SIZE);
							gridCart.setPrefHeight(Region.USE_COMPUTED_SIZE);
							gridCart.setMaxHeight(Region.USE_PREF_SIZE);
							GridPane.setMargin(anchorPane, new Insets(10));

						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					break;
				}
			}

		}
		totalPrice = 0;

		for (productInOrder prod : currentOrderProducts) {
			totalPrice += prod.getAmount() * prod.getPrice();

		}
		
		totalPrice=Math.round(totalPrice*100.0)/100.0;
		FinalOrderSum.setText(String.valueOf(totalPrice)+"$");
		
	    }
	
	

	

}
