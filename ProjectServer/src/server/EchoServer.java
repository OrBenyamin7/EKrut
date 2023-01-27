package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Boundry.ServerGuiController;
import common.MyFile;
import dataBase.DB_Connection;

import javafx.event.Event;
import logic.ClientConnected;

import java.sql.Connection;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer {
	
	
	
	
	// Class variables *************************************************
			static Connection conn;
			private ArrayList<ConnectionToClient> clientConnected = new ArrayList<>();
			private String host;
			private InetAddress ip;
			final public static int DEFAULT_PORT = 5555;
			String DBPassword;
			private Map<ConnectionToClient,String> userNamesOfUsers = new HashMap<>();
			
		//****************************************************************	
			
			
			/**
			 * Constructor , getting port and Starting the server  
			 * @param port
			 */
			public EchoServer(int port) {
				super(port);
				
				
				try {
					ip = InetAddress.getLocalHost();
					host = ip.getHostName();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
			/**
			 * function to handle client request, 
			 * getting the request to action by "msg" and getting the specific client that ask the action
			 * In the end of each action , sending info back to client message handler 
			 * @param msg
			 * @param client
			 */
			
			public void handleMessageFromClient(Object msg, ConnectionToClient client) {
				try {
					@SuppressWarnings("unchecked")
					ArrayList<String> data = (ArrayList<String>) msg;
					String userName;
					ArrayList<String> back = new ArrayList<>();
					back.clear();
					if(data.size()>0) {
						String action = data.get(0);
						switch (action) {
						case "AddNewSaleFromMarketingManager":
							System.out.println("Message received: " + msg + " from " + client);
							DB_Connection.addNewSaleFromMarketingManager(data);
							data.clear();
							data.add(0,"ChangeStatusOfOrDeleteSale");
							client.sendToClient(data);
							data.clear();
							break;
						case "updateReservationStatusClose":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.updateReservationClose(data);
							data.clear();
							data.add(0,"updateReservationStatusClose");
							client.sendToClient(data);
							data.clear();
							break;
						case "ChangeStatusOfOrDeleteSale":
							System.out.println("Message received: " + msg + " from " + client);
							DB_Connection.updateSaleActivision(data);
							data.clear();
							data.add(0,"ChangeStatusOfOrDeleteSale");
							client.sendToClient(data);
							data.clear();
							break;
						
						case "getSalesFromDbToMarketingManager":
							System.out.println("Message received: " + msg + " from " + client);
							data.add(DB_Connection.getSalesFromDb());
							data.add(0,"getSalesFromDbToMarketingManager");
							client.sendToClient(data);
							data.clear();
							break;
						case "getSalesFromDbToMarketingWorker":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							String DBret=(DB_Connection.getSalesToWorkerFromDb(data));
							data.remove(0);
							data.add(DBret);
							data.add(0,"getSalesFromDbToMarketingWorker");
							client.sendToClient(data);
							data.clear();
							break;
						case "updateSaleOnMechines":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.updateSaleOnMechines(data);
							data.clear();
							data.add(0,"updateSaleOnMechines");
							client.sendToClient(data);
							data.clear();
							break;
						case "updateResDeliveryTaken":
							
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.updateReservation(data);
							data.clear();
							data.add(0,"updateResDeliveryTaken");
							client.sendToClient(data);
							data.clear();
							break;

						
						case "updateStockFromCatalogOrder":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							String answer = DB_Connection.setStockFromOrder(data);
							
							data.clear();
							data.add("updateStockFromCatalogOrder");
							data.add(answer);
							client.sendToClient(data);
							data.clear();
							break;
						case "getdataforcatalog":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);					
							ArrayList<MyFile> ret=new ArrayList<MyFile>();
							ret = DB_Connection.getcatalog(data,ret);
							client.sendToClient(ret);
								
							break;
						case "getDataForCatalogEK":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);					
							ArrayList<MyFile> ret1=new ArrayList<MyFile>();
							DB_Connection.getcatalogEK(data,ret1);							
							client.sendToClient(ret1);	
							break;
							
						case "quit":
								System.out.println("Message received: " + msg + " from " + client);
								data.remove(0);
								userName = userNamesOfUsers.get(client);
								userNamesOfUsers.remove(client);
								DB_Connection.signOutUser(userName);
								clientDisconnected(client);
								back.add("quit");
								client.sendToClient(back);
								data.clear();
								
								break;
						case "getPickUpOrder":
							System.out.println("Message received: " + msg + " from " + client);
							System.out.println("arrive");
							data.remove(0);
							userName = userNamesOfUsers.get(client);
							data.add(0, userName);
							ArrayList<String> retValFromDb = DB_Connection.getPickUpOrderDB(data);
							retValFromDb.add(0,"getPickUpOrder");
							System.out.println(retValFromDb);
							client.sendToClient(retValFromDb);
							data.clear();
							break;
						case "getdelorders":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							userName = userNamesOfUsers.get(client);
							ArrayList<String> retdelordersFromDb = DB_Connection.getdelOrdersDB(userName);
							retdelordersFromDb.add(0,"getdelorders");
							System.out.println(retdelordersFromDb);
							client.sendToClient(retdelordersFromDb);
							data.clear();
							break;
						case "getAreaAndMachimes":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> retValFromDbForMachines = DB_Connection.getAreaAndMachinesNames();
							retValFromDbForMachines.add(0,"getAreaAndMachimes");
							client.sendToClient(retValFromDbForMachines);
							data.clear();
							break;
							
						case "UserNameAndPasswordCheck":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> userNameAndPasswordRetVal = DB_Connection.checkUserNameAndPassword(data);
							if(!data.get(0).equals("Error")) {
								userNamesOfUsers.put(client, data.get(0));
							}
							userNameAndPasswordRetVal.add(0,"userNameAndPasswordRetVal");
							client.sendToClient(userNameAndPasswordRetVal);
							data.clear();
							break;
						case "SignOut":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							userName = userNamesOfUsers.get(client);
							userNamesOfUsers.remove(client);
							DB_Connection.signOutUser(userName);
							back.add("SignOut");
							client.sendToClient(back);
							data.clear();
							break;
						case "getUserDataCustomer":
							userName = userNamesOfUsers.get(client);
							ArrayList<String> userData = DB_Connection.getUserData(userName);
							userData.add(0,"getUserDataCustomer");
							client.sendToClient(userData);
							data.clear();
							break;
						case "getUserDataServiceRep":
							userName = userNamesOfUsers.get(client);
							ArrayList<String> userData1 = DB_Connection.getUserData(userName);
							userData1.add(0,"getUserDataServiceRep");
							client.sendToClient(userData1);
							data.clear();
							break;
						
						case "getUserNameDetails":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> detailsOfUser = DB_Connection.getDetailsOfUser(data);
							detailsOfUser.add(0,"getUserNameDetails");
							client.sendToClient(detailsOfUser);
							data.clear();
							break;
							
					
						case "addResrvation":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							data.add(0,userNamesOfUsers.get(client));
							ArrayList<String> orderNumber=DB_Connection.addResrvation(data);
							orderNumber.add(0,"addResrvation");
							client.sendToClient(orderNumber);
							data.clear();
							break;
						case "Fastconnect":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							userNamesOfUsers.put(client, data.get(0));
							data.add(0,userNamesOfUsers.get(client));
							ArrayList<String> userNameAndPasswordRetVal1 = DB_Connection.checkUserNameforfast(data);
							userNameAndPasswordRetVal1.add(0,"Fastconnect");
							client.sendToClient(userNameAndPasswordRetVal1);
							data.clear();
							break;	
						case "checkMemberFirstBuy":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							data.add(userNamesOfUsers.get(client));
							ArrayList<String> isFirstBuy=DB_Connection.checkMemberFirstBuy(data);
							isFirstBuy.add(0,"checkMemberFirstBuy");
							client.sendToClient(isFirstBuy);
							data.clear();
							break;
						
							
							
						
						case "getUserDataAreaManager": //for area manager screen home page
							userName = userNamesOfUsers.get(client);
							ArrayList<String> areaManagerData = DB_Connection.getUserData(userName);
							areaManagerData.add(0,"getUserDataAreaManager");
							client.sendToClient(areaManagerData);
							data.clear();
							break;
						case "getMachineName": //to get machine name for specific area
							userName = userNamesOfUsers.get(client);
							ArrayList<String> machineNumberData = DB_Connection.getMachineData(userName);
							machineNumberData.add(0,"getMachineName");
							client.sendToClient(machineNumberData);
							data.clear();
							break;
						case "getMachineNameForSetMinimum": //to get machine number for specific area
							userName = userNamesOfUsers.get(client);
							ArrayList<String> machineData = DB_Connection.getMachineData(userName);
							machineData.add(0,"getMachineNameForSetMinimum");
							client.sendToClient(machineData);
							data.clear();
							break;
						case "getOrderReportDetails": //get order report details
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> orderReportData = DB_Connection.getOrderReportData(data);
							
							orderReportData.add(0,"getOrderReportDetails");
							client.sendToClient(orderReportData);
							data.clear();
							break;
						case "getCustomersReportDetails": //get customer report details
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> costumerReportData = DB_Connection.getCustomersReportData(data);
							
							costumerReportData.add(0,"getCustomersReportDetails");
							client.sendToClient(costumerReportData);
							data.clear();
							break;
						case "getInventoryReportDetails": //get inventory report details
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> inventoryReportData = DB_Connection.getInventoryReportData(data);
							
							inventoryReportData.add(0,"getInventoryReportDetails");
							client.sendToClient(inventoryReportData);
							data.clear();
							break;
						case "setMinimumLevelMachineDetails": //set machine's minimum level 
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> setMinimumLevelMachineData = DB_Connection.setMinimumLevelMachineData(data);
							setMinimumLevelMachineData.add(0,"getMinimumLevelMachineDetails");
							
							client.sendToClient(setMinimumLevelMachineData);
							data.clear();
							break;
						case "getUpdateInfoForMA": //get information for area manager home screen
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> getUpdateInfoForMAData = DB_Connection.getUpdateInfoForMA(data);
							getUpdateInfoForMAData.add(0,"getUpdateInfoForMADetails");
							
							client.sendToClient(getUpdateInfoForMAData);
							data.clear();
							break;
							
						case "getOperationWorkersName": //get machine's minimum level details
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> getOperationWorkersNameData = DB_Connection.getOperationWorkersName(data);
							getOperationWorkersNameData.add(0,"getOperationWorkersNameDetails");

							client.sendToClient(getOperationWorkersNameData);
							data.clear();
							break;
						case "sendExecutionOrder": //sending execution order
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> sendExecutionOrderStatusSend = DB_Connection.sendExecutionOrder(data);
							sendExecutionOrderStatusSend.add(0,"sendExecutionOrder");

							client.sendToClient(sendExecutionOrderStatusSend);
							data.clear();
							break;
							
						case "getNotApproveCustomers": //get all customers with NotApproved status
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> notApprovedCustomers = DB_Connection.getNotApprovedCustomers(data);
							notApprovedCustomers.add(0,"getNotApproveCustomers");

							client.sendToClient(notApprovedCustomers);
							data.clear();
							break;
							
						case "updateCustomerStatus": //update customer status
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> feedBackUpdateCustomerStatus = DB_Connection.updateCustomerStatus(data);
							feedBackUpdateCustomerStatus.add(0,"getInfoForApproveCustomersUpdate");

							client.sendToClient(feedBackUpdateCustomerStatus);
							data.clear();
							break;
							
							
							
							
						case "AddUserAsCustomerOrAMember":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.addUserToCustomerTable(data);
							data.add(0,"writeCustomerAsAMember");
							client.sendToClient(data);
							data.clear();
							
							break;
						case "writeCustomerAsAMember":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.setCustomerAsMemer(data.get(0));
							data.add(0,"writeCustomerAsAMember");
							client.sendToClient(data);
							data.clear();
							break;
							
							

						case "getUserDataOperationWorker": //getting the information about operation worker
							userName = userNamesOfUsers.get(client);
							ArrayList<String> operationWorkerData = DB_Connection.getOperationWorkerData(userName);
							operationWorkerData.add(0,"getUserDataOperationWorker");
							client.sendToClient(operationWorkerData);
							data.clear();
							break;
						case "getMachineNameForSelectedArea": //getting machine names for operation worker
							userName = userNamesOfUsers.get(client);
							ArrayList<String> operationWorkerMachinesData = DB_Connection.getOperationWorkerData(data);
							operationWorkerMachinesData.add(0,"getMachineNameForSelectedArea");
							client.sendToClient(operationWorkerMachinesData);
							data.clear();
							break;
							
						case "getStockForOperationWorker": //getting the stock for specific machine for operation worker
							userName = userNamesOfUsers.get(client);
							ArrayList<String> stockArray = DB_Connection.getStockForOperationWorker(data);
							stockArray.add(0,"getStockForOperationWorker");
							client.sendToClient(stockArray);
							data.clear();
							break;
							
						case "updateOperationWorkerMessageStatus": //getting the messages for operation worker
							userName = userNamesOfUsers.get(client);
							ArrayList<String> updateMessageStatus = DB_Connection.updateMessageStatusOP(data);
							updateMessageStatus.add(0,"updateOperationWorkerMessageStatus");
							client.sendToClient(updateMessageStatus);
							data.clear();
							break;
							
						case "updateStockByOperationWorker": //for updating stock by operation worker
							userName = userNamesOfUsers.get(client);
							ArrayList<String> updateStockStatus = DB_Connection.updateStockByOperationWorker(data);
							updateStockStatus.add(0,"updateOperationWorkerStockStatus");
							client.sendToClient(updateStockStatus);
							data.clear();
							break;
							
						case "getUserDataCeo": //for ceo screen home page
							userName = userNamesOfUsers.get(client);
							ArrayList<String> ceoData = DB_Connection.getUserData(userName);
							ceoData.add(0,"getUserDataCeo");
							client.sendToClient(ceoData);
							data.clear();
							break;
							
						case "getMachineNameForSelectedAreaCeo": //for ceo report view screen
							userName = userNamesOfUsers.get(client);
							ArrayList<String> ceoMachinesData = DB_Connection.getOperationWorkerData(data);
							ceoMachinesData.add(0,"getMachineNameForSelectedAreaCeo");
							client.sendToClient(ceoMachinesData);
							data.clear();
							break;
							
						case "getOrderReportDetailsForCeo": //get order report details for ceo
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> orderReportDataCeo = DB_Connection.getOrderReportData(data);
							
							orderReportDataCeo.add(0,"getOrderReportDetailsForCeo");
							client.sendToClient(orderReportDataCeo);
							data.clear();
							break;
						case "getCustomersReportDetailsForCeo": //get customer report details for ceo
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> costumerReportDataCeo = DB_Connection.getCustomersReportData(data);
							
							costumerReportDataCeo.add(0,"getCustomersReportDetailsForCeo");
							client.sendToClient(costumerReportDataCeo);
							data.clear();
							break;
						case "getInventoryReportDetailsForCeo": //get inventory report details for ceo
							userName = userNamesOfUsers.get(client);
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> inventoryReportDataCeo = DB_Connection.getInventoryReportData(data);
							
							inventoryReportDataCeo.add(0,"getInventoryReportDetailsForCeo");
							client.sendToClient(inventoryReportDataCeo);
							data.clear();
							break;
						case "GetMessagesForAllCustomers":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							ArrayList<String> messagesFromDb = DB_Connection.getMessages(data);
							messagesFromDb.add(0,"GetMessagesForAllCustomers");
							client.sendToClient(messagesFromDb);
							data.clear();
							break;
							
						case "updateApproveStatusCalcNotifyDelLine":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.updateApproveStatusCalcNotifyDelLine(data); //orderNumber, estimatedDelDetails.get(0), estimatedDelDetails.get(1)
							DB_Connection.addNewMessageToCustomre(data);
							data.clear();
							data.add(0,"updateApproveStatusCalcNotifyDelLine");
							client.sendToClient(data);
							data.clear();
							break;
					 
	
							
						case "getdelToWorkerFromDbForTable":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							String DBdelret1=(DB_Connection.getdelToWorkerFromDbForTable(data));
							data.remove(0);
							data.add(DBdelret1);
							data.add(0,"getdelToWorkerFromDbForTable");
							client.sendToClient(data);
							data.clear();
							break;
							
							
							
						case "setDataAfterRequestApproveDelivery":
							System.out.println("Message received: " + msg + " from " + client);
							data.remove(0);
							DB_Connection.updatedelToWorkerFromDb(data.get(0));
							data.clear();
							break;
							
							
							
							
						
							
						
							
							
						default:
							throw new Exception("Invalid operation");
						
					}
					
					}

				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			
			
			/**
			 * getter for ip
			 */
			
			public InetAddress getIp() {
				return ip;
			}


			/**
			 * set ip 
			 * @param ip
			 */
			public void setIp(InetAddress ip) {
				this.ip = ip;
			}

			/**
			 * getter for host
			 */
			public String getHost() {
				return host;
			}
			
			/**
			 * set host 
			 * @param host
			 */
			public void setHost(String host) {
				this.host = host;
			}

			/**
			 * Function that started when user get connected and connecting the db
			 * @param dbName
			 * @param dbUserNameRoot
			 * @param dbPassword
			 */
			public void dbStarted(String dbPassword,String dbUserNameRoot, String dbName) {
				DB_Connection.connectDB(dbPassword,dbUserNameRoot,dbName);
				DB_Connection.produceReports(); //producing reports automatically every 1st in month
			}
			
			
			//Function that start when server is starting
			@Override
			protected void serverStarted() {
				System.out.println("Server listening for connections on port " + getPort());


			}

			//Function that start when server is stop
			@Override
			protected void serverStopped() {
				System.out.println("Server has stopped listening for connections.");
			}
			
			private static Map<String,ClientConnected> currentContections = new HashMap<>();
			/**
			 * function that called each time that client connect to server , and adding it to the table view gui
			 * @param client
			 */
			@Override
		    public synchronized void clientConnected(ConnectionToClient client) {
				super.clientConnected(client);
				
				ServerUI serverUI = ServerUI.getInstance();
				if(currentContections.get(client.getInetAddress().getHostName()) == null){
					currentContections.put(client.getInetAddress().getHostName(), new ClientConnected(client.getInetAddress().getHostAddress(),client.getInetAddress().getHostName(),"Connected"));
					serverUI.tableViewList.clear();
					serverUI.tableViewList.addAll(currentContections.values());
					
				}else if(currentContections.get(client.getInetAddress().getHostName()).getStatus().equals("Disconnected")) {
					
					currentContections.get(client.getInetAddress().getHostName()).setStatus("Connected");
					serverUI.tableViewList.clear();
					
					serverUI.tableViewList.addAll(currentContections.values());
				}
			
				
		    }
			
			
			/**
			 * function that called each time that client Disconnect from server , and remove it to from table view GUI
			 * @param client
			 */
			@Override
			public synchronized void  clientDisconnected(ConnectionToClient client) {
				super.clientDisconnected(client);
				ServerUI serverUI = ServerUI.getInstance();
				(currentContections.get(client.getInetAddress().getHostName())).setStatus("Disconnected");
				
				
				serverUI.tableViewList.clear();
			
				serverUI.tableViewList.addAll(currentContections.values());
				
				
					
				
				
		    }

}