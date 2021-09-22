package cn.chenshaotong;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

public class AppSelectDocQuery {
    public static void main(String[] args) throws IOException {
        System.out.println("test...");

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost",9200,"http"))
        );

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("user");
        //全量查询
//        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchQuery("name", "CST"));
//        SearchRequest source = searchRequest.source(query);
//        SearchResponse search = client.search(source, RequestOptions.DEFAULT);
//        System.out.println(search.getHits());
//        System.out.println(search.getTook());
//        for (SearchHit h :search.getHits()){
//            System.out.println(h.getSourceAsString());
//        }
//        //条件查询
//        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.termQuery("name", "CST1"));
//        query.sort("age", SortOrder.DESC);
//        SearchRequest source = searchRequest.source(query);
//        SearchResponse search = client.search(source, RequestOptions.DEFAULT);
//        System.out.println(search.getHits());
//        System.out.println(search.getTook());
//        for (SearchHit h :search.getHits()){
//            System.out.println(h.getSourceAsString());
//        }

        System.out.println("去年麦克雷");
        //模糊查询
        SearchSourceBuilder builder = new SearchSourceBuilder() ;
        FuzzyQueryBuilder fuzzyQueryBuilder = QueryBuilders.fuzzyQuery("name", "cst").fuzziness(Fuzziness.AUTO);
        builder.query(fuzzyQueryBuilder);
        searchRequest.source(builder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        System.err.println(response.getTotalShards());
        System.err.println(response.getTook());


        for (SearchHit h :response.getHits()){
            System.out.println(h.getSourceAsString());
        }





        client.close();


    }
}
