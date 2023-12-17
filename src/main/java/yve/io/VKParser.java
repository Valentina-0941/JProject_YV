package yve.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class VKParser {
    public static String getJson() throws URISyntaxException, IOException {
        Scanner scanner = new Scanner(new File("D:\\Document\\File\\vk.txt"));
        String token = scanner.nextLine();
        scanner.close();
        String groupID = "basicprogrammingrtf2023";
        String fields = "bdate,sex,country,city,education,schools,universities";

        String address = String.format(
                "https://api.vk.com/method/groups.getMembers?group_id=%s&fields=%s&access_token=%s&v=5.131",
                groupID, fields, token
        );
        URL url = new URI(address).toURL();
        scanner = new Scanner((InputStream) url.getContent());

        StringBuilder builder = new StringBuilder();
        while (scanner.hasNext()) builder.append(scanner.nextLine());
        scanner.close();
        return builder.toString();
    }

    public static List<HashMap<String, String>> parseJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(getJson());
            JsonNode responseNode = rootNode.path("response");

            JsonNode itemsNode = responseNode.path("items");
            List<HashMap<String, String>> list = new ArrayList<>();
            for (JsonNode item : itemsNode) {
                HashMap<String,String> map = new HashMap<>();

                String firstName = item.path("first_name").asText();
                map.put("first_name", firstName);

                String lastName = item.path("last_name").asText();
                map.put("last_name", lastName);

                int rawG = item.path("sex").asInt();
                String sex = rawG == 1 ? "Ж" : rawG == 2 ? "М" : "Н";
                map.put("sex", sex);

                String country = item.path("country").path("title").asText();
                map.put("country", country);

                String city = item.path("city").path("title").asText();
                map.put("city", city);

                String university = item.path("university_name").asText();
                map.put("university", university);

                String bdate = item.path("bdate").asText();
                map.put("bdate", bdate);

                String faculty = item.path("faculty_name").asText();
                map.put("faculty", faculty);

                boolean isClosed = item.path("is_closed").asBoolean();
                map.put("is_closed", Boolean.toString(isClosed));

                list.add(map);

//                System.out.println("Имя: " + lastName + " " + firstName);
//                System.out.println("Пол: " + sex);
//                System.out.println("Страна: " + country);
//                System.out.println("Город: " + city);
//                System.out.println("Образование: " + university);
//                System.out.println("Факультет: " + faculty);
//                System.out.println("Страница " + isClosed);
//                System.out.println();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
