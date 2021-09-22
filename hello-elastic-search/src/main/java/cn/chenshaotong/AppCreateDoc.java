package cn.chenshaotong;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class AppCreateDoc {
    public static void main(String[] args) throws IOException {
        System.out.println("test...");

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http"))
        );

        IndexRequest request = new IndexRequest();
        request.index("user").id("1001");

        User user = User.builder().name("chenst").age(25).desc("软件开发者").sex("男").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String userStr = objectMapper.writeValueAsString(user);
        request.source(userStr, XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);
        System.out.println(response.getResult());


        restHighLevelClient.close();


    }
}
