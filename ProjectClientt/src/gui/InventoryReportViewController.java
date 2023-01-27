package gui;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for inventory report view
 * @author michal
 *
 */
public class InventoryReportViewController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private Label inventoryReportLabel;

    @FXML
    private ImageView machineNameImg;

    @FXML
    private Label machineNameLable;

    @FXML
    private ImageView managmentAreaImg;

    @FXML
    private Label managmentAreaLable;

    @FXML
    private CategoryAxis namesOfItems;

    @FXML
    private NumberAxis rangeOfQuantity;

    @FXML
    private Label specificAreaLable;

    @FXML
    private Label specificLackAmountLabel;

    @FXML
    private Label specificMachineNameLable;

    @FXML
    private Label specificTotalAmountLabel;

    @FXML
    private StackedBarChart<String,Integer> stackedBarChart;

    @FXML
    private ImageView totalAmountImg;

    @FXML
    private Label totalAmountLable;

    @FXML
    private ImageView totalLackImg;

    @FXML
    private Label totalLackLable;
    
    @FXML
    private Label dateLabel;
    
    @FXML
    private CategoryAxis day;
    
    @FXML
    private NumberAxis amount;
    
    @FXML
    private Label specificAverageLack;
    
    @FXML
    private LineChart<String, Integer> lineChart;
    
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
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/InventoryReportView.fxml"));

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
	 * Used to retrieve inventory report details from a server
	 * @param massageFromServer ArrayList
	 */
	public static void getInventoryReportDetails(ArrayList<String> massageFromServer) {
		arrReportData = massageFromServer;
	}
	
	/**
	 * Used to initialize the data for the report view window. Displays the data on the graph in the form of a LineChart.
	 * And adds tooltips to the data points on the graph.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dateLabel.setText(arrReportData.get(arrReportData.size()-1) + "/" + arrReportData.get(arrReportData.size()-2));
		specificAverageLack.setText(new DecimalFormat("##.##").format(
				Double.parseDouble(arrReportData.get(arrReportData.size()-3))) + "%");

		specificMachineNameLable.setText(arrReportData.get(arrReportData.size()-4));
		specificAreaLable.setText(arrReportData.get(arrReportData.size()-5));
		String str = arrReportData.get(0);
		String[] arrOfItems = str.split(",");
		
		
		XYChart.Series series1 = new XYChart.Series<>();
		XYChart.Series series2 = new XYChart.Series<>();
		
		for(int i=0; i<arrOfItems.length-1; i+=3)
		{
			
			series1.getData().add(new XYChart.Data(arrOfItems[i],Integer.parseInt(arrOfItems[i+1])));
			series2.getData().add(new XYChart.Data(arrOfItems[i],Integer.parseInt(arrOfItems[i+2])));
		}
		
		amount.setLabel("Amount");
		day.setLabel("Day");
		
		
		series1.setName("Threshold");
		series2.setName("Availability");
		
		lineChart.getData().addAll(series1,series2);
		
		for(XYChart.Series<String, Integer> series : lineChart.getData())
		{
			for(XYChart.Data<String, Integer> item : series.getData())
			{
				Tooltip.install(item.getNode(), new Tooltip("" + item.getYValue()));
			}
		}
		
		lineChart.setTitle("Montly Inventory Distribution By Specific Machine");
		
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

	/**
	 * Press exit button from area manager page
	 * @param event ActionEvent
	 * @throws IOException
	 */ 
	@FXML
	void pressExitBtn(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
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
