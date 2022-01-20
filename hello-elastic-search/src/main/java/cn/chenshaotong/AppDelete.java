package cn.chenshaotong;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class AppDelete {
  public static void main(String[] args) throws IOException {
    System.out.println("test...");

    RestHighLevelClient restHighLevelClient =
        new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("pet");
    AcknowledgedResponse delete =
        restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

    System.out.println(delete.isAcknowledged());
    restHighLevelClient.close();
  }
}
