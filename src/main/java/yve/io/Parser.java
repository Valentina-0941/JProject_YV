package yve.io;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.query.Query;
import yve.entities.Progress;
import yve.entities.Student;
import yve.entities.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Parser {
    private final Sheet sheet;
    @Getter
    private final ArrayList<Student> students = new ArrayList<>();
    @Getter
    private final ArrayList<Task> tasks = new ArrayList<>();
    @Getter
    private final ArrayList<Progress> progresses = new ArrayList<>();
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();

    public Parser(File file, List<HashMap<String, String>> list) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        Workbook wb = new XSSFWorkbook(stream);
        sheet = wb.getSheetAt(0);

        parseStudent(list);
        parseTask();
        parseProgress();
    }

    private void parseProgress() {
        for (int row = 3; row <= sheet.getLastRowNum(); row++) {
            for (int col = 0; col <= sheet.getRow(row).getLastCellNum(); col++) {
                if (!taskHashMap.containsKey(col)) continue;
                Integer score = (int) sheet.getRow(row).getCell(col).getNumericCellValue();
                Progress progress = Progress.builder()
                        .score(score)
                        .student(students.get(row - 3))
                        .task(taskHashMap.get(col))
                        .build();
                progresses.add(progress);
            }
        }
    }

    private void parseTask() {
        HashSet<String> inv = new HashSet<>();
        inv.add("Акт");
        inv.add("");
        inv.add("ДЗ");
        inv.add("Сем");
        inv.add("Упр");

        Row themeRow = sheet.getRow(0);
        Row nameRow = sheet.getRow(1);
        Row maxScoreRow = sheet.getRow(2);

        for (int x = 12; x <= nameRow.getLastCellNum(); x++) {
            String value = extractValue(nameRow.getCell(x));
            if (inv.contains(value) || value == null) continue;

            int subX = x;
            String themeName;
            while ((themeName = extractValue(themeRow.getCell(subX))) == null) subX--;

            String[] nameParts = value.split(": ");
            if (nameParts.length == 1) nameParts = new String[]{"ДЗ", nameParts[0]};

            Task task = Task.builder()
                    .nameTask(nameParts[1])
                    .type(nameParts[0])
                    .maxScore((int) maxScoreRow.getCell(x).getNumericCellValue())
                    .nameTheme(themeName)
                    .build();

            tasks.add(task);
            taskHashMap.put(x, task);
        }

    }

    private void parseStudent(List<HashMap<String, String>> list) {
        int lim = sheet.getLastRowNum();

        for (int y = 3; y <= lim; y++) {
            Row row = sheet.getRow(y);

            Student student = Student.builder()
                    .ulearnId(extractValue(row.getCell(1)))
                    .email(extractValue(row.getCell(2)))
                    .studentGroup(extractValue(row.getCell(3)))
                    .build();

            String[] rawName = extractValue(row.getCell(0)).split(" ");
            if (rawName.length == 1) {
                student.setName(rawName[0]);
                student.setSurname(rawName[0]);
            } else if (rawName.length == 2) {
                student.setSurname(rawName[0]);
                student.setName(rawName[1]);
            } else {
                student.setName(rawName[0]);
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < rawName.length; i++) {
                    builder.append(rawName[i]);
                    if (i != rawName.length - 1) builder.append(" ");
                }
                student.setSurname(builder.toString());
            }

            temp(student, list);
            students.add(student);
        }
    }

    private void temp(Student student, List<HashMap<String, String>> list) {
        for (HashMap<String, String> map : list) {
            if (!map.get("first_name").equals(student.getName()) || !map.get("last_name").equals(student.getSurname())) {
                continue;
            }

            System.out.println("TEST: " + student.getName() + " | " + student.getSurname());

            student.setSex(map.get("sex"));
            student.setBirthdate(map.get("bdate"));
            student.setCity(map.get("city"));
            student.setCountry(map.get("country"));
            student.setUniversity(map.get("university"));
            student.setFaculty(map.get("faculty"));
            student.setClosed(Boolean.parseBoolean(map.get("is_closed")));
        }
    }

    private static String extractValue(Cell cell) {
        return extractValue(cell, null);
    }

    private static String extractValue(Cell cell, String def) {
        if (cell == null) return def;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> def;
        };
    }
}
