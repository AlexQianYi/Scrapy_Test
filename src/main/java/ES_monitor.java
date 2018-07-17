import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ES_monitor {

    TransportClient transportClient;

    // index lib name
    String index = "original_project_file";

    // type name
    String name = "project_file";

    @Before
    public void before(){
        /***
         * 1. configure clusters by setting
         */
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "elasticsearch") // clusters name
                .put("client.transport.sniff", true) // sniff function
                .build();


        /***
         * 2. create client
         * by setting, default cluster name is elasticsearch
         * tcp: 9300
         */
        transportClient = new TransportClient(settings);
        TransportAddress transportAddress = new InetSocketTransportAddress("192.168.1.200", 9300);
        transportClient.addTransportAddress(transportAddress);

        /***
         * 3. get cluster information
         */
        ImmutableList<DiscoveryNode> connectedNodes = transportClient.connectedNodes();
        for (DiscoveryNode discoveryNode : connectedNodes){
            System.out.println(discoveryNode.getHostAddress());
        }
    }

    /***
     * get doc information by prepareGet
     */
    @Test
    public void testGet(){
        GetResponse getResponse = transportClient.prepareGet(index, type, "1").get();
        System.out.println(getResponse.getSourceAsString());
    }

    /***
     * update doc by prepareUpdate
     * if doc not exist, return error
     */
    @Test
    public void testUpdate() throws IOException{
        XContentBuilder source = XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field("name", "will")
                                    .endObject();
        UpdateResponse updateResponse = transportClient.prepareUpdate(index, type, "3")
                                    .setDoc(source).get();
        System.out.println(updateResponse.getVersion());

    }


    /***
     * add doc by prepareIndex
     * input parameter json
     * update id at same time
     */
    @Test
    public void testIndexJson(){
        String source = "{\"name\":\"qill\",\"age\":33}";
        IndexResponse indexResponse = transportClient.prepareIndex(index, type, "4")
                                        .setSource(source).get();
        System.out.println(indexResponse.getVersion());
    }

    /***
     * add doc by prepareIndex
     * input parameter Map<String, Object>
     */
    @Test
    public void testIndexMap(){
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("name", "Alice");
        source.put("age", 18);
        IndexResponse indexResponse = transportClient.prepareIndex(index, type, "5")
                                        .setSource(source).get();
        System.out.println(indexResponse.getVersion());
    }

    /***
     * add doc by prepareIndex
     * input parameter javaBean
     * transfer javaBean as JSON
     * if error, throw Exception
     */
    @Test
    public void testIndexBean() throws JsonProcessingException{
        Student stu = new Student();
        stu.setName("Fresh");
        stu.setAge(22);

        ObjectMapper mapper = new ObjectMapper();
        String source = mapper.writeValueAsString(stu);

        IndexResponse indexResponse = transportClient.prepareIndex(index, type, "6")
                                        .setSource(source).get();

        System.out.println(indexResponse.getVersion());

    }

    /***
     * add doc by prepareIndex
     * input parameter XContentBuilder
     * throw IOException
     * throw ExecutionException
     * throw InterrputedException
     */
    @Test
    public void testIndexBuilder() throws IOException, InterruptedException, ExecutionException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field("name", "Avivi")
                                    .field("age", 30)
                                    .endObject();

        IndexResponse indexResponse = transportClient.prepareIndex(index, type, "7")
                                    .setSource(builder)
                                    .execute().get();

        System.out.println(indexResponse.getVersion());
    }

    /***
     * add doc by prepareIndex
     * input parameter key-value
     */
    @Test
    public void testIndexDirect(){
        IndexResponse indexResponse = transportClient.prepareIndex(index, type ,"8")
                                        .setSource("name", "GAGA", "age", 35).get();
        System.out.println(indexResponse.getVersion());
    }

    /***
     * delete doc by prepareIndex
     */
    @Test
    public void testDelete(){
        DeleteResponse deleteResponse = transportClient.prepareDelete(index, type, "9").get();
        System.out.println(deleteResponse.getVersion());

        // delete all records
        DeleteByQueryResponse deleteByQueryResponses = transportClient.prepareDeleteByQuery(index)
                                                        .setTypes(type)
                                                        .setQuery(QueryBuilders.matchAllQuery())
                                                        .set();
        System.out.println(deleteByQueryResponses.contextSize());
        System.out.println(deleteByQueryResponses.isContextEmpty());    //true
        System.out.println(deleteByQueryResponses.status().getStatus());

    }

    /***
     * delete index lib
     * CANNOT REVERSE!!!
     * CAREFUL!!
     */
    @Test
    public void testDeleteIndex(){
        DeleteIndexResponse deleteIndexResponse = transportClient.admin().indices()
                                                    .prepareDelete("original_project_file").get();
        System.out.println(deleteIndexResponse.isContextEmpty());
    }

    /***
     * get doc number by prepareCount
     */
    @Test
    public void testCount(){
        long count = transportClient.prepareCount(index).get().getCount();
        System.out.println(count);
    }

    /***
     * batching by prepareBulk
     */
    @Test
    public void testBulk() throws IOException{
        // 1. generate bulk
        BulkRequestBuilder bulk = transportClient.prepareBulk();

        // 2. add operation
        IndexRequest add = new IndexRequest(index, type, "10");
        add.source(XContentFactory.jsonBuilder()
                .startObject()
                .field("name", "Henrry")
                .field("age", 28)
                .endObject());

        // 3. delete operation
        DeleteRequest delete = new DeleteRequest(index, type,"4");

        // 4. update operation
        XContentBuilder source = XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field("name", "jack")
                                    .field("age", 17)
                                    .endObject();
        UpdateRequest update = new UpdateRequest(index, type, "8");
        update.doc(source);

        bulk.add(delete);
        bulk.add(add);
        bulk.add(update);

        // 5. execute bathing operation
        BulkResponse bulkResponse = bulk.get();
        if(bulkResponse.hasFailures()){

            BulkItemResponse[] items = bulkResponse.getItems();
            for (BulkItemResponse item: items){
                System.out.println(item.getFailureMessage());
            }
        }else {
            System.out.println("all execute succeed!");
        }

    }

    /***
     * search index by prepareSearch
     * setQuery(QueryBuilders.matchQuery("name", "jack"))
     * setSearchType(SearchType.QUERY_THEN_FETCH)
     */
    @Test
    public void testSearch(){
        SearchResponse searchResponse = transportClient.prepareSearch(index)
                                            .setTypes(type)
                                            // search all
                                            // .setQuery(QueryBuilders.matchAllQuery())
                                            .setQuery(QueryBuilders.matchQuery("name", "Avivi").operator(Operator.AND))
                                            // search according to conditions
                                            .setSearchType(SearchType.QUERY_THEN_FETCH)
                                            .setFrom(0).setSize(10)
                                            .addSort("age", SortOrder.DESC)
                                            .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);

        SearchHit[] searchHits = hits.hits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsString());
        }
    }

    /***
     * multiple search
     */
    @Test
    public void testSearchAndTimeout() {
        SearchResponse searchResponse = transportClient.prepareSearch(index, "origin_project_file")
                                            .setTypes(type, "teacher")
                                            .setQuery(QueryBuilders.matchAllQuery())
                                            .setSearchType(SearchType.QUERY_THEN_FETCH)
                                            .setTimeout("3")
                                            .get();
        SearchHits hits = searchResponse.getHits();

        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);
        SearchHit[] hits2 = hits.getHits();
        for (SearchHit searchHit : hits2) {
            System.out.println(searchHit.getSourceAsString());
        }
    }

    /***
     * Filter
     * lt <
     * gt >
     * lte <=
     * gte >=
     */
    @Test
    public void testFilter() {
        SearchResponse searchResponse = transportClient.prepareSearch(index).setTypes(type)
                                            .setQuery(QueryBuilders.matchAllQuery())
                                            .setSearchType(SearchType.QUERY_THEN_FETCH)
                                            .setPostFilter(FilterBuilders.rangeFilter("age").gte(18).lte(22))
                                            .setExplain(true)
                                            .get();
        SearchHits hits = searchResponse.getHits();

        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);
    }
}
