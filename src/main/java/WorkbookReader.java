import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class WorkbookReader {
    private String fileName;
    private ArrayList<Integer> numberSetOne, numberSetTwo;
    private ArrayList<String> wordSetOne;

    public WorkbookReader(String fileName) {
        this.fileName = fileName;
        numberSetOne = new ArrayList<Integer>();
        numberSetTwo = new ArrayList<Integer>();
        wordSetOne = new ArrayList<String>();
    }

    //Reads through Excel file and stores data appropriately
    public void read() {
        File file = new File(fileName);
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        boolean firstRow = true;    //Ignores title row
        while (rowIterator.hasNext()) { //Iterates through rows
            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            int colNumber = 0;
            while (cellIterator.hasNext()) {    //Iterates through each cell in row
                Cell cell = cellIterator.next();

                if (!firstRow) {
                    switch (colNumber) {    //0 = numberSetOne, 1 = numberSetTwo, 2 = wordSetOne
                        case 0:
                            numberSetOne.add((int) (cell.getNumericCellValue()));
                            break;
                        case 1:
                            numberSetTwo.add((int) (cell.getNumericCellValue()));
                            break;
                        case 2:
                            wordSetOne.add(cell.getStringCellValue());
                            break;
                        default:
                            System.out.println("Reading Error");
                    }
                    colNumber++;
                }
            }
            firstRow = false;
        }
    }

    //Opens HttpClient and posts JSON parameter as a string to challenge url
    public void postJson(JSONObject json) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://34.239.125.159:5000/challenge");

            String jsonString = json.toString();
            StringEntity entity = new StringEntity(jsonString);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            System.out.println(response.getStatusLine().getStatusCode());
            client.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Post Denied");
        }
    }

    //Main function
    public static void main(String[] args) {
        WorkbookReader data1 = new WorkbookReader("workbooks/Data1.xlsx");
        WorkbookReader data2 = new WorkbookReader(("workbooks/Data2.xlsx"));
        Calculator calc = new Calculator();
        data1.read();
        data2.read();

        int[] multipliedArray = calc.multiply(data1.numberSetOne, data2.numberSetOne);
        int[] dividedArray = calc.divide(data1.numberSetTwo, data2.numberSetTwo);
        String[] concatArray = calc.concat(data1.wordSetOne, data2.wordSetOne);

        JSONObject json = new JSONObject();
        JSONArray jsonNumberArrayOne = new JSONArray();
        JSONArray jsonNumberArrayTwo = new JSONArray();
        JSONArray jsonStringArrayOne = new JSONArray();

        json.put("id", "Patterson.Brad96@gmail.com");
        for(int i = 0; i < multipliedArray.length; i++) {
            jsonNumberArrayOne.put(multipliedArray[i]);
            jsonNumberArrayTwo.put(dividedArray[i]);
            jsonStringArrayOne.put(concatArray[i]);
        }
        json.put("numberSetOne", jsonNumberArrayOne);
        json.put("numberSetTwo", jsonNumberArrayTwo);
        json.put("wordSetOne", jsonStringArrayOne);
        System.out.println(json);
        data1.postJson(json);
    }
}
