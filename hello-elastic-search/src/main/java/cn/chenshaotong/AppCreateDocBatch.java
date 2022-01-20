package cn.chenshaotong;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

public class AppCreateDocBatch {
  public static void main(String[] args) throws IOException {
    System.out.println("test...");

    RestHighLevelClient restHighLevelClient =
        new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(
        new IndexRequest()
            .index("user")
            .id("1002")
            .source(XContentType.JSON, "name", "CST1", "age", 25, "sex", "男"),
        new IndexRequest()
            .index("user")
            .id("1003")
            .source(XContentType.JSON, "name", "CST12", "age", 23, "sex", "男"),
        new IndexRequest()
            .index("user")
            .id("1004")
            .source(XContentType.JSON, "name", "wangwu", "age", 20, "sex", "女"),
        new IndexRequest()
            .index("user")
            .id("1005")
            .source(XContentType.JSON, "name", "cst23", "age", 35, "sex", "男"));

    BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    System.out.println(bulk.getItems());

    restHighLevelClient.close();
  }
}
