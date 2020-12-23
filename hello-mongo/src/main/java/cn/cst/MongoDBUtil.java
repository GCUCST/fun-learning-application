package cn.cst;

import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.bson.Document;

@UtilityClass
public class MongoDBUtil {
  MongoClient mongoClient;

  MongoClient getMongoClient(String host, int port) {
    if (mongoClient == null) {
      mongoClient = new MongoClient(host, port);
      System.out.println("Connect to database successfully!");
    }
    return mongoClient;
  }

  void dropConnection(MongoDatabase mongoDatabase, String connectionName) {
    mongoDatabase.getCollection(connectionName).drop();
    System.out.println("drop connection " + connectionName);
  }

  void createConnection(MongoDatabase mongoDatabase, String connectionName) {
    mongoDatabase.createCollection(connectionName);
    System.out.println("create connection " + connectionName);
  }

  FindIterable findAllData(MongoDatabase mongoDatabase, String connectionName) {
    FindIterable<Document> documents = mongoDatabase.getCollection(connectionName).find();
    return documents;
  }

  void insertOne(MongoDatabase mongoDatabase, String connectionName, String obj) {
    MongoCollection<Document> collection = mongoDatabase.getCollection(connectionName);
    collection.insertOne(Document.parse(obj));
  }

  void showAllData(FindIterable<Document> documents) {
    for (Document document : documents) {
      System.out.println(document);
    }
  }

  public static void insertMany(
      MongoDatabase mDatabase, String connectionName, List<User> userListFromYaml) {
    List<Document> list = new ArrayList();
    for (int i = 0; i < userListFromYaml.size(); i++) {
      list.add(Document.parse(JSON.toJSONString(userListFromYaml.get(i))));
    }
    MongoCollection<Document> collection = mDatabase.getCollection(connectionName);
    collection.insertMany(list);
  }
}
