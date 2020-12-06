package cn.cst;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Main {
    public static void main(String[] args) {
        try {
            //连接MongoDB服务器，端口号为27017
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            //连接数据库
            MongoDatabase mDatabase = mongoClient.getDatabase("hello_database");
            mDatabase.getCollection("core-data").drop();
            mDatabase.createCollection("core-data");
            mDatabase.getCollection("core-data").insertOne(Document.parse("{\"name\":\"Chen Shaotong\"}"));
            System.out.println("Connect to database successfully!");
            System.out.println("MongoDatabase info is : " + mDatabase.getName());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}