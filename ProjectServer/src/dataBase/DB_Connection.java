package dataBase;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import common.MyFile;
import logic.SimulationClass;

public class DB_Connection {

	// Class variables *************************************************
	static Connection conn;

	// ******************************************************************

	/**
	 * function to connect the database according to the user info
	 * 
	 * @param password
	 * @param dbUserNameRoot
	 * @param dbName
	 */



	/**

	This method connects to a MySQL database with the provided password, username, and database name.
	@param password The password for the MySQL database.
	@param dbUserNameRoot The username for the MySQL database.
	@param dbName The name of the MySQL database to connect to.
	*/
	public static void connectDB(String password, String dbUserNameRoot, String dbName) {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost/project?serverTimezone=IST", "root", password);
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * function that get info to save in db from the user the info arriving in
	 * ArrayList
	 * 
	 * @param data
	 */
	public static ArrayList<String> getUserData(String userName) {
		ArrayList<String> userData = new ArrayList<>();
		Statement stmt;
		String datafromdb = "";

		try {

			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT userName,firstName,lastName,location FROM project.users where userName = '"
							+ userName + "';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
			}
			rs = stmt.executeQuery(
					"SELECT status ,IsAMember FROM project.customer where userName = '" + userName + "';");
			while (rs.next()) {
				datafromdb = datafromdb + " " + rs.getString(1) + " " + rs.getString(2);
			}
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");
				for (int i = 0; i < arrOfSub.length; i++) {
					userData.add(arrOfSub[i]);
				}
				
				
				
				rs.close();
			} else {
				userData.add("Error");
				rs.close();
			}
			
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userData;
	}

	



	
	
	/**

	This method adds a new reservation to the database with the provided information in the data ArrayList.
	It first generates a random order number to be used as the reservation's order number.
	Then it inserts the provided information into the reservation table in the database.
	If the deferred payment option is selected, it also updates the customer's debt in the customer table.
	@param data An ArrayList containing the following information in order:
	userName, order, price, machine, type, address, status, area, date, isDeferredPayment
	@return An ArrayList containing a single string, the order number generated for the reservation.
	@throws SQLException if a database access error occurs or this method is called on a closed connection.
*/
	public static ArrayList<String> addResrvation(ArrayList<String> data) throws SQLException {

		Random rand = new Random();
		ArrayList<String> retVal = new ArrayList<>();
		int rand_int1;
		Statement stmt;
		ResultSet rs;
		while (true) {
			rand_int1 = rand.nextInt(10000);

			String datafromdb = "";
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT orderNumber FROM project.reservation where orderNumber = '"
					+ String.valueOf(rand_int1) + "';");
			if (!rs.next()) {

				rs.close();
				break;

			}
		}
		retVal.add(String.valueOf(rand_int1));
		String[] arrOfDate = ((String) data.get(8)).split("-");
		PreparedStatement ps = conn.prepareStatement("insert into reservation values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, data.get(0));// userName
		ps.setString(2, data.get(1));// order
		ps.setString(3, data.get(2));// price
		ps.setString(4, data.get(3));// machine
		ps.setString(5, String.valueOf(rand_int1));// orderNumber
		ps.setString(6, data.get(4));// type
		ps.setString(7, data.get(5));// address
		ps.setString(8, data.get(6));// status
		ps.setString(9, data.get(7));// area
		ps.setString(10, arrOfDate[2]);// date
		ps.setString(11, arrOfDate[1]);// date
		ps.setString(12, arrOfDate[0]);// date
		ps.setString(13, "0");// area
		ps.executeUpdate();
		if( data.get(4).equals("Delivery")) {
		    ps = conn.prepareStatement("insert into project.deliveries values(?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, "NotApproved");// RequestApprove
			ps.setString(2, String.valueOf(rand_int1));// orderNumber
			ps.setString(3, "Delivery");// kindOfOrder WTF?!
			ps.setString(4, data.get(5));// address
			ps.setString(5, data.get(7));// area
			ps.setString(6, "new");// orderStatus
			ps.setString(7, "-");// address
			ps.setString(8, "NO");// status
			ps.setString(9, "NotRecived");// area
			ps.setString(10, "0");// date
			ps.executeUpdate();
		}
		
		
		
		String isDeferdPaymemt = data.get(9);

		if (isDeferdPaymemt.equals("1")) {
			rs = stmt.executeQuery("SELECT debt FROM project.customer where userName = '" + data.get(0) + "';");
			rs.next();

			Double currentDebt = Double.parseDouble(rs.getString(1));
			currentDebt += Double.parseDouble(data.get(2));
			ps = conn.prepareStatement("update customer set debt = ?  where userName = ?;");
			ps.setString(1, String.valueOf(currentDebt));
			ps.setString(2, data.get(0));
			ps.execute();
		}
		rs.close();
		ps.close();
		return retVal;
	}

	/**

	This method updates the isLoggedIn status of a user in the database to 0 (logged out) with the provided userName.
	@param data The userName of the user to be logged out
	*/
	
	public static void signOutUser(String data) {

		try {
			PreparedStatement ps = conn.prepareStatement("update users set isLoggedIn = ?  where userName = ?;");
			ps.setString(1, "0");
			ps.setString(2, data);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**

	This method checks if the provided username and password match a user in the database. If they do, it returns the user's first name, last name, role, isLoggedIn status, and location.
	If the user is also a customer, it also returns the customer's status and isAMember status.
	If the provided username and password do not match any user in the database, it returns an error message.
	@param data An ArrayList containing the following information in order:
	 userName, passWord
	@return An ArrayList containing the user's first name, last name, role, isLoggedIn status, location, customer status, and isAMember status. If the provided username and password do not match any user in the database, it returns an ArrayList containing the string "Error".
*/
	public static ArrayList<String> checkUserNameAndPassword(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> subscriber = new ArrayList<>();
		String userName = data.get(0);
		String password = data.get(1);
		String datafromdb = "";
		String[] arrOfSub = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT firstName,lastName,role,isLoggedIn,location FROM project.users where userName = '"
							+ userName + "'and password = '" + password + "';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
						+ " " + rs.getString(5);
			}
			if (datafromdb.length() > 1) {
				arrOfSub = ((String) datafromdb).split(" ");

				subscriber.add(arrOfSub[0]);
				subscriber.add(arrOfSub[1]);
				subscriber.add(arrOfSub[2]);
				subscriber.add(arrOfSub[3]);
				PreparedStatement ps = conn.prepareStatement("update users set isLoggedIn = ?  where userName = ?;");
				ps.setString(1, "1");
				ps.setString(2, userName);
				ps.executeUpdate();
				rs = stmt.executeQuery(
						"SELECT status,IsAMember FROM project.customer where userName = '" + userName + "';");
				if (rs.next()) {

					datafromdb = rs.getString(1) + " " + rs.getString(2);
					String[] arrOfSub1 = ((String) datafromdb).split(" ");
					subscriber.add(arrOfSub1[0]);
					subscriber.add(arrOfSub1[1]);
				} else {
					subscriber.add("Not A Customer");
					subscriber.add("Not A Customer");
				}
				ps.close();
				rs.close();
			} else {
				subscriber.add("Error");
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (subscriber.get(0).equals("Error") == false) {
			subscriber.add(arrOfSub[4]);
		}
		return subscriber;
	}

	
	/**

	This method checks if the provided username matches a user in the database. If it does, it returns the user's first name, last name, role, isLoggedIn status, location, customer status, and isAMember status.
	If the provided username does not match any user in the database, it returns an error message.
	@param data An ArrayList containing the following information in order:
	userName
	@return An ArrayList containing the user's first name, last name, role, isLoggedIn status, location, customer status, and isAMember status. If the provided username does not match any user in the database, it returns an ArrayList containing the string "Error".
*/	
	public static ArrayList<String> checkUserNameforfast(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> subscriber = new ArrayList<>();
		String userName = data.get(0);
		String datafromdb = "";
		boolean ismember = false;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT firstName,lastName,role,isLoggedIn,location FROM project.users where userName = '"
							+ userName + "' ;");
			rs.next();
			datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
			String location = " " + rs.getString(5);

			rs = stmt.executeQuery(
					"SELECT status,IsAMember FROM project.customer where userName = '" + userName + "' ;");
			rs.next();

			ismember = rs.getString(2).equals("1");
			datafromdb = datafromdb + " " + rs.getString(1) + " " + rs.getString(2) + " " + location;
			rs.close();

			if (datafromdb.length() > 1 && ismember) {
				String[] arrOfSub = ((String) datafromdb).split(" ");

				subscriber.add(arrOfSub[0]);
				subscriber.add(arrOfSub[1]);
				subscriber.add(arrOfSub[2]);
				subscriber.add(arrOfSub[3]);
				subscriber.add(arrOfSub[4]);
				subscriber.add(arrOfSub[5]);
				subscriber.add(arrOfSub[6]);

				PreparedStatement ps = conn.prepareStatement("update users set isLoggedIn = ?  where userName = ?;");
				ps.setString(1, "1");
				ps.setString(2, userName);
				ps.executeUpdate();
				ps.close();

			} else {
				subscriber.add("Error");
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}

	
	/**

	The getcatalogEK function retrieves the catalog of products available in a specific machine.
	It also retrieves the discount level area of the machine and the stock of each product in the machine.
	@param data The ArrayList<String> containing the machine name
	@param myFileList The ArrayList<MyFile> that will contain the data of the products and their images.
	@return myFileList The ArrayList<MyFile> containing the data of the products and their images.
	*/
	
	public static ArrayList<MyFile> getcatalogEK(ArrayList<String> data,ArrayList<MyFile> myFileList)  {
		Statement stmt;
		String dataFromDBMechine = "";
		String discountLevelArea = "0";
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"SELECT stock,discountLevelArea FROM project.machines where machineName = '" + data.get(0) + "' ;");
			rs.next();
			dataFromDBMechine = rs.getString(1) + " " + rs.getString(2);
			discountLevelArea = rs.getString(2);
			String[] arrFromMechine = ((String) dataFromDBMechine).split(" ");
			String[] stockProductAndAmountInMechine = ((String) arrFromMechine[0]).split(",");
			ArrayList<String> stockProducts = new ArrayList<>();

			for (int i = 0; i < stockProductAndAmountInMechine.length; i += 2) {
				if (Integer.parseInt(stockProductAndAmountInMechine[i + 1]) > 0) {
					stockProducts.add(stockProductAndAmountInMechine[i]);
					stockProducts.add(stockProductAndAmountInMechine[i + 1]);

				}
			}
			
			
			
			int j1 = 0;
			for (int j = 0; j < stockProducts.size(); j += 2) {
				rs = stmt.executeQuery(
						"SELECT * FROM project.productcatalog where name = '" + stockProducts.get(j) + "';");

				if (rs.next()) {
					myFileList.add(new MyFile("OldPlaceOfURL",rs.getString(2)));
					myFileList.get(0).getData().add(rs.getString(1));
					myFileList.get(0).getData().add(rs.getString(2));
					myFileList.get(0).getData().add("oldPlaceOfImageSrc");
					myFileList.get(0).getData().add(rs.getString(4));
					myFileList.get(0).getData().add(rs.getString(5));
					myFileList.get(0).getData().add(stockProducts.get(j + 1));
					Blob blob = rs.getBlob(3);
					int blobLength = (int) blob.length();  
					byte[] blobAsBytes = blob.getBytes(1, blobLength);
					myFileList.get(j1).initArray(blobLength);
					myFileList.get(j1).setSize(blobLength);
					myFileList.get(j1).setMybytearray(blobAsBytes);
					
					j1++;
				}

			}
			rs.close();
			myFileList.get(0).getData().add(0, discountLevelArea);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		myFileList.get(0).getData().add(0,"getDataForCatalogEK");
		return myFileList;
	}

	
	/**

	The getcatalog function retrieves the catalog of products available in a specific machine.
	It also retrieves the discount level area of the machine and the stock of each product in the machine.
	@param data The ArrayList<String> containing the machine name
	@param myFileList The ArrayList<MyFile> that will contain the data of the products and their images.
	@return myFileList The ArrayList<MyFile> containing the data of the products and their images.
	*/
	public static ArrayList<MyFile> getcatalog(ArrayList<String> data,ArrayList<MyFile> myFileList) {
		
		ArrayList<String> dataForCatalog = new ArrayList<>();
		Statement stmt;
		String dataFromDBMechine = "";
		String discountLevelArea = "0";
		ResultSet rs;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"SELECT stock,discountLevelArea FROM project.machines where machineName = '" + data.get(1) + "' ;");
			rs.next();
			dataFromDBMechine = rs.getString(1) + " " + rs.getString(2);
			discountLevelArea = rs.getString(2);
			String[] arrFromMechine = ((String) dataFromDBMechine).split(" ");
			String[] stockProductAndAmountInMechine = ((String) arrFromMechine[0]).split(",");
			ArrayList<String> stockProducts = new ArrayList<>();

			for (int i = 0; i < stockProductAndAmountInMechine.length; i += 2) {
				if (Integer.parseInt(stockProductAndAmountInMechine[i + 1]) > 0) {
					stockProducts.add(stockProductAndAmountInMechine[i]);
					stockProducts.add(stockProductAndAmountInMechine[i + 1]);

				}
			}
			
			
			
			int j1 = 0;
			for (int j = 0; j < stockProducts.size(); j += 2) {
				rs = stmt.executeQuery(
						"SELECT * FROM project.productcatalog where name = '" + stockProducts.get(j) + "';");

				if (rs.next()) {
					myFileList.add(new MyFile("OldPlaceOfURL",rs.getString(2)));
					myFileList.get(0).getData().add(rs.getString(1));
					myFileList.get(0).getData().add(rs.getString(2));
					myFileList.get(0).getData().add("oldPlaceOfImageSrc");
					myFileList.get(0).getData().add(rs.getString(4));
					myFileList.get(0).getData().add(rs.getString(5));
					myFileList.get(0).getData().add(stockProducts.get(j + 1));
					Blob blob = rs.getBlob(3);
					int blobLength = (int) blob.length();  
					byte[] blobAsBytes = blob.getBytes(1, blobLength);
					myFileList.get(j1).initArray(blobLength);
					myFileList.get(j1).setSize(blobLength);
					myFileList.get(j1).setMybytearray(blobAsBytes);
					
					j1++;
				}

			}
			rs.close();
			myFileList.get(0).getData().add(0, discountLevelArea);
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		myFileList.get(0).getData().add(0,"getdataforcatalog");
		return myFileList;
	}


	/**
	*

	This method is used to retrieve the order of a customer from the database for pick-up.
	@param msg - An ArrayList<String> containing the userName, orderNumber, machineName
	@return order - An ArrayList<String> containing the order details.
	*/
	public static ArrayList<String> getPickUpOrderDB(ArrayList<String> msg) {

		Statement stmt;
		ArrayList<String> order = new ArrayList<>();
		String userName = msg.get(0);
		String datafromdb = "";
		String status = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT status,orders FROM reservation WHERE userName= '" + userName
					+ "' and orderNumber = '" + msg.get(1) + "' and machineName = '" + msg.get(2) + "'  ;");

			while (rs.next()) {
				datafromdb = rs.getString(2);
				status = rs.getString(1);
			}
			if (datafromdb.length() >= 1 && status.equals("NotCompleted")) {
				order.add(datafromdb);
				PreparedStatement ps = conn
						.prepareStatement("update reservation set status = ?  where orderNumber = ?;");
				ps.setString(1, "Completed");
				ps.setString(2, msg.get(1));
				ps.executeUpdate();
				ps.close();
				rs.close();
			} else {
				order.add("Error");
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return order;
	}

	/**
	 * function that get data to extract from db and return all the requested data
	 * of user
	 * 
	 * @param data
	 */

	public static ArrayList<String> getSubscriber(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> Sub = new ArrayList<>();
		String id = data.get(0);
		String datafromdb = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM subscriber WHERE id= '" + id + "';");

			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
						+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7);
			}
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");

				Sub.add(arrOfSub[0]);
				Sub.add(arrOfSub[1]);
				Sub.add(arrOfSub[2]);
				Sub.add(arrOfSub[3]);
				Sub.add(arrOfSub[4]);
				Sub.add(arrOfSub[5]);
				Sub.add(arrOfSub[6]);

				rs.close();
			} else {
				Sub.add("Error");
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Sub;
	}

	/**
	 * function that get data to update from db and update all the requested data of
	 * user
	 * 
	 * @param data
	 */
	public static void Update(ArrayList<String> data) {

		String ID;
		String card_number;
		String sub_number;

		try {

			ID = data.get(0);

			if (data.get(1).equals("")) {
				card_number = data.get(1);
				PreparedStatement ps = conn
						.prepareStatement("update subscriber set credit_card_number = ?  where ID = ?");
				ps.setString(1, card_number);
				ps.setString(2, ID);
				ps.executeUpdate();
				ps.close();
			} else if (data.get(2).equals("")) {
				sub_number = data.get(2);
				PreparedStatement ps = conn
						.prepareStatement("update subscriber set subscriber_number = ?  where ID = ?");
				ps.setString(1, sub_number);
				ps.setString(2, ID);
				ps.executeUpdate();
				ps.close();
			} else if (!data.get(1).equals("") && !data.get(2).equals("")) {
				card_number = data.get(1);
				sub_number = data.get(2);
				PreparedStatement ps = conn.prepareStatement(
						"update subscriber set subscriber_number = ? , credit_card_number = ?  where id = ?");
				ps.setString(1, sub_number);
				ps.setString(2, card_number);
				ps.setString(3, ID);
				ps.executeUpdate();
				ps.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Used to retrieve the names of the area and machines from the database.
	@return ArrayList of Strings representing the location and machine name retrieved from the database.
	 * 
	 */
	public static ArrayList<String> getAreaAndMachinesNames() {
		Statement stmt;
		ArrayList<String> details = new ArrayList<>();

		String datafromdb = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT location,machineName  FROM machines ;");

			while (rs.next()) {
				datafromdb = datafromdb + rs.getString(1) + " " + rs.getString(2) + " ";
			}
			if (datafromdb.length() >= 1) {
				details.add(datafromdb);
				rs.close();
			} else {
				details.add("Error");
				rs.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return details;
	}

	/**
	 * Used to update the stock of a machine based on an order. Compares the items in the order with the 		items in the stock array, reducing the stock amount accordingly.
		@param data an ArrayList of Strings representing the machine name, and the items and quantities 	in the order.
	 */
	public static String setStockFromOrder(ArrayList<String> data) {
		Statement stmt;
		String datafromdb = "";
		String machineName = data.get(0);
		data.remove(0);

		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT stock  FROM machines WHERE machineName= '" + machineName + "';");

			while (rs.next()) {
				datafromdb = datafromdb + rs.getString(1);
			}
			String[] stockFromDb = ((String) datafromdb).split(",");

			for (int j = 0; j < data.size(); j += 2) {
				for (int i = 0; i < stockFromDb.length; i += 2) {
					if (stockFromDb[i].equals(data.get(j))) {
						int amountFromDb = Integer.parseInt(stockFromDb[i + 1]);
						amountFromDb -= Integer.parseInt(data.get(j + 1));
						if(amountFromDb<0) {
							amountFromDb = 0;
							return "Items Already Bought By Other Customer";
						}
						stockFromDb[i + 1] = String.valueOf(amountFromDb);
						break;
					}
				}
			}
			String stockToDb = "";
			for (int i = 0; i < stockFromDb.length; i++) {
				if (i < stockFromDb.length - 1) {
					stockToDb += stockFromDb[i] + ",";
				} else {
					stockToDb += stockFromDb[i];
				}

			}
			
			
			PreparedStatement ps = conn.prepareStatement("update machines set stock = ?  where machineName = ?");
			ps.setString(1, stockToDb);
			ps.setString(2, machineName);
			ps.executeUpdate();
			ps.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Ok";

	}
/**
	 Returns an ArrayList containing details of a user.
	@param data An ArrayList containing the userName of the user.
	@return An ArrayList containing details of the user in the following order: firstName, lastName, role, isAMember, status.
	 */

	public static ArrayList<String> getDetailsOfUser(ArrayList<String> data) {
		Statement stmt;
		String datafromdb = "";

		ArrayList<String> retArr = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT firstName, lastName,role  FROM project.users WHERE userName= '" + data.get(0) + "';");

			if (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3);
			}

			if (datafromdb.length() < 3) {
				retArr.add("notExsist");
			} else {

				String[] detailsFromDb = ((String) datafromdb).split(" ");
				retArr.add(detailsFromDb[0]);
				retArr.add(detailsFromDb[1]);
				retArr.add(detailsFromDb[2]);
				rs = stmt.executeQuery("SELECT isAMember,status  FROM customer WHERE userName= '" + data.get(0) + "';");
				datafromdb = "";
				if (rs.next()) {
					datafromdb = rs.getString(1) + " " + rs.getString(2);
				}
				String[] detailsFromDb1 = ((String) datafromdb).split(" ");

				if (datafromdb.length() >= 1) {
					retArr.add(detailsFromDb1[0]);
					retArr.add(detailsFromDb1[1]);
				} else
					retArr.add("isNotCustomer");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retArr;
	}

	/**
	 * 	function that activates getUserData to get area and returns the machine's names for specific area
	 * @param userName A string value of the userName of the user
	 * @return An ArrayList containing the machine names of the user and the location(storeName) in the following order: machineName, location.
	 */
	public static ArrayList<String> getMachineData(String userName) 
	{
		ArrayList<String> storeMachineData = getUserData(userName);
		ArrayList<String> machineName = new ArrayList<>();
		Statement stmt;
			
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT machineName FROM project.machines where location = '" + storeMachineData.get(3) +  "';");
			while (rs.next()) {
				machineName.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		machineName.add(storeMachineData.get(3)); //adding to machineName the area(storeName)
		return machineName;
	}

	/**
	 * function to get from data base oder report data
	 * @param data An ArrayList containing the location, month, and year of the report in that order
	 * @return An ArrayList containing the list of items, number of total orders, machine name, and area of the report. If the report does not exist, the ArrayList will contain "Error"
	 */
	public static ArrayList<String> getOrderReportData(ArrayList<String> data) {
		Statement stmt;
		//ArrayList<String> orderData = new ArrayList<>();
			
		String location = data.get(0);
		String month = data.get(1);
		String year = data.get(2);
			
		//String datafromdb = "";
		ArrayList<String> datafromdb = new ArrayList<>();
		int j=1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT listOfItems,numOfTotalOrders, machineName, area FROM project.monthlyordersreports"
					+ " where month = '" + month + "'and area = '" + location + "'and year = '" + year +"';");
			while (rs.next()) {
				datafromdb.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
			}
				
				
			if (datafromdb.size() > 0) {
				
				datafromdb.add(year);
				datafromdb.add(month);
				rs.close();
			}
			else {
				datafromdb.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return datafromdb;
	}

	/**
	 * 	function to get from data base costumer report data
	 * @param data An ArrayList containing the location, month, and year of the report in that order
	 * @return An ArrayList containing the list of customer orders and the area of the report. If the report does not exist, the ArrayList will contain "Error"
	 */
	public static ArrayList<String> getCustomersReportData(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> orderData = new ArrayList<>();
			
	
		String area = data.get(0);
		String month = data.get(1);
		String year = data.get(2);
			
		String datafromdb = "";
		int j=1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT listOfCustomerOrder,area FROM project.monthlycostumerreports"
					+ " where area = '" + area  + "'and month = '" + month + "'and year = '" + year +"';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2);
			}
				
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");
				
				
				for(int i=0; i<arrOfSub.length; i++) {
					orderData.add(arrOfSub[i]);
				}
				
				orderData.add(year);
				orderData.add(month);
				
				rs.close();
			}
			else {
				orderData.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
		return orderData;
	}

	

	/**
	 * 	function to get from data base costumer report data
	 * @param data  An ArrayList containing the machine name, month, and year of the report in that order
	 * @return An ArrayList containing the list of items, area, machine name, total lack of the report. If the report does not exist, the ArrayList will contain "Error"
	 */
	public static ArrayList<String> getInventoryReportData(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> orderData = new ArrayList<>();
		String machineName = data.get(0);
		String month = data.get(2);
		String year = data.get(3);
			
		String datafromdb = "";
		int j=1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT listOfItems,area,machineName,totalLack FROM project.monthlyinvenoryreports"
					+ " where MachineName = '" + machineName  + "'and month = '" + month + "'and year = '" + year +"';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
			}
				
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");

				for(int i=0; i<arrOfSub.length; i++) {
					orderData.add(arrOfSub[i]);
				}
				
				orderData.add(year);
				orderData.add(month);
				rs.close();
			}
			else {
				orderData.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderData;
	}

	/**
	 * 	function to get from data base items that are low than minimum level
	 * @param data An ArrayList containing the location of the store
	 * @return An ArrayList containing the machine name, item name, and their stock level, if the stock level is less than the minimum level defined in the machines table. If there's no update needed, the ArrayList will contain "No Update".
	 */
	public static ArrayList<String> getUpdateInfoForMA(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> orderData = new ArrayList<>();
		ArrayList<String> returnData = new ArrayList<>();

		String location = data.get(0);
		ArrayList<String> strArr = new ArrayList<>();
		String datafromdb = "";
			
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT machineName,stock,minLevel FROM project.machines"
					+ " where location = '" + location +"';");
			while (rs.next()) {
				strArr.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
			}

			int flagFirstTime;
			for(int i=0; i<strArr.size(); i++) {
				flagFirstTime=1;
				String[] arrOfSub = strArr.get(i).split(" ");
				String machineNumber = arrOfSub[0];
				String minLevel = arrOfSub[2];
				String[] arrOfStock = arrOfSub[1].split(",");
				String dataStock = "";
				for(int k=0; k<arrOfStock.length - 1;k+=2) {
					if (Integer.parseInt(arrOfStock[k+1]) <  Integer.parseInt(minLevel)) {
						if(flagFirstTime==1) {
							flagFirstTime=0;
							dataStock+=machineNumber + " ";
						}
						dataStock+=arrOfStock[k]+ " ";
					}	
				}
				if (flagFirstTime==0)
					returnData.add(dataStock);
			}
			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
			
		if(returnData.size()==0) {
			returnData.add("No Update");
		}
		return returnData;
	}

	/**
	 * 	function that update the minimum level in selected machine
	 * @param data an ArrayList containing the machine name and the minimum level in that order
	 * @return an ArrayList containing "Success!" if the update was successful, and "Error!" if the update was unsuccessful
	 */
	public static ArrayList<String> setMinimumLevelMachineData(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> result = new ArrayList<>();
		try 
		{
			stmt = conn.createStatement();
			String query = "update machines set minLevel = ? where machineName = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString   (1, data.get(1));
			preparedStmt.setString(2, data.get(0));
			      
			// execute the java preparedstatement
			preparedStmt.executeUpdate();
			result.add("Success!");}
		catch (SQLException e) {
			e.printStackTrace();
			result.add("Error!");
		}
			
		return result;
	}

	/**
	 * 	function to get from data base all operation workers names
	 * @param data an ArrayList containing any data, it is not used in this method
	 * @return an ArrayList containing the first name and last name of all the operation workers in the system
	 */
	public static ArrayList<String> getOperationWorkersName(ArrayList<String> data) {
		Statement stmt;
		String role = "OperationWorker";
				
		ArrayList<String> datafromdb = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT firstName,lastName FROM project.users"
					+ " where role = '" + role +"';");
			while (rs.next()) {
				datafromdb.add(rs.getString(1) + " " + rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return datafromdb;
	}

	/**
	 * 	function that send to database the message entered by the area manager
	 * @param data An ArrayList containing the name of the operation worker at index 0 and the message at index 1
	 * @return  an ArrayList containing the string "Success!" if the order was sent successfully or "Error!" if there was an exception thrown.
	 */
	public static ArrayList<String> sendExecutionOrder(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> result = new ArrayList<>();
		int place = 0;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT messageNumber "
					+ "FROM project.messageToOperationWorker;");
				
			while (rs.next()) {
				place = Integer.parseInt(rs.getString(1));
			}
								
			place++;
			PreparedStatement ps = conn.prepareStatement("insert into project.messageToOperationWorker values(?,?,?,?)");
			ps.setString(1, Integer.toString(place));// number of message
			ps.setString(2, data.get(0));// operation worker name
			ps.setString(3, data.get(1));// message
			ps.setString(4, "in process");// status of message = default is in process
			ps.executeUpdate();
			result.add("Success!");
				      
		}
		catch (SQLException e) {
			e.printStackTrace();
			result.add("Error!");
		}
				
		return result;
	}

	/**
	 * 	function to get from data base all not approved customers
	 * @param data an ArrayList of String which is not used in this method
	 * @return an ArrayList of String which contains the list of all customers that are not approved by the system
	 */
	public static ArrayList<String> getNotApprovedCustomers(ArrayList<String> data) {
		Statement stmt;
		String status = "NotApproved";
					
		ArrayList<String> datafromdb = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userName FROM project.customer"
					+ " where status = '" + status +"';");
			while (rs.next()) {
				datafromdb.add(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return datafromdb;
	}

	/**
	 * 	function that update the customers status to approved
	 * @param data an ArrayList of Strings that contains the usernames of the customers that need to be updated
	 * @return an ArrayList of Strings that contains the result of the update. "Success!" if the update was successful, "Error!" if an error occurred.
	 */
	public static ArrayList<String> updateCustomerStatus(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> result = new ArrayList<>();
		try 
		{
			stmt = conn.createStatement();
			for(int i=0;i<data.size();i++)
			{
				String query = "update customer set status = ? where userName = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString   (1, "Approved");
				preparedStmt.setString(2, data.get(i));
				preparedStmt.executeUpdate();
				PreparedStatement ps = conn.prepareStatement("insert into project.messages (userName,fromWho,status,content) values (?,?,?,?);");
				ps.setString(1, data.get(i));// userName
				ps.setString(2, "AreaManager");// from
				ps.setString(3, "NotCompleted");// status
				ps.setString(4, "Now You Are A Customer !! ");// content
				ps.executeUpdate();
			}
			result.add("Success!");}
		catch (SQLException e) {
			e.printStackTrace();
			result.add("Error!");
		}
				
		return result;
	}

	
	/**

	This method updates the customer's status to "IsAMember" and sets it to 1.
	It also creates a new message for the customer, indicating that the request to become a new member is waiting for approval.
	@param userName The userName of the customer
	@throws SQLException if there is an error in the SQL query
	*/
	public static void setCustomerAsMemer(String userName) throws SQLException {
		PreparedStatement ps;
		ps = conn.prepareStatement("update customer set IsAMember = 1  where userName = ?;");
		ps.setString(1, userName);
		ps.execute();
		
		ps = conn.prepareStatement("insert into project.messages (userName,fromWho,status,content) values (?,?,?,?);");
		ps.setString(1, userName);// userName
		ps.setString(2, "servicerep");// from
		ps.setString(3, "NotCompleted");// status
		ps.setString(4, "Your request to became a new Member is now waiting to approve!");// content
		
		
		
		ps.executeUpdate();
		
		ps.close();
	}

	
	/**

	This method is used to add a new user to the customer table in the database.
	@param data An ArrayList of Strings that contains the userName, and the type of the user (AsCustomer/AsMember)
	@throws SQLException if an error occurs while interacting with the database
	*/
	public static void addUserToCustomerTable(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt
				.executeQuery("SELECT memberNumber FROM project.users WHERE userName= '" + data.get(0) + "';");
		rs.next();
		datafromdb = rs.getString(1);

		PreparedStatement ps = conn.prepareStatement("insert into customer values(?,?,?,?,?,?,?,?)");
		ps.setString(1, data.get(0));// userName
		ps.setString(2, "NotApproved");// status
		ps.setString(3, datafromdb);// memberNubmer
		if (data.get(1).equals("AsCustomer")) {
			ps.setString(4, "0");// isAMember
		} else {
			ps.setString(4, "1");// isAMember
		}
		ps.setString(5, "1111111111111111");// cardNumber
		ps.setString(6, "10/25");// expierdDate
		ps.setString(7, "345");// cvv
		ps.setString(8, "0");// debt
		ps.executeUpdate();
		
		ps = conn.prepareStatement("insert into project.messages (userName,fromWho,status,content) values (?,?,?,?);");
		ps.setString(1, data.get(0));// userName
		ps.setString(2, "servicerep");// from
		ps.setString(3, "NotCompleted");// status
		if (data.get(1).equals("AsCustomer")) {
			ps.setString(4, "Your request to became a new Customer is now waiting to approve!");// content
		} else {
			ps.setString(4, "Your request to became a new Member is now waiting to approve!");// content
		}
		
		
		ps.executeUpdate();
		
		ps.close();

	}

	/**

	This function is used to import data from the simulationoutsidedata table in the project database.
	The imported data is used to populate the users and customer tables in the project database.
	@throws SQLException if an error occurs while trying to execute the SQL statement.
	*/
	
	public static void importData() throws SQLException {
		ArrayList<SimulationClass> users = new ArrayList<>();
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt.executeQuery("SELECT * FROM project.simulationoutsidedata;");
		while (rs.next()) {
			users.add(new SimulationClass(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
					rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14),
					rs.getString(15), rs.getString(16)));
		}

		PreparedStatement ps;
		for (SimulationClass user : users) {
			rs = stmt.executeQuery("SELECT * FROM project.users;");
			if (!rs.next()) {
				ps = conn.prepareStatement("insert into users values(?,?,?,?,?,?,?,?,?,?,?)");
				ps.setString(1, user.getUserName());// userName
				ps.setString(2, user.getPassword());// passWord
				ps.setString(3, user.getFirstName());// firstName
				ps.setString(4, user.getLastName());// lastName
				ps.setString(5, user.getRole());// role
				ps.setString(6, user.getEmail());// email
				ps.setString(7, user.getPhoneNumber());// phoneNumber
				ps.setString(8, "0");// isLogIn
				ps.setString(9, user.getId());// ID
				ps.setString(10, user.getLocation());// location
				ps.setString(11, user.getMemberNumber());// memberNumber
				ps.executeUpdate();
				if (!user.getStatus().equals("NotACustomer")) {
					ps = conn.prepareStatement("insert into customer values(?,?,?,?,?,?,?,?)");
					ps.setString(1, user.getUserName());// userName
					ps.setString(2, user.getStatus());// status
					ps.setString(3, user.getMemberNumber());
					ps.setString(4, user.getIsAMember());// isAMember
					ps.setString(5, user.getCreditCardNumber());// cardNumber
					ps.setString(6, user.getExpirationDate());// expierdDate
					ps.setString(7, user.getCvv());// cvv
					ps.setString(8, user.getDebt());// debt
					ps.executeUpdate();

				}
				ps.close();
			}
		}
		rs.close();

	}

	/**

	This function check if the customer is buying for the first time or not.
	@param data ArrayList that contains the username of the customer
	@return ArrayList that contains the result of the query. If it's the first buy it's contains "FirstBuy" otherwise "notFirstBuy"
	*/
	
	public static ArrayList<String> checkMemberFirstBuy(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> back = new ArrayList<>();
		String userName = data.get(0);
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT userName FROM project.reservation where userName = '" + userName + "' ;");
			if (rs.next()) {
				back.add("notFirstBuy");
			} else {
				back.add("FirstBuy");
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return back;
	}

	
	/**

	This function retrieves data from the 'sales' table in the database and returns the data as a string.
	@return A string containing the data retrieved from the 'sales' table in the database.
	@throws SQLException if a database access error occurs or this method is called on a closed connection
	*/
	public static String getSalesFromDb() throws SQLException {
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt.executeQuery("SELECT * FROM project.sales;");
		while (rs.next()) {
			datafromdb = datafromdb + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
					+ rs.getString(4) + " " + rs.getString(5) + " ";
		}

		rs.close();

		return datafromdb;
	}

	
	/**

	This method is used to get the sales data from the database.
	The method receives a list of string as input, which is used to filter the data based on the area.
	The data is returned as a string.
	@param data - ArrayList of strings containing the area to filter the data.
	@return datafromdb - A string containing the sales data
	@throws SQLException
	*/
	public static String getSalesToWorkerFromDb(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt.executeQuery("SELECT * FROM project.sales where Area = '" + data.get(0) + "' ;");
		while (rs.next()) {
			datafromdb = datafromdb + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
					+ rs.getString(4) + " " + rs.getString(5) + " ";
		}

		rs.close();

		return datafromdb;
	}


	/**

	The updateSaleOnMechines method updates the discount level of machines in a specific area.
	@param data - An ArrayList of type String that contains the following information in order:
	1. activeOrNot - a string that can be "SetActive" or "SetDisActive"
	2. area - a string representing the area of the machines that needs to be updated
	3. discount level - a string representing the discount level of the machines
	@throws SQLException - if a database access error occurs or this method is called on a closed connection
*/
	
	public static void updateSaleOnMechines(ArrayList<String> data) throws SQLException {
		String activeOrNot = data.get(0);
		String area = data.get(1);
		String datafromdb = "";
		PreparedStatement ps;
		switch (activeOrNot) {
		case "SetActive":
			ps = conn.prepareStatement("update machines set discountLevelArea = ?  where location = ?;");
			ps.setString(1, data.get(2));
			ps.setString(2, area);
			ps.execute();
			ps.close();
			break;
		case "SetDisActive":
			ps = conn.prepareStatement("update machines set discountLevelArea = ?  where location = ?;");
			ps.setString(1, "0");
			ps.setString(2, area);
			ps.execute();
			ps.close();
			break;

		}
	}

	
	/**

	Update a sale in the sales table in the database.
	@param data ArrayList of strings containing the action to take on the sale (SetActive, SetDisActive, Delete),
	the saleNumber (idNumber) and the new value for the IsActive column.
	@throws SQLException if a database error occurs.
	*/
	public static void updateSaleActivision(ArrayList<String> data) throws SQLException {
		String idNumber = data.get(2);
		String action = data.get(1);
		String datafromdb = "";
		PreparedStatement ps;
		switch (action) {
		case "SetActive":
			ps = conn.prepareStatement("update sales set IsActive = ?  where saleNumber = ?;");
			ps.setString(1, "1");
			ps.setString(2, idNumber);
			ps.execute();
			ps.close();
			break;
		case "SetDisActive":
			ps = conn.prepareStatement("update sales set IsActive = ?  where saleNumber = ?;");
			ps.setString(1, "0");
			ps.setString(2, idNumber);
			ps.execute();
			ps.close();
			break;
		case "Delete":
			ps = conn.prepareStatement("delete from sales where saleNumber = ?");
			ps.setString(1, idNumber);
			ps.execute();
			ps.close();
			break;
		}

	}

	/**
	 * function that return arrayList with the operation worker data
	 * @param userName
	 * @return ArrayList usrData
	 */
	public static ArrayList<String> getOperationWorkerData(String userName) 
	{
		ArrayList<String> userData = new ArrayList<>();
		userData = getUserData(userName);
		String name = userData.get(1) + " " + userData.get(2);
		String status = "in process";
		
		Statement stmt;
		ArrayList<String> strArr = new ArrayList<>();		
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT message,statusMessage FROM project.messagetooperationworker"
					+ " where workerName = '" + name + "'and statusMessage = '" + status +"';");
			while (rs.next()) {
				strArr.add(rs.getString(1) + "," + rs.getString(2));
			}
			
			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
			
		strArr.add(userData.get(1));
		return strArr;
		
	}
	
	
	/**
	 * function that returns all the machines numbers according to location for operation worker
	 * @param data
	 * @return ArrayList machineName
	 */
	public static ArrayList<String> getOperationWorkerData(ArrayList<String> data) 
	{
		ArrayList<String> machineName = new ArrayList<>();
		String location = data.get(1);
		Statement stmt;
				
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT machineName FROM project.machines where location = '" + location +  "';");
			while (rs.next()) {
				machineName.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return machineName;
	}
	
	
	/**
	 * function that return stock for selected location and machine
	 * @param data
	 * @return ArrayList stockToReturn
	 */
	public static ArrayList<String> getStockForOperationWorker(ArrayList<String> data) 
	{
		ArrayList<String> result = new ArrayList<>();
		ArrayList<String> stockToReturn = new ArrayList<>();
		//String newStock = "";
		String location = data.get(1);
		String machineName = data.get(2);
		//String item = data.get(3);
		//int amountToAdd = Integer.parseInt(data.get(4));
		Statement stmt;
					
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT stock,minLevel FROM project.machines where location = '" + location + "'and machineName = '" + machineName +"';");
			while (rs.next()) {
				result.add(rs.getString(1));
				result.add(rs.getString(2));
			}
			String[] stock = result.get(0).split(",");
			for(int i=0;i<stock.length;i++)
			{
				stockToReturn.add(stock[i]);
			}
			
			stockToReturn.add(result.get(1));
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			stockToReturn.add("Error!");
		}
		
		return stockToReturn;
		
	}

		
	/**
	 * function that update the message status to Done for operation worker
	 * @param data
	 * @return ArrayList result with Success or Error
	 */
	public static ArrayList<String> updateMessageStatusOP(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> result = new ArrayList<>();
		try 
		{
			stmt = conn.createStatement();
			for(int i=0;i<data.size();i++)
			{
				String query = "update messagetooperationworker set statusMessage = ? where message = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, "Done");
				preparedStmt.setString(2, data.get(i));
					      
				// execute the java preparedstatement
				preparedStmt.executeUpdate();
			}
			result.add("Success!");}
		catch (SQLException e) {
			e.printStackTrace();
			result.add("Error!");
		}
					
		return result;
	}
	
	
	/**
	 * function that update the stock entered by the operation worker
	 * @param data
	 * @return ArrayList result with Success or Error
	 */
	public static ArrayList<String> updateStockByOperationWorker(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> result = new ArrayList<>();
		String location = data.get(1);
		String machineName = data.get(2);
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		int day = localDate.getDayOfMonth();
		
		try 
		{
			stmt = conn.createStatement();
			for(int i=0;i<data.size();i++)
			{
				String query = "update machines set stock = ? where location = ? and machineName = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, data.get(3));
				preparedStmt.setString(2, location);
				preparedStmt.setString(3, machineName);
						      
				// execute the java preparedstatement
				preparedStmt.executeUpdate();
			}
			
			try 
			{
				int insertID = 0;
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT stockUpdateNumber "
						+ "FROM project.stockupdate;");
					
				while (rs.next()) {
					insertID = Integer.parseInt(rs.getString(1));
					//reportId++;
				}
									
				insertID++;
			
				PreparedStatement ps1 = conn.prepareStatement("insert into project.stockupdate values(?,?,?,?,?,?,?)");
				ps1.setString(1, Integer.toString(insertID));// number of insert
				ps1.setString(2, data.get(3));// list Of customers and their amount of orders
				ps1.setString(3, machineName);// machineName
				ps1.setString(4, location);// location
				ps1.setString(5, Integer.toString(day)); // day
				ps1.setString(6, Integer.toString(month)); // month
				ps1.setString(7, Integer.toString(year)); // year
				ps1.executeUpdate();
				
				result.add("Success!");
			}
			catch (SQLException e) {
				e.printStackTrace();
				result.add("Error!");
			}
			
			return result;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			result.add("Error!");
		}
						
		return result;
	}
	
	/**
	 * function that import data from DB for producing reports every 1st in month
	 */
	@SuppressWarnings("deprecation")
	public static void produceReports() {
		Statement stmt;
		ArrayList<String> data = new ArrayList<>();
		int monthToGet;
		int yearToGet;
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		int year = localDate.getYear();
		int day = localDate.getDayOfMonth();
		String typeOfOrder = "Delivery";
		//day = 1; //for checking functionality of producing reports every 1st in month
		
		if(day == 1) //in case its the first in the month
		{
			monthToGet = month - 1;
			if(monthToGet == 0) //in case the current month is January
			{
				monthToGet = 12;
				yearToGet = year - 1;
			}
			else
				yearToGet = year;
			
			try 
			{
			
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT machineName,location,userName,orders,day FROM project.reservation where month = '" + Integer.toString(monthToGet) 
					+ "'and not kindOfOrder = '" + typeOfOrder + "'and year = '" + Integer.toString(yearToGet) + "';");
				while (rs.next()) {
					data.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3)+ " " + rs.getString(4) + " " + rs.getString(5));

				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
				
			// calling 3 function for each type of report
			produceOrderReport(data, Integer.toString(yearToGet), Integer.toString(monthToGet));
			produceClientHistogramReport(data, Integer.toString(yearToGet), Integer.toString(monthToGet));
			produceInventoryReport(data, Integer.toString(yearToGet), Integer.toString(monthToGet));
			
		}
	}
	
	/**
	 * function that produce order report every 1st in month
	 * @param data
	 * @param year
	 * @param month
	 */
	public static void produceOrderReport(ArrayList<String> data, String year, String month)
	{
		HashMap<String, Integer> mapForAmountOfOrders = new HashMap<>(); //map for numOfTotalOrders
		HashMap<String, String> mapForListOfOrders = new HashMap<>(); //map for list of orders for each machine in the month
		HashMap<String, Integer> mapForOrders = new HashMap<>(); //calculate every item amount 
		String place="";
		String item;
		String listOfItem[];
		int value;
			
		for(int i=0;i<data.size();i++)
		{
			place = data.get(i).split(" ")[0] + " " +data.get(i).split(" ")[1];
			if(!mapForAmountOfOrders.containsKey(place))
			{
				mapForAmountOfOrders.put(place, 1);
				mapForListOfOrders.put(place, data.get(i).split(" ")[3]);
			}
			else
			{
				// increasing in 1 the amount of orders
				value = mapForAmountOfOrders.get(place);
				value++;
				mapForAmountOfOrders.put(place, value);
				String listOfItems = data.get(i).split(" ")[3];
				listOfItems = listOfItems + "," + mapForListOfOrders.get(place);
				mapForListOfOrders.put(place, listOfItems);
			}
		}
		
		
		
		for(String location : mapForAmountOfOrders.keySet())
		{
			item = mapForListOfOrders.get(location);
			
			listOfItem = item.split(",");
			
			for(int i=0;i<listOfItem.length-1;i+=2)
			{
				if(!mapForOrders.containsKey(listOfItem[i]))
				{
					mapForOrders.put(listOfItem[i], Integer.parseInt(listOfItem[i+1]));
				}
				
				else
				{
					int amount = mapForOrders.get(listOfItem[i]);
					amount = amount + Integer.parseInt(listOfItem[i+1]);
					
					mapForOrders.put(listOfItem[i], amount);
				}
			}
			
			
			
			String machineNameAndArea[] = location.split(" ");
			String listOfItems = "";
			
			int size=0;
			for(String itemInMap : mapForOrders.keySet())
			{
				listOfItems +=  itemInMap + "," + Integer.toString(mapForOrders.get(itemInMap));
				if(size+1 != mapForOrders.size())
				{
					listOfItems += ",";
				}
				
				size++;
			}
			
			
			Statement stmt;
			int reportId = 0;
			try 
			{
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT reportID "
						+ "FROM project.monthlyordersreports;");
					
				while (rs.next()) {
					reportId = Integer.parseInt(rs.getString(1));
					//reportId++;
				}
									
				reportId++;
				PreparedStatement ps = conn.prepareStatement("insert into project.monthlyordersreports values(?,?,?,?,?,?,?)");
				ps.setString(1, Integer.toString(reportId));// number of report
				ps.setString(2, listOfItems);// list Of Items 
				ps.setString(3, Integer.toString(mapForAmountOfOrders.get(location)));// numOfTotalOrders
				ps.setString(4, machineNameAndArea[0]); // machine Name
				ps.setString(5, machineNameAndArea[1]); // machine area
				ps.setString(6, month); // month
				ps.setString(7, year); // year
				ps.executeUpdate();					      
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			mapForOrders.clear();
			
		}
		
	}
	
	
	
	/**
	 * function that produce client histogram report every 1st in month
	 * @param data
	 * @param year
	 * @param month
	 */
	public static void produceClientHistogramReport(ArrayList<String> data, String year, String month)
	{
		
		HashMap<String, Integer> mapForCustomers = new HashMap<>(); //map for checking amount of orders for each client that made reservation
		
		HashMap<String, Integer> mapListRangeUae = new LinkedHashMap<>(); //map for UAE
		HashMap<String, Integer> mapListRangeSouth = new LinkedHashMap<>(); //map for South
		HashMap<String, Integer> mapListRangeNorth = new LinkedHashMap<>(); //map for North

		
		String userName="";
		String item;
		String listOfItem[];
		int value;
		
		for(int i=0;i<data.size();i++)
		{
			userName = data.get(i).split(" ")[2] + " " + data.get(i).split(" ")[1];
			if(!mapForCustomers.containsKey(userName))
			{
				mapForCustomers.put(userName, 1);
			}
			else
			{
				// increasing in 1 the reservation of current customer
				value = mapForCustomers.get(userName);
				value++;
				mapForCustomers.put(userName, value);
			}
		}
		
		
		
		for(int i=1,j=i+1;i<14;i+=2,j+=2)
		{
			mapListRangeUae.put(i + "-" + j, 0);
			mapListRangeSouth.put(i + "-" + j, 0);
			mapListRangeNorth.put(i + "-" + j, 0);
		}
		mapListRangeUae.put("15+", 0);
		mapListRangeSouth.put("15+", 0);
		mapListRangeNorth.put("15+", 0);
		
		
		
		for(String customer : mapForCustomers.keySet())
		{
			String area = customer.split(" ")[1]; //saving the area in the current customer
			
			switch(area)
			{
				case "UAE" :
					mapListRangeUae = insertToCurrectPlace(mapListRangeUae,mapForCustomers.get(customer));
					break;
				case "South" :
					mapListRangeSouth = insertToCurrectPlace(mapListRangeSouth,mapForCustomers.get(customer));
					break;
				case "North" :
					mapListRangeNorth = insertToCurrectPlace(mapListRangeNorth,mapForCustomers.get(customer));
					break;
				default:
					break;
			}
		}
		
		// insert data for each location: UAE, South, North
		insertToDBCustomerDataForReport(returnListOfCustomers(mapListRangeUae), "UAE" ,month, year);
		insertToDBCustomerDataForReport(returnListOfCustomers(mapListRangeSouth), "South" ,month, year);
		insertToDBCustomerDataForReport(returnListOfCustomers(mapListRangeNorth), "North" ,month, year);
			
		
	}
	
	/**
	 * private function that insert to current place the data
	 * @param mapToPlace
	 * @param amount
	 * @return HashMap with updated information
	 */
	private static HashMap<String,Integer> insertToCurrectPlace(HashMap<String,Integer> mapToPlace, int amount)
	{
		
		if(amount<15)
		{
			for(int i=1,j=i+1;i<14;i+=2,j+=2)
			{
				if((i<=amount) && (amount<=j))
				{
					String place = i + "-" + j;
					int value = mapToPlace.get(place);
					value++;
					mapToPlace.put(place, value);
					return mapToPlace;
				}
			}
		}
		
		else
		{
			String place = "15+";
			int value = mapToPlace.get(place);
			value++;
			mapToPlace.put(place, value);
		}
		return mapToPlace;
	}
	
	/**
	 * private function that insert into DB the information about the area for client histogram report
	 * @param listOfCustomers
	 * @param area
	 * @param month
	 * @param year
	 */
	private static void insertToDBCustomerDataForReport(String listOfCustomers,String area, String month, String year)
	{
		Statement stmt;
		int reportId = 0;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT indx "
					+ "FROM project.monthlycostumerreports;");
				
			while (rs.next()) {
				reportId = Integer.parseInt(rs.getString(1));
				//reportId++;
			}
								
			reportId++;
			PreparedStatement ps = conn.prepareStatement("insert into project.monthlycostumerreports values(?,?,?,?,?)");
			ps.setString(1, Integer.toString(reportId));// number of report
			ps.setString(2, listOfCustomers);// list Of customers and their amount of orders
			ps.setString(3, area);// area
			ps.setString(4, month); // month
			ps.setString(5, year); // year
			ps.executeUpdate();					      
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * private function that return the list of customers and the information in one String
	 * @param customers
	 * @return String listOfCustomers with all the information
	 */
	private static String returnListOfCustomers(HashMap<String,Integer> customers)
	{
		String listOfCustomers = "";
		int size = 0;
		
		for(String customerKey : customers.keySet())
		{
			listOfCustomers += customers.get(customerKey) + "," + customerKey;
			if(size+1 != customers.size())
			{
				listOfCustomers += ",";
			}
			
			size++;
		}
		
		return listOfCustomers;
	}
	
	/**
	 * function that produce inventory report every 1st in month
	 * @param data
	 * @param year
	 * @param month
	 */
	public static void produceInventoryReport(ArrayList<String> data, String year, String month)
	{
		// finding the last day in the month - required to fill data exactly days
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date givenDate;
		try {
			givenDate = df.parse("01/" + month + "/" + year);
			cal.setTime(givenDate);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
 
        int lastDateOfMonthForGivenDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        
        //map for each machine in location and it's stock in the last day of the month
        HashMap<String,String> machinesStock = getOldStockForInventory();
        
        //map for each machine in location and it's minimum level
        HashMap<String,String> machinesLevel = getMinimumLevelMachineInventory();
        
        //map for each day in month and the data in every day - amount of product
        // below minimum level and amount of unavailable products
        LinkedHashMap<String,String> dayAndAmountOfProducts = new LinkedHashMap<>();
        
        //map for each machine that hold the day and the stock that updated 
        HashMap<String,String> machineUpdateStockByDay = getUpdateStockForMachine();
        
        //map for list of orders and their days for each machine in the month
		HashMap<String, String> mapForListOfOrders = new HashMap<>();
		
		//map for each item and calculate its amount
		HashMap<String, Integer> mapForItem = new HashMap<>();
		
		int amountLastDay = 0;
		
		
		for(int i=0;i<data.size();i++)
		{
			String[] order = data.get(i).split(" ");
			
			if(mapForListOfOrders.containsKey(order[1] + " " + order[0] + " " + order[4]))
			{
				String oldOrder = mapForListOfOrders.get(order[1] + " " + order[0] + " " + order[4]);
				oldOrder = oldOrder + "," + order[3];
				mapForListOfOrders.put(order[1] + " " + order[0] + " " + order[4], oldOrder);
			}
			
			else
			{
				mapForListOfOrders.put(order[1] + " " + order[0] + " " + order[4], order[3]);
			}
		}		
		
		
		//running on the all map for specific location each time
		for(String location : machinesStock.keySet())
		{
			String[] items = machinesStock.get(location).split(",");
			int totalNotAvailable = 0;
			
			for(int i=0;i<items.length-1;i+=2)
			{
				mapForItem.put(items[i], Integer.parseInt(items[i+1]));
			}
			
			//Initializing the map for amount in each day
			for(int j=1;j<=lastDateOfMonthForGivenDate;j++)
			{
				dayAndAmountOfProducts.put(Integer.toString(j), "0,0");
			}
			
			for(int i=1;i<=lastDateOfMonthForGivenDate;i++)
			{
				
				//checking if there is update in stock in the current day i
				if(machineUpdateStockByDay.containsKey(location + " " + i))
				{
					String[] updateStock = machineUpdateStockByDay.get(location + " " + i).split(",");
					
					for(int j=0;j<updateStock.length-1;j+=2)
					{
						mapForItem.put(updateStock[j], Integer.parseInt(updateStock[j+1]));
					}
				}
				
				//checking if there is order in the current machine in the current day i
				if(mapForListOfOrders.containsKey(location + " " + i))
				{
					String[] itemInOrder = mapForListOfOrders.get(location + " " + i).split(",");
					
					for(int j=0;j<itemInOrder.length-1;j+=2)
					{
						int amount = mapForItem.get(itemInOrder[j]);
						amount = amount - Integer.parseInt(itemInOrder[j+1]);
						mapForItem.put(itemInOrder[j], amount);
					}
				}
				
				
				int amountBelowMinimumLevel = 0;
				int amountNotAvailable = 0;
				for(String item : mapForItem.keySet())
				{
					if(mapForItem.get(item)< Integer.parseInt(machinesLevel.get(location)))
						amountBelowMinimumLevel++;
					if(mapForItem.get(item) == 0)
						amountNotAvailable++;
				}
				
				dayAndAmountOfProducts.put(Integer.toString(i), amountBelowMinimumLevel + "," + amountNotAvailable);
				if(amountLastDay != amountNotAvailable)
				{
					amountLastDay = amountNotAvailable;
					totalNotAvailable+= amountNotAvailable;
				}
					
			}
			
			
			String dataOfDays = "";
			for(int i=1;i<=lastDateOfMonthForGivenDate;i++)
			{
				dataOfDays+= i + "," + dayAndAmountOfProducts.get(Integer.toString(i));
				
				if(i!=lastDateOfMonthForGivenDate)
					dataOfDays+= ",";
			}
			
			
			insertToDBInventoryDataForReport(location.split(" ")[1], location.split(" ")[0], month, 
					year, dataOfDays, "" + (1.0*totalNotAvailable/lastDateOfMonthForGivenDate)*100);
			
			mapForItem.clear();
		
		}
		
		
		for(String machineName : machinesStock.keySet())
		{
			String[] place = machineName.split(" ");
			
			updateCurrentStockToOld(place[0],place[1]);
		}
          
        
	}
	
	/**
	 * function that get the old stock from DB
	 * @return HashMap with old stock for each machine
	 */
	private static HashMap<String,String> getOldStockForInventory()
	{
		HashMap<String,String> machinesAndStock = new HashMap<>();
		
		Statement stmt;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT location,machineName,stockLastMonth FROM project.machines;");
				
			while (rs.next()) 
			{
				machinesAndStock.put(rs.getString(1) + " " + rs.getString(2), rs.getString(3));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return machinesAndStock;
	}
	
	/**
	 * function that get the minimum level of each machine
	 * @return HashMap with minimum level for each machine
	 */
	private static HashMap<String,String> getMinimumLevelMachineInventory()
	{
		HashMap<String,String> machinesAndLevel = new HashMap<>();
		
		Statement stmt;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT location,machineName,minLevel FROM project.machines;");
				
			while (rs.next()) 
			{
				machinesAndLevel.put(rs.getString(1) + " " + rs.getString(2), rs.getString(3));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return machinesAndLevel;
	}
	
	
	
	/**
	 * function that get from DB the updates in stock for each machines
	 * @return HashMap with updates in stock for each machine according days
	 */
	private static HashMap<String,String> getUpdateStockForMachine()
	{
		HashMap<String,String> machineUpdateStock = new HashMap<>();
		
		Statement stmt;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT area,machineName,day,stock FROM project.stockupdate;");
				
			while (rs.next()) 
			{
				machineUpdateStock.put(rs.getString(1) + " " + rs.getString(2) + 
						" " + rs.getString(3), rs.getString(4));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return machineUpdateStock;
	}
	
	
	/**
	 * function that insert to DB the information for inventory report
	 * @param machineName
	 * @param area
	 * @param month
	 * @param year
	 * @param dataForReport
	 * @param average
	 */
	private static void insertToDBInventoryDataForReport(String machineName,String area, String month, String year, String dataForReport, String average)
	{
		Statement stmt;
		int reportId = 0;
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT reportID "
					+ "FROM project.monthlyinvenoryreports;");
				
			while (rs.next()) {
				reportId = Integer.parseInt(rs.getString(1));
			}
								
			reportId++;
			PreparedStatement ps = conn.prepareStatement("insert into project.monthlyinvenoryreports values(?,?,?,?,?,?,?)");
			ps.setString(1, Integer.toString(reportId));// number of report
			ps.setString(2, dataForReport);// list of data for report
			ps.setString(3, machineName);// machineName
			ps.setString(4,average);// totalLack
			ps.setString(5, area); // area
			ps.setString(6, month); // month
			ps.setString(7, year); // year
			ps.executeUpdate();					      
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * function that save each 1st in month the current stock as old one
	 * @param location
	 * @param machineName
	 */
	private static void updateCurrentStockToOld(String location,String machineName)
	{
		Statement stmt;
		String oldStock = "";
		try 
		{
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT stock FROM project.machines where location = '" + location 
				+ "'and machineName = '" + machineName + "';");
			while (rs.next()) {
				oldStock+= rs.getString(1);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
				
		
		try 
		{
			
			String query = "update machines set stockLastMonth = ? where location = ? and machineName = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, oldStock);
			preparedStmt.setString(2, location);
			preparedStmt.setString(3, machineName);
			
			preparedStmt.executeUpdate();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	public static void addNewSaleFromMarketingManager(ArrayList<String> data) throws SQLException {
		data.remove(0);
		Random rand = new Random();
		int rand_int1;
		String discountLevel = data.get(0);
		String area = data.get(1);
		String discription = data.get(2);
		Statement stmt;
		ResultSet rs;
		String datafromdb = "";
		while (true) {
			rand_int1 = rand.nextInt(200);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"SELECT DiscountLevel FROM project.sales where saleNumber = '" + String.valueOf(rand_int1) + "';");
			if (!rs.next()) {
				rs.close();
				break;

			}
		}
		PreparedStatement ps = conn.prepareStatement("insert into project.sales values(?,?,?,?,?)");
		ps.setString(1, String.valueOf(rand_int1));// saleId
		ps.setString(2, discountLevel);
		ps.setString(3, area);
		ps.setString(4, discription);
		ps.setString(5, "0");
		ps.execute();
		ps.close();

	}

	public static String getdelToWorkerFromDb(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt.executeQuery(
				"SELECT orderNumber,address,status,location,DeliveryReceived FROM project.reservation where kindOfOrder = '" + "Delivery"
						+ "' and location = '" + data.get(0) + "' ;");
		while (rs.next()) {
			datafromdb = datafromdb + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
					+ rs.getString(4) + " "+ rs.getString(5) + " ";
		}

		rs.close();

		
		return datafromdb;
	}

	

	public static void updateReservation(ArrayList<String> data) throws SQLException {
		String orderNUmber = data.get(0);
		System.out.println(data);
		PreparedStatement ps;
			ps = conn.prepareStatement("update reservation set DeliveryReceived = ?  where orderNumber = ?;");
			ps.setString(1, "1");
			ps.setString(2, orderNUmber);
			ps.execute();
			ps.close();

			ps = conn.prepareStatement("update deliveries set DeliveryReceived = ?  where orderNumber = ?;");
			ps.setString(1, "Recived");// userName
			ps.setString(2, orderNUmber);// from
			ps.executeUpdate();
		

		
	}

	

		
		
	/**

	This method updates the approve status, order status, client notification status, delivery details and days left for delivery of a specific order.
	@param data An array list containing the order number, estimated delivery details, days left for delivery.
	@throws SQLException if there is an error in the SQL statement.
	*/
	public static void updateApproveStatusCalcNotifyDelLine(ArrayList<String> data) throws SQLException {
		String orderNumber = data.get(0);
		String estimatedDelDetails = data.get(1);
		String daysLeftForDel = data.get(2);
		PreparedStatement ps;
			ps = conn.prepareStatement("update deliveries set RequestApprove = ?, OrderStatus = ?, ClientIsNotified =?, DeliveryDetails=? , DaysLeftForDelivery = ?  where orderNumber = ?;" );
			ps.setString(1, "Approved");
			ps.setString(2, "Handled");
			ps.setString(3, "Yes");
			ps.setString(4, estimatedDelDetails);
			ps.setString(5, daysLeftForDel);
			ps.setString(6, orderNumber);
			ps.execute();
			ps.close();		
	}


	/**

	This function updates the status of a reservation in the "deliveries" and "reservation" table in the database.
	The status is changed to "Completed" and the orderNumber is used as the reference for which reservation to update.
	@param data An ArrayList of Strings containing the orderNumber of the reservation to update
	@throws SQLException if there is an error in the SQL syntax or in the database connection
	*/
	public static void updateReservationClose(ArrayList<String> data) throws SQLException {
		String idNumber = data.get(0);
		PreparedStatement ps;
			ps = conn.prepareStatement("update deliveries set OrderStatus = ?  where orderNumber = ?;");
			ps.setString(1, "Completed");
			ps.setString(2, idNumber);
			ps.execute();
			ps.close();	
			ps = conn.prepareStatement("update reservation set status = ?  where orderNumber = ?;");
			ps.setString(1, "Completed");
			ps.setString(2, idNumber);
			ps.execute();
			ps.close();
	}
	
	
	
	/**

	This function is used to retrieve delivery information of a specific location from the database and return it as a string.
	@param data an ArrayList of strings where the first element is the location of the delivery.
	@return a string containing the delivery information of the specified location.
	@throws SQLException if there is an error in executing the SQL query.
	*/

	public static String getdelToWorkerFromDbForTable(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		String datafromdb = "";
		ResultSet rs = stmt.executeQuery(
				"SELECT orderNumber, OrderStatus, address, DaysLeftForDelivery, ClientIsNotified ,location, DeliveryReceived, RequestApprove, DeliveryDetails FROM project.deliveries where kindOfOrder = '" + "Delivery"
						+ "' and location = '" + data.get(0) + "' ;");
		while (rs.next()) {
			datafromdb = datafromdb + rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
					+ rs.getString(4) + " "+ rs.getString(5) + " "+ rs.getString(6) + " "+ rs.getString(7) + " " + rs.getString(8) + " " + rs.getString(9) + " ";
		}
		  
		rs.close();
		
		return datafromdb;
	}
	
	/**

	This method is used to retrieve the delivery order details from the database for a specific customer.
	It takes the customer name as an input, and returns an ArrayList containing the order number, order status and Delivery Received status of all the delivery orders placed by that customer
	@param customerName The name of the customer for whom the delivery order details are required
	@return An ArrayList containing the order number, order status and Delivery Received status of all the delivery orders placed by the customer
	@throws SQLException if a database access error occurs
	*/
	public static ArrayList<String> getdelOrdersDB(String customerName) {
		ArrayList<String> delivertData = new ArrayList<>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT orderNumber, status, DeliveryReceived FROM project.reservation " + " where userName = '"
							+ customerName + "'and status = '"+"NotCompleted"+"' and kindOfOrder = '"+"Delivery"+"';");
			while (rs.next()) {
				delivertData.add(rs.getString(1) + "," + rs.getString(2)+ "," + rs.getString(3));
			}
			

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(delivertData.size()==0) {
			delivertData.add("Error");
		}
		System.out.println(delivertData);
		return delivertData;

	}
	
	
	/**

	This function updates the delivery status in the deliveries table in the database.
	It sets the RequestApprove column to 1 for a specific orderNumber.
	@param orderNumber the order number of the delivery to be updated
	@throws SQLException if there is an issue executing the SQL statement
	*/
	public static void updatedelToWorkerFromDb(String orderNumber) throws SQLException {
		PreparedStatement ps;
		ps = conn.prepareStatement("update deliveries set RequestApprove = 1  where orderNumber = ?;");
		ps.setString(1, orderNumber);
		ps.execute();
		ps.close();
	}

	/**

	Retrieves messages from the database for a specific user and marks them as read.
	@param data An ArrayList containing the username of the user
	@return ArrayList of messages
	@throws SQLException if there is an error executing the SQL query
	*/
	public static ArrayList<String> getMessages(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs;
		String userName = data.get(0);
		
		ArrayList<String> msgBackToClient = new ArrayList<>();
		rs = stmt.executeQuery(
				"SELECT content FROM project.messages where status = '" + "NotCompleted" + "' and userName = '"
						+ userName + "' ;");
		
		while(rs.next()) {
			msgBackToClient.add(rs.getString(1));
		}
		if(msgBackToClient.size()>0) {
			
		}
		PreparedStatement ps = conn.prepareStatement("update project.messages set status = ?  where userName = ?;");
		ps.setString(1, "Completed");
		ps.setString(2, userName);
		ps.executeUpdate();
		rs.close();
		return msgBackToClient;
	}
	
	/**

	The addNewMessageToCustomre function is used to insert a new message to the customer with information about their delivery.
	@param data an ArrayList of strings containing the order number and the estimated delivery time.
	@throws SQLException if there is an issue with the SQL query.
	*/
	public static void addNewMessageToCustomre(ArrayList<String> data) throws SQLException {
		Statement stmt = conn.createStatement();
		PreparedStatement ps;
		ResultSet rs;
		rs = stmt.executeQuery(
				"SELECT userName FROM project.reservation where orderNumber = '" + data.get(0) + "';");
		
	
		rs.next();
		String userName = rs.getString(1);
		
		
		ps = conn.prepareStatement("insert into project.messages (userName,fromWho,status,content) values (?,?,?,?);");
		ps.setString(1, userName);// userName
		ps.setString(2, "DeliveryEmployee");// from
		ps.setString(3, "NotCompleted");// status
		ps.setString(4, "Your delivry will arrive on the \n At:" + data.get(1));// content
		ps.executeUpdate();
		
	}
	
	
	
	

}
