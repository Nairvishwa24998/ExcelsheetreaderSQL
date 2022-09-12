package com.excelsql.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.google.gson.Gson;

public class ExcelSheetReaderSQLMethods {
	static final String DATABASE_URL = "jdbc:mysql://localhost:3306/";
	static final String USER = "root";
	static final String PASSWORD = "password";
	private static final String USE = "USE student_database";

//	returns connection so it can be used in other methods
	public Connection createDatabase() throws SQLException {
		Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

		try (Statement command = conn.createStatement();) {
			String db = "CREATE DATABASE student_database";
			command.executeUpdate(db);
			command.executeUpdate(USE);
			String table = "CREATE TABLE student_details(studentname VARCHAR(30), admissionnumber VARCHAR(6), physics DOUBLE  (4,1), mathematics DOUBLE (4,1), chemistry DOUBLE  (4,1))";
			command.executeUpdate(table);
			System.out.println("Database succesfully created");
		} catch (SQLException exception) {
			System.out.println("Database and table already exist. Moving to the next method...");
		}

		return conn;
	}

//	Takes an arraylist of studentObjects and adds them to database
	public void addEntries(List<ExcelSheetReaderstudentObject> records, Connection conn) {
		try (Statement command = conn.createStatement();) {

			String answer = "SELECT COUNT(1) FROM student_details";
			ResultSet check = command.executeQuery(answer);
			check.next();

			command.executeUpdate(USE);
			for (ExcelSheetReaderstudentObject student : records) {
				String addStudent = "INSERT INTO student_details(studentname,admissionnumber,physics,mathematics,chemistry)"
						+ "VALUES(?,?,?,?,?)";

				try (PreparedStatement pstatement = conn.prepareStatement(addStudent,
						Statement.RETURN_GENERATED_KEYS);) {
					pstatement.setString(1, student.getstudentName());
					pstatement.setString(2, student.getadmissionNumber());
					pstatement.setDouble(3, student.getphysics());
					pstatement.setDouble(4, student.getchemistry());
					pstatement.setDouble(5, student.getmathematics());
					pstatement.executeUpdate();
				}

			}
			System.out.println("All student objects added");
		} catch (SQLException exception) {
			System.out.println("All information has already been added");
		}

	}

	public void searchEntries(Connection conn, String response) {
		try (Statement command = conn.createStatement();){
//			create object to store values obtained from database for easy conversion
			ExcelSheetReaderstudentObject studentInfo = new ExcelSheetReaderstudentObject();
			command.executeUpdate(USE);
			String findStudent = "SELECT * FROM student_details WHERE LOWER(studentname) = TRIM(LOWER(?)) OR LOWER(admissionnumber) = TRIM(LOWER(?))";
			try (PreparedStatement pstatement = conn.prepareStatement(findStudent, Statement.RETURN_GENERATED_KEYS);) {
				pstatement.setString(1, response);
				pstatement.setString(2, response);
				ResultSet results = pstatement.executeQuery();
				while (results.next()) {
					studentInfo.setstudentName(results.getString(1));
					studentInfo.setadmissionNumber(results.getString(2));
					studentInfo.setphysics(results.getDouble(3));
					studentInfo.setchemistry(results.getDouble(4));
					studentInfo.setmathematics(results.getDouble(5));
//				convert objects to json and print
					String json = new Gson().toJson(studentInfo);
					System.out.println(json);
				}
				
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}

	}

}
