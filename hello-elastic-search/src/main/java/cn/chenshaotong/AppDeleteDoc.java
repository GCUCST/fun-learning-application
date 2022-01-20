package cn.chenshaotong;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class AppDeleteDoc {
  public static void main(String[] args) throws IOException {
    System.out.println("test...");

    RestHighLevelClient restHighLevelClient =
        new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    DeleteRequest deleteRequest = new DeleteRequest();
    deleteRequest.index("user").id("1005");

    DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    System.out.println(delete);

    restHighLevelClient.close();
  }
}
