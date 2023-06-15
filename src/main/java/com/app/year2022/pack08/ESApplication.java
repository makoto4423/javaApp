package com.app.year2022.pack08;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Arrays;

/**
 *  7.17.5就开始提示废弃 RestHighLevelClient，注释部分就是用的官方推荐的java api client，但官方提供的demo用的是jdk17的语法，看不懂，
 *  自己摸索出部分语法，但是subAgg实在不知道怎么写
 *  翻到spring官方提供的整合，也是说用的是RestHighLevelClient，算了，不折腾了，能用就行
 */

public class ESApplication {

    public static void main(String[] args) {
        RequestOptions options = RequestOptions.DEFAULT;
        try (RestHighLevelClient highLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9200, null)))) {
//            UpdateRequest updateRequest = new UpdateRequest("product","Om8Zq4IB5nAWm-N4lmdZ");
//            updateRequest.setIfSeqNo(1);
//            updateRequest.setIfPrimaryTerm(2);
//            JSONObject obj  = new JSONObject();
//            obj.put("brandId",7);
//            updateRequest.doc(obj.toJSONString());
//            UpdateResponse response = highLevelClient.update(updateRequest, options);

//            IndexRequest indexRequest = new IndexRequest("product");
////            indexRequest.setIfSeqNo(3);
////            indexRequest.setIfPrimaryTerm(1);
//            indexRequest.id("hCOtwIIBNJ5jOb_RcxYQ");
//            indexRequest.create(false);
//            indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//            indexRequest.timeout(new TimeValue(20000));
//            JSONObject obj  = new JSONObject();
//            obj.put("master",3);
//            indexRequest.source(obj.toJSONString(), XContentType.JSON);
//            IndexResponse response = highLevelClient.index(indexRequest, options);

//            GetRequest request = new GetRequest("product");
//            request.id("hCOtwIIBNJ5jOb_RcxYQ");
//            request.version(6);
//            GetResponse response = highLevelClient.get(request, options);


            SearchRequest request = new SearchRequest("kibana_sample_data_logs");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.version(true);
            sourceBuilder.seqNoAndPrimaryTerm(true);
            BoolQueryBuilder booleanQueryBuilder = new BoolQueryBuilder();
            booleanQueryBuilder.must(new MatchQueryBuilder("message", "http"))
                    .should(new TermsQueryBuilder("miss", Arrays.asList("miss one", "miss two", "miss three")));
            sourceBuilder.query(booleanQueryBuilder);
            sourceBuilder.aggregation(AggregationBuilders.terms("referer").field("referer")
                    .subAggregation(AggregationBuilders.terms("sub_agg1").field("ip"))
                    .subAggregation(AggregationBuilders.topHits("top_hits")
                            .fetchSource(new String[]{"agent", "clientip"}, null).size(1))).size(10);
            booleanQueryBuilder.filter(new TermQueryBuilder("response",200))
                    .filter(new RangeQueryBuilder("machine.ram").from(200000).to(Integer.MAX_VALUE));
            sourceBuilder.collapse(new CollapseBuilder("referer")
                    .setInnerHits(new InnerHitBuilder().setName("abc")
                            .setSize(2).addSort(new FieldSortBuilder("clientip").order(SortOrder.DESC))));
            request.source(sourceBuilder);
            SearchResponse response = highLevelClient.search(request, options);
            for (SearchHit hit : response.getHits().getHits()) {
//                hit.getSourceAsMap().forEach((k, v) -> System.out.println("k ->" + k + "; v ->" + v));
                System.out.println(hit.getScore());
                System.out.println("---");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args) throws IOException {
//        // Create the low-level client
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 9200)).build();
//
//// Create the transport with a Jackson mapper
//        ElasticsearchTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//// And create the API client
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//        SearchResponse<JSONObject> response =  client.search(
//                req -> req.index("kibana_sample_data_logs")
//                .query(q -> q.bool(
//                        b -> b.must(new MatchQuery.Builder().field("message").query("http").build()._toQuery())
//                                .should(new TermQuery.Builder().field("machine.os").value("win xp")
//                                        .field("clientip").value("139.177.77.124").build()._toQuery())
//                        ))
//                        .aggregations("referer",
//                                new Aggregation()
//                ,
//                JSONObject.class);
//
//        System.out.println("match " + response.shards().total().intValue());
//        for(Hit<JSONObject> hit : response.hits().hits()){
//            assert hit.source() != null;
//            hit.source().forEach((k, v)->
//                    System.out.println("k ->" + k + "; v ->" + v));
//            System.out.println(hit.score());
//            System.out.println("---");
//        }
//        restClient.close();
}


