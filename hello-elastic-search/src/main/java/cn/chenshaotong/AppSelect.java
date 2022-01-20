package cn.chenshaotong;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

public class AppSelect {
  public static void main(String[] args) throws IOException {
    System.out.println("test...");

    RestHighLevelClient restHighLevelClient =
        new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    GetIndexRequest createIndexRequest = new GetIndexRequest("user");
    GetIndexResponse getIndexResponse =
        restHighLevelClient.indices().get(createIndexRequest, RequestOptions.DEFAULT);

    System.out.println(getIndexResponse);
    restHighLevelClient.close();
  }
}
