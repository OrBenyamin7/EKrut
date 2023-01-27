package Boundry;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dataBase.DB_Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.ClientConnected;
import ocsf.server.ConnectionToClient;
import server.ServerUI;

public class ServerGuiController implements Initializable {
	// Class variables *************************************************
	@FXML
	private Button connect_btn;

	@FXML
	private TextField db_name;

	@FXML
	private PasswordField db_password;

	@FXML
	private TextField db_user_name;

	@FXML
	private Button disconnect_btn;

	@FXML
	private Button exit_btn;
	@FXML
	private TextField port ;
	@FXML
	private TextField ip = null;
	
	@FXML
	private TableView<ClientConnected> table;

	
	@FXML
	private TableColumn<ClientConnected, String> status_column;
	
	@FXML
	private TableColumn<ClientConnected, String> host_column;

	@FXML
	private TableColumn<ClientConnected, String> ip_column;
	
	@FXML
	private TextArea textarea_console;
	 @FXML
	    private Button importDataBTN;
	
	private Stage primaryStage;
	private double xoffset;
	private double yoffset;
	private ServerUI serverUI;
	private boolean isConnected = false;
	private String host;
	private InetAddress ipAddress;
	static ObservableList<ClientConnected> list = FXCollections.observableArrayList();
	static ObservableList<String> consoleText = FXCollections.observableArrayList();
	//**********************************************************8********
	
	
	
	/**
	 * function to append text for showing in console
	 * setDB info to be defaulted
	 * @param text
	 */
	
	public void appendText(String text) {
		Platform.runLater(()->textarea_console.appendText(text));
	}
	
	
	
	/**
	 * getter for listViewTable

	 */
	
	public ObservableList<ClientConnected> getList(){
		return this.list;
	}
	
	
	/**
	 * function to initiate data in table view on server GUI and starting the console view also
	 * @param url
	 * @param resources
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		importDataBTN.setDisable(true);
		status_column.setCellValueFactory(new PropertyValueFactory<ClientConnected, String>("status"));
		host_column.setCellValueFactory(new PropertyValueFactory<ClientConnected, String>("host"));
		ip_column.setCellValueFactory(new PropertyValueFactory<ClientConnected, String>("ipAddress"));
		port.setText("5555");
		serverUI = ServerUI.getInstance();
		table.setItems(list);
		db_name.setText("jdbc:mysql://localhost/project?serverTimezone=IST");
		db_user_name.setText("root");
		OutputStream out = new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				appendText(String.valueOf((char)b));
				
			}
		};
		System.setOut(new PrintStream(out,true));
		
		
		
	}
	
	@FXML
    void pressImportData(ActionEvent event) throws SQLException {
		DB_Connection.importData();
    }
	
	
	/**
	 * when connect button is pressed on server GUI , 
	 * this function will connect the server and starting the db
	 * @param event
	 */
	
	
	@FXML
	public void pressConnect(ActionEvent event) {
		if(!isConnected) {
			serverUI.runServer(port.getText());
			ipAddress = ServerUI.getIp();
			host = ServerUI.getHost();
			ip.setText(ipAddress.getHostAddress());
			isConnected = true;
			importDataBTN.setDisable(false);
		}
		serverUI.dbConnection(db_password.getText(),db_name.getText(),db_user_name.getText());
	}

	@FXML
	void pressDisconnect(ActionEvent event) throws Exception {
		//NotImplementedYet
	}

	
	@FXML
	void pressExit(ActionEvent event) {
		System.exit(0);
	}
	
	/**
	 * start function that starting the GUI page, also have a mouse moverS
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.initStyle(StageStyle.UNDECORATED);
	
		Parent root = FXMLLoader.load(getClass().getResource("/Boundry/ServerGuii.fxml"));

		//
		root.setOnMousePressed(event1 -> {
            xoffset = event1.getSceneX();
            yoffset = event1.getSceneY();
        });
	
        // event handler for when the mouse is pressed AND dragged to move the window
        root.setOnMouseDragged(event1 -> {
            primaryStage.setX(event1.getScreenX()-xoffset);
            primaryStage.setY(event1.getScreenY()-yoffset);
        });
        //
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	

	

}
