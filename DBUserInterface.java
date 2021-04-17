package com.database;

import java.util.*;
import java.sql.*;

public class DBUserInterface {
	
	private Scanner input;
	private DBcommands db;
	private SQLExceptionHandler handler;
	private Connection connection;
	
	public DBUserInterface() {
		this.input = new Scanner(System.in);
		this.db = new DBcommands();	
		this.handler = new SQLExceptionHandler();
	}

	public boolean welcome() {
		boolean status = false;

		//Keep running until user either exits or has attempted login
		boolean running = false;
		while(!running) {

			System.out.println("");
			System.out.println("Welcome To Movie Database");
			System.out.println("-------------------------------");
			System.out.println("1. Continue To Login");
			System.out.println("2. Exit Program");
			System.out.println("");

			int userInput;
			try {
				System.out.print("Choice: ");
				userInput = Integer.parseInt(input.nextLine());

				if(userInput == 1)
				{
					status = userlogin();

					//if login was successful stop
					//Otherwise print error and
					//cont looping until exit
					if(status){
						running = true;
					}

				}

				else if(userInput == 2)
				{
					System.out.println("Exiting Program..");

					running = true;
					close();

				}

				else
				{
					System.out.println("Please Choose either 1 or 2");
				}

			}

			catch(Exception e)
			{
				System.out.println("Error: Please enter numbers ONLY");
			}
		}

		//Return True for sucess login
		//Or Keep false for exit
		return status;
	}
	
	public boolean userlogin() {
		System.out.println("");
		System.out.println("Movie Database Login");
		System.out.println("-------------------------------");
		System.out.print("Username: ");
		String userName = input.nextLine();
		System.out.print("Password: ");
		String password = input.nextLine();
		
		boolean status = false;
		try 
        {
        	// Incorporate mySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
            
        	// Connect to the test database
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection("jdbc:mysql://10.81.1.123:4406/moviedb?useSSL=false","root", "12251225");

			//Call ONLY if connection went without problems otherwise back to welcome screen
        	db.setConnection(connection);

			//Success change to true otherwise it stays false;
        	status = true;
        
        }
			
		catch (SQLException e) 
        {
			System.out.println("Error Code: " + e.getErrorCode() + "\n" + e.getMessage() );
		
		}

		
		catch(ClassNotFoundException e)
		{
			System.out.println("Error: Unable to load driver class.");
		}
		
		return status;
	
	}
	
	public void userlogout() {
		//Close the connection but keep input open
		try 
		{
			if(connection != null)
			{
				connection.close();
			}
			
			userInterface();
			
		} 
		catch (SQLException e) 
		{
			System.out.println("An error has occured while loging out");
		}
	
	}
	
	public void close() {
		//Close the connection and the input
		try
		{
					
			if(connection != null)
			{
				connection.close();
			}
			
			input.close();
		
		}

		catch(Exception e)
		{
			System.out.println("Error: Somthing has happend while closing connection");
		}		
	}

	public void userInterface() {
		boolean status = welcome();

		if(!status)
		{
			//Exit Program
			//No Need to go beyond this point
			//Otherwise continue with program
			return;
		}

		input = new Scanner(System.in);
		boolean running = false;
		while(!running)
		{
			System.out.println("");
			System.out.println("Movie Database Menu");
			System.out.println("-----------------------------");
			System.out.println("1. Add Star");
			System.out.println("2. Add Customer");
			System.out.println("3. Add Credit Card");
			System.out.println("4. Add Sale");
			System.out.println("5. Add Employee");
			System.out.println("6. Delete Customer");
			System.out.println("7. Delete Credit Card");
			System.out.println("8. Delete Employee");
			System.out.println("9. Lookup Movies Featuring Star");
			System.out.println("10. Database Metadata");
			System.out.println("11. SQL Query");
			System.out.println("12. Logout");
			System.out.println("13. Exit");
			System.out.println(" ");

			System.out.print("Choose Option: ");
			int userInput;

			try {
				userInput = Integer.parseInt(input.nextLine());

				switch(userInput)
				{
					case 1:
						addStar();
						break;
					case 2:
						addCustomer();
						break;
					case 3:
						addCreditCard(null);
						break;
					case 4:
						addSale();
						break;
					case 5:
						addEmployee();
						break;
					case 6:
						deleteCustomer();
						break;
					case 7:
						deleteCreditCard();
						break;
					case 8:
						deleteEmployee();
						break;
					case 9:
						featureStar();
						break;
					case 10:
						metaData();
						break;
					case 11:
						query();
						break;
					case 12:
						System.out.println("Logging Out..");
						System.out.println("");
						userlogout();
						running = true;
						break;
					case 13:
						System.out.println("Exiting Program");
						close();
						running = true;
						break;

					default:
						System.out.println("Error: Choose options 1-13");
				}
			}

			catch(Exception e) {
				System.out.println("Error: Please enter a number");
			}
		}

	}

	//---------------- Auxliary Methods ----------

	private void addStar() {

		String firstName, lastName, dob, photoURL;

		System.out.println("");
		System.out.print("First Name (Blank if None): ");
		firstName = input.nextLine();

		System.out.print("Last Name: ");
		lastName = input.nextLine();

		if(lastName.length() == 0)
		{
			System.out.println("");
			System.out.println("Error: Last Name is Required");
			System.out.println("Returning to Menu");
			System.out.println("");

			return;
		}

		System.out.print("Year of Birth (YYYY)(Blank if None): ");
		dob = input.nextLine();

		if( (dob.length() > 4) || dob.matches(".*[a-z].*")){
			System.out.println("ERROR: Invalid Year of Birth");
			return;
		}

		String[] star = {firstName,lastName,dob};

		try
		{
			db.addStar(star);
		}

		catch (SQLException e)
		{
			System.out.println("");
			handler.handleException(e);
			System.out.println("Returning to main menu");
		}

	}

	private void addCustomer() {
		String fname, lname, cc_id, address, email, password;

		System.out.println("");
		System.out.println("Insert Customer Into Database");
		System.out.println("--------------------------------------------");
		System.out.println("1. All Attributes are required (except Credit Card)");
		System.out.println("2. Name can be either first and last or last");
		System.out.println("");

		System.out.print("First Name: ");
		fname = input.nextLine();

		System.out.print("Last Name: ");
		lname = input.nextLine();

		System.out.print("Credit Card ID (Not Required): ");
		cc_id = input.nextLine();

		System.out.print("Address: ");
		address = input.nextLine();

		System.out.print("Email: ");
		email = input.nextLine();

		System.out.print("Password: ");
		password = input.nextLine();

		//Remove any white space and check for length (All attributes are required)
		if(password.trim().length() == 0 || email.trim().length() == 0 || address.trim().length() == 0 ||
				lname.trim().length() == 0 || fname.trim().length() == 0)
		{
			System.out.println("");
			System.out.println("Error: Required attributes are not filled");
			System.out.println("Returning to Menu");
			return;
		}

		if(cc_id.trim().length() != 0) {
			boolean status = addCreditCard(cc_id);
			if(!status) {
				System.out.println("Unable to Enter Credit Card. Exiting...");
				return;
			}
		}

		String[] customer = {fname,lname,cc_id,address, email, password};

		try
		{
			db.addCustomer(customer);
		}

		catch(SQLException e)
		{
			System.out.println(e);
			System.out.println("Error: There was problem in inserting customer into database");
		}

	}

	private boolean addCreditCard(String cc_id) {

		String fname, lname, date;

		if(cc_id == null) {
			System.out.print("CC ID: ");
			cc_id = input.nextLine();
		}

		System.out.print("CC First Name: ");
		fname = input.nextLine();

		System.out.print("CC Last Name: ");
		lname = input.nextLine();

		System.out.print("Expiration (YYYY-MM-DD): ");
		date = input.nextLine();

		if(fname.trim().length() == 0 || lname.trim().length() == 0 || date.trim().length() == 0)
		{
			System.out.println("");
			System.out.println("Error: Required attributes are not filled");
			System.out.println("Returning to Menu");
			return false;
		}


		String[] customer = {cc_id,fname,lname,date};
		boolean status = false;
		try
		{
			status = db.addCreditCard(customer);
		}

		catch(SQLException e)
		{
			System.out.println(e);
			System.out.println("Error: There was problem in inserting Credit Card into database");
		}

		return status;
	}

	private void addSale() {
		String movie_id, customer_id, date;

		System.out.println("");
		System.out.println("Insert Sale Into Database");
		System.out.println("--------------------------------------------");
		System.out.println("1. All Attributes are required ");
		System.out.println("");

		System.out.print("Movie ID: ");
		movie_id = input.nextLine();

		System.out.print("Customer ID: ");
		customer_id = input.nextLine();

		System.out.print("Expiration (YYYY-MM-DD): ");
		date = input.nextLine();

		//Remove any white space and check for length (All attributes are required)
		if(movie_id.trim().length() == 0 || customer_id.trim().length() == 0 || date.trim().length() == 0)
		{
			System.out.println("");
			System.out.println("Error: Required attributes are not filled");
			System.out.println("Returning to Menu");
			return;
		}

		String[] sale = {customer_id,movie_id, date};

		try
		{
			db.addSale(sale);
		}

		catch(SQLException e)
		{
			System.out.println(e);
			System.out.println("Error: There was problem in inserting sale into database");
		}

	}

	private void addEmployee() {
		String name, uname, password, userlevel;

		System.out.println("");
		System.out.println("Insert Employee Into Database");
		System.out.println("--------------------------------------------");
		System.out.println("1. All Attributes are required");
		System.out.println("");

		System.out.print("Name: ");
		name = input.nextLine();

		System.out.print("User Name: ");
		uname = input.nextLine();

		System.out.print("Password: ");
		password = input.nextLine();

		System.out.print("User Level: ");
		userlevel = input.nextLine();

		//Remove any white space and check for length (All attributes are required)
		if(password.trim().length() == 0 || name.trim().length() == 0 || userlevel.trim().length() == 0 ||
				password.trim().length() == 0)
		{
			System.out.println("");
			System.out.println("Error: Required attributes are not filled");
			System.out.println("Returning to Menu");
			return;
		}

		String[] employee = {name,uname,password,userlevel};

		try
		{
			db.addEmployee(employee);
		}

		catch(SQLException e)
		{
			System.out.println(e);
			System.out.println("Error: There was problem in inserting customer into database");
		}

	}

	private void deleteCustomer() {
		System.out.println("Customer Deletion");
		System.out.println("----------------------------------------------------");
		System.out.print("Customer ID: ");
		String customer_id = input.nextLine();

		if(customer_id.trim().length() == 0)
		{
			System.out.println("Error: No Credit Card Number was entered");
			System.out.println("Returning to Main Menu");
			return;
		}

		try
		{
			db.deleteCustomer(customer_id);
		}

		catch(SQLException e)
		{
			handler.handleException(e);
			System.out.println("");
		}

	}

	private void deleteCreditCard() {
		System.out.println("Credit Card Deletion");
		System.out.println("----------------------------------------------------");
		System.out.print("Credit Card Number: ");
		String ccID = input.nextLine();

		if(ccID.trim().length() == 0)
		{
			System.out.println("Error: No Credit Card Number was entered");
			System.out.println("Returning to Main Menu");
			return;
		}

		try
		{
			db.deleteCreditCard(ccID);
		}

		catch(SQLException e)
		{
			handler.handleException(e);
			System.out.println("");
		}

	}

	private void deleteEmployee() {
		System.out.println("Employee Deletion");
		System.out.println("----------------------------------------------------");
		System.out.print("Employee ID or Username: ");
		String employee_id = input.nextLine();

		if(employee_id.trim().length() == 0)
		{
			System.out.println("Error: No Information was entered");
			System.out.println("Returning to Main Menu");
			return;
		}

		try
		{
			db.deleteEmployee(employee_id);
		}

		catch(SQLException e)
		{
			handler.handleException(e);
			System.out.println("");
		}

	}

	private void query() {
		// TODO Auto-generated method stub
		System.out.println("ONLY SELECT/UPDATE/INSERT/DELETE Commands Allowed");
		System.out.println("----------------------------------------------------");
		System.out.print("Enter SQL Command: ");
		String query = input.nextLine();

		ArrayList<String> allowed = new ArrayList<String>(Arrays.asList("SELECT","UPDATE","INSERT","DELETE"));

		query = query.toUpperCase();
		String[] parsedQuery = query.split("\\s+");

		System.out.println(Arrays.toString(parsedQuery));
		if(!allowed.contains(parsedQuery[0]))
		{
			System.out.println("Error: Command does not contain the allowed commands (SELECT/UPDATE/INSERT/DELETE)");
			System.out.println("Returning to Main Menu");
			return;
		}

		try
		{
			db.executeQuery(query);
		}
		catch(SQLException e)
		{
			System.out.println("");
			handler.handleException(e);
			System.out.println("Returning to Main Menu");
		}

	}

	private void metaData() {
		// TODO Auto-generated method stub
		System.out.println("");
		System.out.println("Database Metadata");
		System.out.println("-------------------------------------------");
		System.out.println("");

		try
		{
			db.getMetaData();
		}

		catch(SQLException e)
		{
			System.out.println("");
			handler.handleException(e);
			System.out.println("Returning to Menu");
		}

	}

	private void featureStar() {
		String star_id, name;
		System.out.println("");
		System.out.println("To Find Movies Featuring a Star");
		System.out.println("Not All Information Is Required");
		System.out.println("-----------------------------------------");
		System.out.println("1. Provide Just a Star ID");
		System.out.println("2. Provide Name");
		System.out.println("3. Provide all ID, First and Last Name");
		System.out.println("");

		System.out.print("Star ID (Blank if None): ");
		star_id = input.nextLine().trim();

		System.out.print("Name (Blank if None): ");
		name = input.nextLine().trim();

		System.out.println("");
		int total_length = star_id.length() + name.length();

		if(total_length == 0)
		{
			System.out.println("");
			System.out.println("Error: No information provided. Returning to Menu.");
			return;
		}

		HashMap<String, String> star = new HashMap<String,String>();

		if(!star_id.isEmpty())
		{
			star.put("stars.id", star_id);
		}

		if(!name.isEmpty())
		{
			star.put("stars.name", name);
		}

		try
		{
			db.printStar(star);
		}

		catch(SQLException e)
		{
			handler.handleException(e);
			System.out.println("Returning to Menu");
		}
	}

	public static void main(String[] args) {
		try{
			
			DBUserInterface process = new DBUserInterface();
			process.userInterface();
			
		}
		
		catch(Exception e)
		{	
			System.out.println(e);
		}
	
	}

}
