package gui;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.scene.chart.StackedBarChart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for client histogram report view
 * @author michal
 *
 */
public class ClientHistogramReportViewController implements Initializable{

    @FXML
    private Label ClientHistogramReportLabel;

    @FXML
    private NumberAxis amountOfCostumers;
    
    @FXML
    private StackedBarChart<String,Integer> stackedBarChart;
    
    @FXML
    private Button backBtn;

    @FXML
    private BarChart clientBarChart;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private ImageView managmentAreaImg;

    @FXML
    private Label managmentAreaLable;

    @FXML
    private CategoryAxis rangeOfOrders;

    @FXML
    private Label specificAreaLable;
    
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
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/ClientHistogramReportView.fxml"));

		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});

		root.setOnMouseDragged(event1 -> {
			primaryStage.setX(event1.getScreenX() - xoffset);
			primaryStage.setY(event1.getScreenY() - yoffset);
		});
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Used to retrieve customer report details from a server
	 * @param massageFromServer ArrayList
	 */
	public static void getCustomersReportDetails(ArrayList<String> massageFromServer) {
		arrReportData = massageFromServer;
	}
	
	/**
	 * Used to initialize the data for the report view window. Displays the data on the graph in the form of a stacked bar chart.
	 * And adds tooltips to the data points on the graph.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(arrReportData.get(arrReportData.size()-1) + "/" + arrReportData.get(arrReportData.size()-2));
		specificAreaLable.setText(arrReportData.get(arrReportData.size()-3));
		amountOfCostumers.setLabel("Clients");
		rangeOfOrders.setLabel("Orders");
		String str = arrReportData.get(0);
		String[] arrOfItems = str.split(",");
		
		for(int i=0; i<arrOfItems.length-1; i+=2) { 	
			XYChart.Series<String,Integer> series = new XYChart.Series();
			XYChart.Data dataCustomers = new XYChart.Data((arrOfItems[i+1]),Integer.parseInt(arrOfItems[i]));
			series.getData().add(dataCustomers);
			series.setName("Range: " + arrOfItems[i+1]);
			stackedBarChart.getData().addAll(series);
		}
		
		for(XYChart.Series<String, Integer> series : stackedBarChart.getData())
		{
			for(XYChart.Data<String, Integer> customer : series.getData())
			{
				Tooltip.install(customer.getNode(), new Tooltip("" + customer.getYValue()));
			}
		}
		
		stackedBarChart.setTitle("Montly Histogram Distribution By Different Activity Levels");
		
	}

	/**
	 * Used to handle the action of pressing the back button. It determines the current user's role, hides the current window, and opens the appropriate report view window for the user's role.
	 * @param ActionEvent event
	 */
	@FXML
	void pressBackBtn(ActionEvent event) throws Exception {
		Stage primaryStage;
		switch(arrFromServerRet.get(2))
		{
		case "AreaManager":
			ArrayList<String> msg = new ArrayList<>();
			msg.add("getMachineName");
			((Node) event.getSource()).getScene().getWindow().hide(); 
			ClientUI.chat.accept(msg);
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			AreaManagerReportViewController amrvc = new AreaManagerReportViewController();
			AreaManagerReportViewController.getUserData(arrFromServerRet);
			AreaManagerReportViewController.ekOrOl(ekOrOLFromConnect);
			amrvc.start(primaryStage);
			break;
		
		case "Ceo":
	 		((Node) event.getSource()).getScene().getWindow().hide(); 
	 		primaryStage = new Stage();
	 		primaryStage.initStyle(StageStyle.UNDECORATED);
	 		CeoReportViewController crvc = new CeoReportViewController();
	 		CeoReportViewController.getUserData(arrFromServerRet);
		 	CeoReportViewController.ekOrOl(ekOrOLFromConnect);
	 		crvc.start(primaryStage);
	 		break;
		}
	}

	/**
	 * Press exit button from area manager page
	 * @param ActionEvent event
	 * @throws IOException
	 */ 
	@FXML
	void pressExitBtn(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); 
		ClientUI.chat.accept(msg);
		System.exit(1);
	}
	
	/**
	 * Used to retrieve the user data from the server.
	 * @param arrFromServer ArrayList
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
