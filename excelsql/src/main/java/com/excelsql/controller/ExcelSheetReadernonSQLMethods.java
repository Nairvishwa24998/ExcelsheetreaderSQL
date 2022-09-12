package com.excelsql.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.google.gson.Gson;

public class ExcelSheetReadernonSQLMethods {
	public void populateRecords(int rowCount, List<ExcelSheetReaderstudentObject> records, XSSFSheet sheet) {
		for (int i = 1; i < rowCount + 1; i++) {
			// Excel's default format is double for numeric entries so converting to string
			// and choosing the part before the dot
			String admissionNumber = (sheet.getRow(i).getCell(1).getNumericCellValue() + "").split("\\.")[0];
			ExcelSheetReaderstudentObject studentDetails = new ExcelSheetReaderstudentObject();
			studentDetails.setstudentName(sheet.getRow(i).getCell(0).getStringCellValue());
			studentDetails.setadmissionNumber(admissionNumber);
			studentDetails.setphysics(sheet.getRow(i).getCell(2).getNumericCellValue());
			studentDetails.setmathematics(sheet.getRow(i).getCell(3).getNumericCellValue());
			studentDetails.setchemistry(sheet.getRow(i).getCell(4).getNumericCellValue());
			records.add(studentDetails);
		}

	}

//	Take in ArrayList of StudentObjects and return json file containing them
	public void jsonifyRecords(List<ExcelSheetReaderstudentObject> records) throws IOException {

		String json = new Gson().toJson(records);
		try(FileWriter file = new FileWriter("./src/main/resources/outputdata.json");){
		file.write(json);}
		System.out.println("Succesfully converted to json file by the name outputdata.json");
	}

	// remove trailing and leading spaces and format accordingly
	public String cleanInput(String response) {
		return response.trim().substring(0, 1).toUpperCase() + response.substring(1).toLowerCase();
	}

}
