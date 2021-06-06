package com.example.demo;

import com.example.demo.pojo.SymbolName;
import com.example.demo.service.SymbolNameService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import static com.example.czsc.utils.Utils.println;

@SpringBootApplication
public class StocksChartApplication {
	public static void main(String[] args) {
		File file =new File("/Users/haifeng/Downloads/log.txt");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter =new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



		SpringApplication.run(StocksChartApplication.class, args);
	}

}
