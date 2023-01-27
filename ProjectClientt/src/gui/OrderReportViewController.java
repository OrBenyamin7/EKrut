package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class for order report window
 * @author Michal and Or
 *
 */
public class OrderReportViewController implements Initializable{

    @FXML
    private Label CanceledOrdersLable;

    //@FXML
    //private Label MostPurchasedLable;

    @FXML
    private Label OrderReportLabel;

    @FXML
    private Label OrdersInTotalLable;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView canceledOrderImg;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private ImageView managmentAreaImg;

    @FXML
    private Label managmentAreaLable;

    @FXML
    private ImageView mostPurchasedImg;

    @FXML
    private ImageView ordersInTotalImg;

    @FXML
    private PieChart pieChartOrder;

    @FXML
    private Label specificAreaLable;


    @FXML
    private Label specificMostPurchasedLable;

    @FXML
    private Label specificOrdersInTotalLable;
    
    @FXML
    private Label dateLabel;
    
	private double xoffset;
	private double yoffset;
	
	private static ArrayList<String> arrReportData;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	
	

	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
    public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OrderReportView.fxml"));
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
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

 	@FXML
 	/**
 	 * press back button from current window
 	 * @param event
 	 * @throws Exception
 	 */
 	void pressBackBtn(ActionEvent event) throws Exception {
 		Stage primaryStage;
 		switch(arrFromServerRet.get(2))
 		{
 			case "AreaManager":
 				ArrayList<String> msg = new ArrayList<>();
 				msg.add("getMachineName");
 				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
 				ClientUI.chat.accept(msg);
 				primaryStage = new Stage();
 				primaryStage.initStyle(StageStyle.UNDECORATED);
 				AreaManagerReportViewController amrvc = new AreaManagerReportViewController();
 				AreaManagerReportViewController.getUserData(arrFromServerRet);
 				AreaManagerReportViewController.ekOrOl(ekOrOLFromConnect);
 				amrvc.start(primaryStage);
 				break;
 			
 			case "Ceo":
 		 		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
 		 		primaryStage = new Stage();
 		 		primaryStage.initStyle(StageStyle.UNDECORATED);
 		 		CeoReportViewController crvc = new CeoReportViewController();
 		 		CeoReportViewController.getUserData(arrFromServerRet);
 		 		CeoReportViewController.ekOrOl(ekOrOLFromConnect);
 		 		crvc.start(primaryStage);
 		 		break;
 			}
 		}

	@FXML
	/**
	 * press exit button from current window
	 * @param event
	 * @throws IOException
	 */
	void pressExitBtn(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
	}
	
	/**
	 * function that get the report data from db
	 * @param massageFromServer
	 */
	public static void getOrderReportDetails(ArrayList<String> massageFromServer) {
		arrReportData = massageFromServer;
	}
	

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		int amount = 0;
		int totalOrders = 0;
		
		ArrayList<String> orderData;
		ArrayList<String> machinesOrderData = new ArrayList<>();
		
		String[] arrOfSub;

		if(arrReportData.size()>0)
		{			
			dateLabel.setText(arrReportData.get(arrReportData.size()-1) + "/" + arrReportData.get(arrReportData.size()-2));
			for(int i=0;i<arrReportData.size()-2;i++)
			{
				orderData = new ArrayList<>();
			
				arrOfSub = ((String) arrReportData.get(i)).split(" ");
			
				for(int j=0; j<arrOfSub.length; j++) 
				{
					orderData.add(arrOfSub[j]);
				}
			
				if(i==0)
					specificAreaLable.setText(orderData.get(orderData.size()-1));
				totalOrders += Integer.parseInt(orderData.get(orderData.size()-3));
				
				machinesOrderData.add(orderData.get(orderData.size()-2) +" " 
						+ orderData.get(orderData.size()-3));
			
			}
			
			
			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
			
			for(int i=0;i<machinesOrderData.size();i++)
			{
				arrOfSub = machinesOrderData.get(i).split(" ");
				pieChartData.add(new PieChart.Data(arrOfSub[0], (Double.parseDouble(arrOfSub[1])/totalOrders)));
			}
			
			pieChartOrder.setData(pieChartData);
		}
		
		specificOrdersInTotalLable.setText(Integer.toString(totalOrders));
			
		//pieChartOrder.setLegendVisible(false);
		pieChartOrder.getData().forEach(data -> 
		{
			String percent = String.format("%.2f%%", data.getPieValue()*100);
			Tooltip toolTip = new Tooltip(percent);
			Tooltip.install(data.getNode(), toolTip);
		});
		
		pieChartOrder.setTitle("Montly Orders Distribution By Machines");
	}
	
	/**
	 * function that get user data from previous window
	 * @param arrFromServer
	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
	
	
	/**
	 * function that save arrayList from server for ek or ol 
	 * @param arrFromServer
	 */
	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
}
