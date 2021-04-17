package com.database;

import java.sql.SQLException;

public class SQLExceptionHandler {
	
	public SQLExceptionHandler() {
		super();
	}

	public void handleException(SQLException e){
		
		String message = e.getMessage();
		int errorCode = e.getErrorCode();
		
		if(errorCode == 2055)
		{
			System.out.println("Error: Lost Connection to Server. Error Code: "+ errorCode);
		}
		
		else if (errorCode == 2058)
		{
			System.out.println("Error: Already connected to Server. Error Code: "+ errorCode);
		}
		
		else if (errorCode == 1149)
		{
			//This error message provides good explanation to a user
			System.out.println("Error: "+ message);
		}
		
		else if(errorCode == 1292)
		{
			System.out.println("Error: Incorrect Date Value must be in format YYYY-MM-DD. Error Code: "+ errorCode);
		}
		
		else if(errorCode == 1054)
		{
			System.out.println("Error: Unknown column name. Choose a valid column name. Error Code: "+ errorCode);		
		}
		
		else if(errorCode == 1146 )
		{
			//This error message provides good explanation to a user
			System.out.println("Error: " + message + " Error Code: "+ errorCode);
		}
		
		else if(errorCode == 1064)
		{
			System.out.println("Error: There is an error in your SQL Syntax. Error Code: "+ errorCode);
		}
		
		else if(errorCode == 1452)
		{
			System.out.println("Error: Cannot add or update due to foreign key constraint failure. Error Code: "+ errorCode);
		}
		else
		{
			System.out.println("Error: " + message + " Error Code: "+ errorCode);
		}
		
		
	}
}
