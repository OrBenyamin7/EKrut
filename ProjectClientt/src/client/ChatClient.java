package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import common.MyFile;
import gui.AreaManagerApproveRequestsController;
import gui.AreaManagerExecutionOrderController;
import gui.AreaManagerHomePageController;
import gui.AreaManagerReportViewController;
import gui.CeoHomePageController;
import gui.CeoReportViewController;
import gui.ChooseSalesMarketingManagmentPageController;
import gui.ConnectController;
import gui.CustomerMainScreenController;
import gui.DeliveryEmployeeController;
import gui.DeliveryEmployeeHomePageController;
import gui.DeliveryGetController;
import gui.EKrutFinalMarketController;
import gui.LoginScreensController;
import gui.MarketingAreaWorkerActivateSalesController;
import gui.MarketingAreaWorkerHomePageController;
import gui.MarketingManagerHomePageController;
import gui.OperationWorkerHomePageController;
import gui.OperationWorkerStatusMessageController;
import gui.OperationWorkerUpdateStockController;
import gui.OperationWorkerUpdateStockReviewController;
import gui.OrderTypeController;
import gui.PickUpOrderLocalController;
import gui.ServiceRepresentativeAddNewCustomerController;
import gui.ServiceRepresentativeHomePageController;
import gui.SetMinimumLevelController;
import logic.GetMessages;
import ocsf.client.AbstractClient;
/**
 * ChatClient is a class that connects to a server and handles messages received from that server.
 * It uses the handleMessageFromServer method to process messages received from the server and the sendToServer method to send messages to the server.
 * The class also uses different controller classes to handle the messages received from the server.
 * 
 * @author  (Kfir Hemo)
 * @version (14.1.23)
 */
public class ChatClient extends AbstractClient{
	// Class variables *************************************************
   	  ChatIF clientUI; 
   	/**
   	   *  A boolean variable that indicates if the client is waiting for a response from the server.
   	   */
	  public static boolean awaitResponse = false;
	//******************************************************************
	  
	  
	  /**
	   * Constructs an instance of the chat client.
	   *
	   * @param host The server to connect to.
	   * @param port The port number to connect on.
	   * @param clientUI The interface type variable.
	   * @throws IOException if the connection fails
	   */
		 
	  public ChatClient(String host, int port, ChatIF clientUI) 
	    throws IOException 
	  {
	    super(host, port); //Call the superclass constructor
	    this.clientUI = clientUI;
	  }

	  //Instance methods ************************************************
	  
	  
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	  public void handleMessageFromServer(Object msg) 
	  {
		  System.out.println("--> handleMessageFromServer");
		  ArrayList<MyFile> myFileList = null; //set Null arrayList for upcoming requested from server 
		  awaitResponse = false;// variable for stop the while loop
		  String action="";//variable for save the action in the switch case
		  ArrayList<String> massageFromServer = null;//set Null arrayList for upcoming requested from server 
		  if(msg instanceof ArrayList<?>)//check for which type to cast the msg
		    {
		        if(((ArrayList<?>)msg).get(0) instanceof MyFile)
		        {
		        	
		        	 myFileList = (ArrayList<MyFile>)msg;
					 action = myFileList.get(0).getData().get(0);
					 myFileList.get(0).getData().remove(0);
		        }
		        else {
					  
		        	
					  massageFromServer =(ArrayList<String>) msg;
					  action = massageFromServer.get(0);
					  massageFromServer.remove(0);
				  }
		    }
		  
		  
		  //switch case for all the actions in the system
			
		  switch(action) {
		  case "GetMessagesForAllCustomers":
			  GetMessages.getMessageDromDb(massageFromServer);
			  break;
		  case "getUserDataAreaManager": //returning data of area manager
    		  AreaManagerHomePageController.getUserData(massageFromServer); 
			  break;  
		  case "getMachineName": //returning machines name
			  AreaManagerReportViewController.getMachineData(massageFromServer);
			  break;
		  case "getMachineNameForSetMinimum": //returning machines name for updating minimum level
			  SetMinimumLevelController.getMachineDataMinimumLevel(massageFromServer);
			  break;
		  case "getOrderReportDetails": //returning order report
			  AreaManagerReportViewController.getOrderReportData(massageFromServer);
			  break;
		  case "getCustomersReportDetails": //returning customers report
			  AreaManagerReportViewController.getCustomersReportData(massageFromServer);
			  break;
		  case "getInventoryReportDetails": //returning inventory report
			  AreaManagerReportViewController.getInventoryReportData(massageFromServer);
			  break;
		  case "getMinimumLevelMachineDetails": //setting minimum level machine level
			  SetMinimumLevelController.getMachineUpdateMinimumLevelData(massageFromServer);
			  break;
		  case "getUpdateInfoForMADetails":  //inventory update for area manager
			  AreaManagerHomePageController.getInventoryUpdateData(massageFromServer);
			  break;
		  case "getOperationWorkersNameDetails": //getting operation worker name
			  AreaManagerExecutionOrderController.getOperationWorkersName(massageFromServer);
			  break;
		  case "sendExecutionOrder": //sending execution order
			  AreaManagerExecutionOrderController.getInfoForSendingExecutionOrder(massageFromServer);
			  break;
		  case "getNotApproveCustomers": //returning not approve customers
			  AreaManagerApproveRequestsController.getNotApproveCustomersData(massageFromServer);
			  break;
		  case "getInfoForApproveCustomersUpdate": //return message after approving customers
			  AreaManagerApproveRequestsController.getInfoForApproveCustomersStatus(massageFromServer);
			  break;
		  case "writeCustomerAsAMember":
			  break;

 		  case "getUserDataOperationWorker": //getting data for operation worker
			  OperationWorkerHomePageController.getUserData(massageFromServer); 
			  break;
		  case "getMachineNameForSelectedArea": //getting machines name for selected area
			  OperationWorkerUpdateStockReviewController.getMachinesData(massageFromServer); 
			  break; 
			  
		  case "getStockForOperationWorker": //getting stock for operation worker
			  OperationWorkerUpdateStockController.getStock(massageFromServer); 
			  break;
		  case "updateOperationWorkerMessageStatus": //getting messages for operation worker
			  OperationWorkerStatusMessageController.getUpdateFromDBAboutMessage(massageFromServer); 
			  break;
		  case "updateOperationWorkerStockStatus": //returning status of stock message
			  OperationWorkerUpdateStockController.getUpdateFromDBAboutStock(massageFromServer); 
			  break;
			  
		  case "getUserDataCeo": //case for getting ceo details
			  CeoHomePageController.getUserData(massageFromServer); 
			  break;  
			  
		  case "getMachineNameForSelectedAreaCeo": //case for ceo getting machine
			  CeoReportViewController.getMachinesNames(massageFromServer); 
			  break; 
		  case "getOrderReportDetailsForCeo": //case for ceo report details
			  CeoReportViewController.getOrderReportData(massageFromServer);
			  break;
		  case "getCustomersReportDetailsForCeo": //case for ceo customers details
			  CeoReportViewController.getCustomersReportData(massageFromServer);
			  break;
		  case "getInventoryReportDetailsForCeo": //case for ceo view inventory details
			  CeoReportViewController.getInventoryReportData(massageFromServer);
			  break;
		  
		  
		  
		  
		  
		  
		  
		  //case for request to update stock from catalog
		  case "updateStockFromCatalogOrder":
				 EKrutFinalMarketController.isConfirmedPurches(massageFromServer.get(0));
			  
			  break;
			  //case the here only for stopping the while loop above
		  case "ChangeStatusOfOrDeleteSale":
			  break;
			//case the here only for stopping the while loop above
		  case "updateReservationStatusClose":
			  break;
			  //sending the ret val to the sale list 
		  case "getSalesFromDbToMarketingManager":
			  ChooseSalesMarketingManagmentPageController.setSalesList(massageFromServer);
			  break;
			  //case for send the sales list to marketing area worker 
		  case "getSalesFromDbToMarketingWorker":
			  MarketingAreaWorkerActivateSalesController.setSalesList(massageFromServer);
			  break;
			  //case for sending data of users to delivery employee
		  case "getUserDataDeliveryEmployee":
			  DeliveryEmployeeController.getUserData(massageFromServer);
			  break;
		  case "getdelToWorkerFromDbForTable": //setting new del from row in db 
			  DeliveryEmployeeController.setNewDeliverysList(massageFromServer);
			  break;
			  //case for send the list back after activate sale
		  case "updateSaleOnMechines":
			  MarketingAreaWorkerActivateSalesController.setSalesList(massageFromServer);
			  break;
			  //case only for cancel the anwerResponed
		  case "updateResDeliveryTaken":
			  break;
			  //Case for sending the delivery oreders
		  case "getdelorders":
			  DeliveryGetController.setDeliverysList(massageFromServer);
			  DeliveryGetController.getDataFromDbPickUp(massageFromServer);
			  break;
			  //Case for Sending the catalog to every screen 
		  case "getDataForCatalogEK":
			  CustomerMainScreenController.getDataProductsFromDBMyFile(myFileList);
			  ServiceRepresentativeHomePageController.getDataProductsFromDBMyFile(myFileList);
			  MarketingManagerHomePageController.getDataProductsFromDBMyFile(myFileList);
			  MarketingAreaWorkerHomePageController.getDataProductsFromDBMyFile(myFileList);
			  DeliveryEmployeeHomePageController.getDataProductsFromDBMyFile(myFileList);
			  AreaManagerHomePageController.getDataProductsFromDBMyFile(myFileList);
			  OperationWorkerHomePageController.getDataProductsFromDBMyFile(myFileList);
			  CeoHomePageController.getDataProductsFromDBMyFile(myFileList);
			  break;
			  //Case for getting catalog in OL 
		  case "getdataforcatalog":
			  OrderTypeController.getDataProductsFromDB(myFileList);
			  break;
		  case "getUserDataCustomer":
			  //CustomerMainScreenController.getUserData(massageFromServer);
			  break;
		  case "getUserDataServiceRep":
			  //ServiceRepresentativeHomePageController.getUserData(massageFromServer);
			  break;
			  //Case for quit from system
		  case "quit":
			  quit();
			  break;
			  //Case to send to login screen if the username and password correct
		  case "userNameAndPasswordRetVal":
			  LoginScreensController.subscriberDetails(massageFromServer);
			  break;
			  //Case for sending username details
		  case "getUserNameDetails":
			  ServiceRepresentativeAddNewCustomerController.getUserNameData(massageFromServer);
			  break;
			  //case to sending pickup orders to the controller
		  case "getPickUpOrder":
			  PickUpOrderLocalController.getDataFromDbPickUp(massageFromServer);
			  break;
			  //case for getting all the machines to the area
		  case "getAreaAndMachimes":
			  PickUpOrderLocalController.getDataFromDbPickUp(massageFromServer);
			  OrderTypeController.getDataFromDbPickUp(massageFromServer);
			  break;
			  //case for the simulation of fast connect
		  case "Fastconnect":
			  ConnectController.subscriberDetails(massageFromServer);
			  CustomerMainScreenController.getUserData(massageFromServer);
			  break;
			  //Case of adding new reservation and sending back the order code
		  case "addResrvation":
			  EKrutFinalMarketController.setOrderCode(massageFromServer);
			  break;
			  //case for checking if its the first buying of member
		  case "checkMemberFirstBuy":
			  EKrutFinalMarketController.isFirstMemberBur(massageFromServer);
			  break;
		

			  
		  
		  
		  }
		  
		  
		  
	  }
	  
	 
	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */ 
	 @SuppressWarnings("unchecked")
	public void handleMessageFromClientUI(Object message)  
	  {
		message=(ArrayList<String>) message;
	    try
	    {
	    	
	    	openConnection();//in order to send more than one message
	       	awaitResponse = true;
	    	 
	    	sendToServer(message);
			//  wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	      clientUI.display("Could not send message to server: Terminating client."+ e);
	      quit();
	    }
	  }

	  
	  /**
	   * This method terminates the client.
	   */
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }

}
