package yve.io;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import yve.entities.Progress;
import yve.entities.Student;
import yve.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class Data {
    @Getter
    static List<Progress> progresses = new ArrayList<>();
    @Getter
    static List<Student> students = new ArrayList<>();
    @Getter
    static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            progresses = session.createQuery("FROM Progress", Progress.class).getResultList();
            students = session.createQuery("FROM Student", Student.class).getResultList();
            tasks = session.createQuery("FROM Task", Task.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sessionFactory.close();
        }
    }
}
