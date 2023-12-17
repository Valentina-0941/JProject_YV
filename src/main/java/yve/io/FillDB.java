package yve.io;

import yve.entities.Progress;
import yve.entities.Student;
import yve.entities.Task;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class FillDB {
    private List<String> values = new ArrayList<>();
    private final List<String> theme = new ArrayList<>();

    private final Student student = new Student();
    private final Task task = new Task();
    private final Progress progress = new Progress();
    public void setData(List<String> values){
        this.values = values;
    }

    public void fill(){
        int iterator = 0;

        for(String item : values.get(0).split(", ")){
            if (!item.equals("")) {
                theme.add(item);

            }
        }
        System.out.println();
    }

}
