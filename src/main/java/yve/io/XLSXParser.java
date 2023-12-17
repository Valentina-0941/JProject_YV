package yve.io;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import yve.entities.Student;
import yve.entities.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class XLSXParser {

    private final Sheet sheet;

    private final ArrayList<Student> students = new ArrayList<>();

    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashSet<Integer> integerHashSet = new HashSet<>();

    public XLSXParser(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        Workbook wb = new XSSFWorkbook(stream);
        sheet = wb.getSheetAt(0);
    }

    private void parseStudents() {
        int lim = sheet.getLastRowNum();

        for (int y = 3; y <= lim; y++) {
            Row row = sheet.getRow(y);

            String rawName = extractValue(row.getCell(0));
            String uid = extractValue(row.getCell(1));
            String email = extractValue(row.getCell(2));
            String group = extractValue(row.getCell(3));

            /*
            Student s = Student.builder()..........
            students.add(s);
             */
        }
    }

    private static String extractValue(Cell cell) {
        return extractValue(cell, null);
    }

    private static String extractValue(Cell cell, String def) {
        if (cell == null) return def;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue() + ", ";
            case NUMERIC -> cell.getNumericCellValue() + ", ";
            default -> def;
        };
    }
}
