package cn.chenshaotong;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class AppSelectDoc {
  public static void main(String[] args) throws IOException {
    System.out.println("test...");

    RestHighLevelClient client =
        new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    GetRequest getRequest = new GetRequest();

    GetRequest user = getRequest.index("user").id("1003");

    GetResponse response = client.get(user, RequestOptions.DEFAULT);

    System.out.println(response.getSourceAsString());
    client.close();
  }
}
