package yve;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import yve.io.Parser;
import yve.io.VKParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        List<HashMap<String, String>> list = VKParser.parseJson();

        Parser parser = new Parser(new File("table.xlsx"), list);

        var s = parser.getStudents();
        var t = parser.getTasks();
        var p = parser.getProgresses();
        int sl = s.size();
        int tl = t.size();
        int pl = p.size();

        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory factory = configuration.buildSessionFactory();
        StatelessSession session = factory.openStatelessSession();

        session.beginTransaction();
        for (int i = 0; i < sl; i++) {
            session.insert(s.get(i));
        }
        for (int i = 0; i < tl; i++) {
            session.insert(t.get(i));
        }
        for (int i = 0; i < pl; i++) {
            session.insert(p.get(i));
        }
        session.getTransaction().commit();

        session.close();
        factory.close();
    }
}
