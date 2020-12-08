package cn.cst;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {


    private static final String DATABASE_NAME = "hello_mongodb";
    private static final String DATABASE_HOST = "localhost";
    private static final int DATABASE_PORT = 27017;
    private static final String CONNECTION_NAME = "core-data";
    private static final String FILE_PATH = Main.class.getClassLoader().getResource("data.yml").getPath();


    static Map<String, Object> properties;

    static List<User> getUserListFromYaml() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(FILE_PATH);
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(inputStream, Map.class);
        Object users = properties.get("users");
        String replace = users.toString().replace("=", ":");
        JSONArray array = new JSONArray(replace);
        List<User> list = jsonMapUser(array);
        return list;
    }


    static List<User> jsonMapUser(JSONArray array) {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("user");
            User user = User.builder().age(jsonObject.getInt("age")).name(jsonObject.getString("name")).build();
            list.add(user);
        }
        return list;
    }

    public static void main(String[] args) {
        try {
            MongoClient mongoClient = MongoDBUtil.getMongoClient(DATABASE_HOST, DATABASE_PORT);
            MongoDatabase mDatabase = mongoClient.getDatabase(DATABASE_NAME);
            MongoDBUtil.dropConnection(mDatabase, CONNECTION_NAME);
            MongoDBUtil.createConnection(mDatabase, CONNECTION_NAME);
            FindIterable<Document> documents = MongoDBUtil.findAllData(mDatabase, CONNECTION_NAME);
            MongoDBUtil.showAllData(documents);
            MongoDBUtil.insertMany(mDatabase, CONNECTION_NAME, getUserListFromYaml());
            documents = MongoDBUtil.findAllData(mDatabase, CONNECTION_NAME);
            MongoDBUtil.showAllData(documents);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}