package com.example.czsc.dataSystem.dataUtils;

import com.example.demo.pojo.Exchange;
import com.example.demo.pojo.ExchangeType;
import com.example.demo.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.python.antlr.ast.Str;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.czsc.utils.Utils.println;


public class ReadExcel {
    private int totalRows = 0;
    private int totalCells;
    {
        totalCells = 0;
    }
    private String errorMsg;

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean validateExcel(String filePath){
        if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath))){
            errorMsg = "文件名不是excel格式";
            println("Come to validateExcel " + filePath);
            return false;
        }
        return true;
    }

    public List<Exchange> getExcelInfo(String filePath) {
        File file1 = new File(filePath);
        List<Exchange> exchanges = new ArrayList<>();
        InputStream is = null;
        try {
            if (!validateExcel(filePath)) {
                return null;
            }
            boolean isExcel2003 = true;
            if (WDWUtil.isExcel2007(filePath))
                isExcel2003 = false;
            is = new FileInputStream(file1);
            exchanges = getExcelInfo(is,isExcel2003);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                }catch (IOException e){
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return exchanges;
    }

    private List<Exchange> getExcelInfo(InputStream is, boolean isExcel2003) {
        List<Exchange> exchanges = null;
        try {
            Workbook workbook = null;
            if (isExcel2003){
                workbook = new HSSFWorkbook(is);
            }else 
                workbook = new XSSFWorkbook(is);
            exchanges = readExcelValue(workbook);
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
        return exchanges;
    }

    private List<Exchange> readExcelValue(Workbook workbook) throws ParseException {
        Sheet sheet = workbook.getSheetAt(0);
        this.totalRows = sheet.getPhysicalNumberOfRows();
        Map<String, Integer> fields = null;
        if (totalRows >= 1 && sheet.getRow(0) != null){
            Row row = sheet.getRow(0);
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            fields = refectCells(row);
        }
        List<Exchange> exchanges = new ArrayList<>();
        Exchange exchange;
        for (int r = 1;r < totalRows;r++){
            Row row = sheet.getRow(r);
            if (row == null) continue;
            exchange = new Exchange();
            assert fields != null;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate time = LocalDate.parse(row.getCell(0).getStringCellValue(),format);
            LocalDateTime time1 = row.getCell(1).getLocalDateTimeCellValue();
            time1 = time1.withYear(time.getYear());
            time1 = time1.withMonth(time.getMonthValue());
            time1 = time1.withDayOfMonth(time.getDayOfMonth());
            exchange.setExchangeTime(DateUtils.asDate(time1));
            String symbol = String.valueOf(getInt(row.getCell(2).getNumericCellValue()));
            if (symbol.length() < 6){
                int length = symbol.length();
                if (length == 5){
                    symbol = "0" + symbol;
                }else if (length==4){
                    symbol = "00" + symbol;
                }else if (length == 3){
                    symbol = "000" + symbol;
                }else if (length ==2){
                    symbol = "0000"+ symbol;
                }else if (length == 1){
                    symbol = "00000" + symbol;
                }
            }
            exchange.setSymbol(symbol);
            String exchangeType = row.getCell(4).getStringCellValue();
            double amount = row.getCell(5).getNumericCellValue();
            switch (exchangeType) {
                case "证券买入":
                case "新股入帐":
                case "红股入账":
                    exchange.setType(ExchangeType.BUY);
                    exchange.setAmount(getInt(amount));
                    break;
                case "证券卖出":
                    exchange.setType(ExchangeType.SELL);
                    exchange.setAmount(getInt(amount) * (-1));
                    break;
                case "红利入账":
                    exchange.setType(ExchangeType.BONUS);
                    exchange.setAmount(0);
                    break;
                case "股息红利差异":
                    exchange.setType(ExchangeType.BONUS_TAX);
                    exchange.setAmount(0);
                    break;
                case "配售缴款":
                    continue;
            }
            double price = row.getCell(6).getNumericCellValue();
            exchange.setPrice(getFloat(price));
            exchange.setRepertory(getInt(row.getCell(8).getNumericCellValue()));
            exchange.setFee(getFloat(row.getCell(9).getNumericCellValue()) * (-1));
            exchange.setFeeTax(getFloat(row.getCell(10).getNumericCellValue()) * (-1));
            if (row.getCell(11).getNumericCellValue() != 0) {
                exchange.setExchangeMoney(getFloat(row.getCell(11).getNumericCellValue()));
            }else{
                exchange.setExchangeMoney(exchange.getAmount() * exchange.getPrice());
            }
            exchanges.add(exchange);
        }
        return exchanges;
    }
    private long getInt(double amount){
        return Math.round(amount);
    }
    private float getFloat(double price){
        return ((Double)price).floatValue();
    }

    private Map<String, Integer> refectCells(Row row) {
        List<String> fields = new ArrayList<>();
        for (int j = 0;j < totalCells;j++){
            fields.add(row.getCell(j).getStringCellValue());
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("name",fields.indexOf("name"));
        if (fields.contains("owner")){
            map.put("owner",fields.indexOf("owner"));
        }

        if (fields.contains("city")){
            map.put("city",fields.indexOf("city"));
        }
        if (fields.contains("money")){
            map.put("money",fields.indexOf("money"));
        }
        if (fields.contains("moneyUnit")){
            map.put("moneyUnit",fields.indexOf("moneyUnit"));
        }

        if (fields.contains("esDate")){
            map.put("esDate",fields.indexOf("esDate"));
        }

        if (fields.contains("business")){
            map.put("business",fields.indexOf("business"));
        }
        if (fields.contains("phoneNumber")) {
            map.put("phoneNumber", fields.indexOf("phoneNumber"));
        }
        if (fields.contains("address")) {
            map.put("address", fields.indexOf("address"));
        }
        if (fields.contains("webAdd")) {
            map.put("webAdd", fields.indexOf("webAdd"));
        }
        if (fields.contains("email")) {
            map.put("email", fields.indexOf("email"));
        }
        return map;
    }

    public static void main(String[] args) {
        String path = "/Users/haifeng/Downloads/exchange.xlsx";
        ReadExcel reader = new ReadExcel();
        List<Exchange> exchanges = reader.getExcelInfo(path);
        for(Exchange exchange : exchanges){
            System.out.println(exchange);
        }
    }
}
