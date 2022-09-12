package com.excelsql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.excelsql.controller.ExcelSheetReaderSQLMethods;
import com.excelsql.controller.ExcelSheetReadernonSQLMethods;
import com.excelsql.controller.ExcelSheetReaderstudentObject;

class ExcelsheetreaderSQL {

	public static void main(String[] args) {
		// Create arraylist to store
		List<ExcelSheetReaderstudentObject> records = new ArrayList<>();
//		initialize class containing non SQL methods
		ExcelSheetReadernonSQLMethods nonSQLmethods = new ExcelSheetReadernonSQLMethods();
//		Initialize class containing SQL Methods
		ExcelSheetReaderSQLMethods sqlMethods = new ExcelSheetReaderSQLMethods();
		Scanner scan = new Scanner(System.in);
		String filePath = "./src/main/resources/Excelsheetdata/Data.xlsx";
		try (XSSFWorkbook workbook = new XSSFWorkbook(filePath);) {

			XSSFSheet sheet = workbook.getSheet("Data");
			// The first row contains column names, so excluded from count
			int rowCount = sheet.getPhysicalNumberOfRows() - 1;

//			fill Arraylist with student objects containing relevant information
			nonSQLmethods.populateRecords(rowCount, records, sheet);
// 			Create a json file with data in records
			nonSQLmethods.jsonifyRecords(records);

//			if database already exist then function would take care of that and creates a table with relevant columns		
			try(Connection con = sqlMethods.createDatabase();){
			sqlMethods.addEntries(records, con);
			String response;
			System.out.println("Enter information");
			response = scan.next();
			response = nonSQLmethods.cleanInput(response);
			sqlMethods.searchEntries(con, response);
			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			scan.close();

		}

	}
}
