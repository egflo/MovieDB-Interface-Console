package com.database;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

public class DBcommands{
	
	private Connection connection;
	
	public void setConnection(Connection conn)
	{
		this.connection = conn;
	}

	public void addStar(String[] star) throws SQLException
	{
		String firstName,lastName,dob;
		
		firstName = star[0];
		lastName = star[1];
		dob = star[2];

		Statement select = connection.createStatement();
		ResultSet tableData = select.executeQuery("SELECT id FROM stars ORDER BY id DESC LIMIT 1");
		tableData.next();

		//Generate a new ID for the incoming Star Entry
		String parse_id = tableData.getString("id").substring(2);
		int last_id = Integer.parseInt(parse_id);
		String new_id = "nm" + String.valueOf(++last_id);

		String insertTableSQL;
		PreparedStatement preparedStatement;
		
		if(dob.trim().isEmpty())
		{
			insertTableSQL = "INSERT INTO stars"
					+ "(id, name) VALUES"
					+ "(?,?)";
			
			preparedStatement = connection.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, new_id);
			preparedStatement.setString(2, firstName + " " + lastName);
		}
		
		else
		{
			insertTableSQL = "INSERT INTO stars"
					+ "(id, name, birthYear) VALUES"
					+ "(?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, new_id);
			preparedStatement.setString(2, firstName + " " + lastName);
			preparedStatement.setString(3, dob);
		}

		// execute insert SQL statement
		int rows = preparedStatement.executeUpdate();

		if(rows > 0)
		{
			System.out.println("Star added to database (Rows Affected: " + rows + ")");
		}
		else
		{
			System.out.println("Star not to database (Rows Affected: " + rows + ")");
		}

		preparedStatement.close();
	}

	public boolean addCreditCard(String[] cc) throws SQLException
	{
		//Insert Credit Card into Database.
		//Returns True for Success INSERT and False otherwise.

		String cc_id = cc[0];
		String fname = cc[1];
		String lname = cc[2];
		String exp = cc[3];

		String query = "SELECT COUNT(*) FROM creditcards WHERE id =?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, cc_id);

		ResultSet result = statement.executeQuery();

		result.next();
		if(result.getInt(1) != 0)
		{
			System.out.println("Error: Credit Card Found in Credit Card Database");
			return false;
		}

		String insertTableSQL = "INSERT INTO creditcards"
				+ "(id, firstName, lastName, expiration) VALUES"
				+ "(?,?,?,?)";

		PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);

		preparedStatement.setString(1, cc_id);
		preparedStatement.setString(2, fname);
		preparedStatement.setString(3, lname);
		preparedStatement.setDate(4, Date.valueOf(exp));

		int row = preparedStatement.executeUpdate();

		System.out.println("Success: Inserted Credit Card  into the database ("+ row +" Rows affected)");
		preparedStatement.close();

		return true;
	}

	public void addCustomer(String[] customer) throws SQLException
	{
		String firstName,lastName,cc_id,address,email,password;
		
	    firstName = customer[0];
		lastName = customer[1];
		cc_id = customer[2];
		address = customer[3];
		email = customer[4];
		password = customer[5];

		/**
		 String query = "SELECT COUNT(*) FROM creditCards WHERE id=?";
		 PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, cc_id);

        ResultSet result = statement.executeQuery();
        
        result.next();
        if(result.getInt(1) == 0)
        {
        	System.out.println("Error: No Customer Found in Credit Card Database");
        	return;
        }
        **/

		String insertTableSQL;
		PreparedStatement preparedStatement;

		if(cc_id.trim().length() == 0){
			insertTableSQL = "INSERT INTO customers"
					+ "(firstName, lastName, address, email, password) VALUES"
					+ "(?,?,?,?,?)";

			preparedStatement = connection.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, address);
			preparedStatement.setString(4, email);
			preparedStatement.setString(5, password);
		}

		else {
			insertTableSQL = "INSERT INTO customers"
					+ "(firstName, lastName, ccid, address, email, password) VALUES"
					+ "(?,?,?,?,?,?)";

			preparedStatement = connection.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, cc_id);
			preparedStatement.setString(4, address);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, password);
		}
		
		int row = preparedStatement.executeUpdate();
		System.out.println("Success: Inserted customer " + lastName + " into the database ("+ row +" Rows affected)");
		preparedStatement.close();
	}

	public void addEmployee(String[] employee) throws SQLException
	{
		String name, uname, userlevel, password;

		name = employee[0];
		uname = employee[1].trim();
		password = employee[2];
		userlevel = employee[3];

		 String query = "SELECT COUNT(*) FROM employees WHERE uname =?";
		 PreparedStatement statement = connection.prepareStatement(query);
		 statement.setString(1, uname);

		 ResultSet result = statement.executeQuery();

		 result.next();
		 if(result.getInt(1) > 0)
		 {
			 System.out.println("Error: Duplicate Username found in Database");
			 return;
		 }

		String insertTableSQL;
		PreparedStatement preparedStatement;


		insertTableSQL = "INSERT INTO employees"
				+ "(Name, UName, Password, Userlevel) VALUES"
				+ "(?,?,?,?)";

		preparedStatement = connection.prepareStatement(insertTableSQL);

		preparedStatement.setString(1, name);
		preparedStatement.setString(2, uname);
		preparedStatement.setString(3, password);
		preparedStatement.setString(4, userlevel);

		int row = preparedStatement.executeUpdate();
		System.out.println("Success: Inserted customer " + uname + " into the database ("+ row +" Rows affected)");

		result.close();
		preparedStatement.close();
	}

	private boolean validateSale(String[] sale) throws  SQLException
	{
		//Validates sale by checking if either movie or customer exists
		//Returns True if sale is valid otherwise false

		String customer_id = sale[0];
		String movie_id = sale[1];

		String query;
		PreparedStatement preparedStatement;

		query = "SELECT COUNT(*) FROM customers WHERE id = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, customer_id);

		ResultSet rs = preparedStatement.executeQuery();

		boolean valid = true;
		if(!rs.next()){
			System.out.println("No such customer exists. Unable to add sale.");
			valid = false;
		}

		query = "SELECT COUNT(*) FROM movies WHERE id = ?";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, movie_id);

		rs = preparedStatement.executeQuery();
		if(!rs.next()){
			System.out.println("No such movie exists. Unable to add sale.");
			valid = false;
		}

		rs.close();
		preparedStatement.close();

		return valid;
	}

	public void addSale(String[] sale) throws  SQLException
	{
		String customer_id = sale[0];
		String movie_id = sale[1];
		String date = sale[2];

		boolean valid = validateSale(sale);

		if(!valid) {
			return;
		}

		String insertTableSQL;
		PreparedStatement preparedStatement;

		insertTableSQL = "INSERT INTO sales"
					+ "(customerId, movieId, saleDate) VALUES"
					+ "(?,?,?)";

		preparedStatement = connection.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);

		preparedStatement.setString(1, customer_id);
		preparedStatement.setString(2, movie_id);
		preparedStatement.setDate(3, Date.valueOf(date));

		int row = preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();

		if (rs.next()){
			int order_num = rs.getInt(1);
			System.out.println("Success: Inserted sale #" + order_num + " into the database ("+ row +" Rows affected)");
		}

		else {

			System.out.println("Error: No sale inserted into the database ("+ row +" Rows affected)");
		}

		preparedStatement.close();
	}

	public void deleteCustomer(String customer_id) throws SQLException
	{

		String query = "DELETE FROM customers WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, customer_id);
		int status = statement.executeUpdate();

		System.out.println("Deletion Status: " + status + " Rows deleted");

		statement.close();
		
	}

	public void deleteEmployee(String employee_id) throws SQLException
	{
		String query;
		//Check if id is an int (id) or string (username)
		if(employee_id.matches("\\d+")){
			query = "DELETE FROM employees WHERE EmpID = ?";
		}
		else {
			query = "DELETE FROM employees WHERE UName = ?";
		}

		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, employee_id);
		int status = statement.executeUpdate();

		System.out.println("Deletion Status: " + status + " Rows deleted");

		statement.close();
	}

	public void deleteCreditCard(String cc_id) throws SQLException
	{
		//Remove credit card from database
		//int status = update.executeUpdate("DELETE FROM CUSTOMERS WHERE cc_id = '"+cc_id+"'");

		String query = "DELECT FROM creditcards WHERE id =?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, cc_id);
		int status = statement.executeUpdate();

		System.out.println("Deletion Status: " + status + " Rows deleted");

		statement.close();
	}

	public void getMetaData() throws  SQLException
	{
		Statement select = connection.createStatement();
		ResultSet rs = select.executeQuery("SHOW tables");

		while (rs.next())
		{
			String tableName = rs.getString(1);
			getMetaDataTable(tableName);

		}
		rs.close();
		select.close();
	}

	public void getMetaDataTable(String tableName) throws SQLException
	{
		  System.out.println("Table: " + tableName);
		  System.out.println("-----------------------------------------");
		    
		  Statement select = connection.createStatement();
	      ResultSet tableData = select.executeQuery("DESCRIBE " + tableName);
		  
	      System.out.printf("%-15s%-15s%-8s%s\n","Field","Type","NULL","key");

	      while(tableData.next())
	      {
	    	  String field = tableData.getString(1);
	    	  String type = tableData.getString(2);
	    	  String NULL = tableData.getString(3);
	    	  String key = tableData.getString(4);
	    	  
	    	  System.out.printf("%-15s%-15s%-8s%s\n",field,type,NULL,key);

	      }
	      System.out.println("");
	      select.close();
	      tableData.close();

	}

	public void executeQuery(String query) throws SQLException
	{
		if(query.contains("SELECT"))
		{
			Statement select = connection.createStatement();
	        ResultSet result = select.executeQuery(query);

	        ResultSetMetaData md = result.getMetaData();
	        
	        int numberOfColumns = md.getColumnCount();
	        	   
	        int row = 0;
	        while(result.next())
	        {      
	        	row++;
	        	System.out.println("Row #" + row);
	        	System.out.println("---------------------------------------------------------");      	
	        	for(int i = 1; i <= numberOfColumns; i++) {
	        		String columnName = md.getColumnName(i);
	                String columnValue = result.getString(i);
	                
	                System.out.println(columnName + ": " + columnValue);
	            }
	    
	        	System.out.println("");
	        }
	        
	        System.out.println("Number of rows: " + row);
	        System.out.println("");
	        
	        select.close();
	        result.close();
		}
		
		else
		{
			
			Statement select = connection.createStatement();
	        int status = select.executeUpdate(query);
			
	        System.out.println(status + " Rows Afftected");
	        select.close();
		}
		
		
	}

	public void printStar(HashMap<String,String> star) throws SQLException
	{
		/**Print out (to the screen) the movies featuring a given star*/
		// Create an execute an SQL statement to select all of table"Stars" records
		String query = "SELECT "
				+"movies.id as film_id,"
				+"movies.title,"
				+"movies.year,"
				+"movies.director"
				+" FROM movies"
				+" INNER JOIN stars_in_movies"
				+" ON movies.id = stars_in_movies.movieid"
				+" INNER JOIN stars"
				+" ON stars_in_movies.starid = stars.id"
				+" WHERE ";

		int count = star.size();

		for (String key : star.keySet()) {
			String value = star.get(key);

			if(count == 1)
			{
				query += String.format("%s = '%s'", key, value);
			}

			else
			{
				query += String.format("%s = '%s' AND ", key, value);
			}
			count--;
		}

		Statement select = connection.createStatement();
		ResultSet result = select.executeQuery(query);

		int entry = 0;
		while (result.next()) {
			entry++;
			System.out.println("Movie #" + entry);
			System.out.println("---------------------------------------------");

			System.out.println("Movie ID: " + result.getString(1));
			System.out.println("Title: " + result.getString(2));
			System.out.println("Year: " + result.getString(3));
			System.out.println("Director: " + result.getString(4));
			System.out.println();

		}

		System.out.println("Total Number of Results: " + entry);

		select.close();
		result.close();
	}
}
