import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {

    private static DocumentBuilderFactory dbFactory = null;
    private static DocumentBuilder db = null;
    private static Document document = null;
    private static List<QueryRule> queryRules = null;

    private TransportClient client;

    public ReadXML (TransportClient client){
        this.client = client;
    }

    static{
        try{
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /***
     * TODO: change the rule to combine the keywords here
     * @param fileName
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     *
     * get the ES search query rules here
     */
    public static QueryBuilder getQueryRules(String fileName) throws ParserConfigurationException, IOException, SAXException {

        File f = new File(fileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(f);
        NodeList nl = doc.getElementsByTagName("");


        String project_name = doc.getElementsByTagName("project_name").item(0).getFirstChild().getNodeValue();
        String project_category = doc.getElementsByTagName("project_category").item(0).getFirstChild().getNodeValue();
        String project_apartment = doc.getElementsByTagName("project_apartment").item(0).getFirstChild().getNodeValue();
        String project_location = doc.getElementsByTagName("project_location").item(0).getFirstChild().getNodeValue();
        String project_publish_time = doc.getElementsByTagName("project_publish_time").item(0).getFirstChild().getNodeValue();
        String project_file_time = doc.getElementsByTagName("project_file_time").item(0).getFirstChild().getNodeValue();
        String project_file_price = doc.getElementsByTagName("project_file_price").item(0).getFirstChild().getNodeValue();
        String project_file_location = doc.getElementsByTagName("project_file_location").item(0).getFirstChild().getNodeValue();
        String project_bit_time = doc.getElementsByTagName("project_bit_time").item(0).getFirstChild().getNodeValue();
        String project_bit_location = doc.getElementsByTagName("project_bit_location").item(0).getFirstChild().getNodeValue();
        String project_except_amount = doc.getElementsByTagName("project_except_amount").item(0).getFirstChild().getNodeValue();
        String project_manager = doc.getElementsByTagName("project_manager").item(0).getFirstChild().getNodeValue();
        String project_manager_tel = doc.getElementsByTagName("project_manager_tel").item(0).getFirstChild().getNodeValue();
        String empty = doc.getElementsByTagName("empty").item(0).getFirstChild().getNodeValue();

        System.out.println(project_name);
        System.out.println(project_category);
        System.out.println(project_apartment);
        System.out.println(project_location);
        System.out.println(project_publish_time);
        System.out.println(project_file_time);
        System.out.println(project_file_price);
        System.out.println(project_file_location);
        System.out.println(project_bit_time);
        System.out.println(project_bit_location);
        System.out.println(project_except_amount);
        System.out.println(project_manager);
        System.out.println(project_manager_tel);


        QueryBuilder qb_project_name = null;
        QueryBuilder qb_project_category = null;
        QueryBuilder qb_project_apartment = null;
        QueryBuilder qb_project_location = null;
        QueryBuilder qb_project_publish_time = null;
        QueryBuilder qb_project_file_time = null;
        QueryBuilder qb_project_file_price = null;
        QueryBuilder qb_project_file_location = null;
        QueryBuilder qb_project_bit_time = null;
        QueryBuilder qb_project_bit_location = null;
        QueryBuilder qb_project_except_amount = null;
        QueryBuilder qb_project_manager = null;
        QueryBuilder qb_project_manager_tel = null;

        if (!project_name.equals(empty)) {
            qb_project_name = QueryBuilders.boolQuery().
                    must(QueryBuilders.matchPhraseQuery("project_name", project_name));
        }else{
            qb_project_name = QueryBuilders.boolQuery();
        }

        if (!project_category.equals(empty)) {
            qb_project_category = QueryBuilders.boolQuery()
                    .must(qb_project_name)
                    .must(QueryBuilders.matchPhraseQuery("project_category", project_category));
        }else{
            qb_project_category = QueryBuilders.boolQuery()
                    .must(qb_project_name);
        }

        if (!project_apartment.equals(empty)) {
            qb_project_apartment = QueryBuilders.boolQuery()
                    .must(qb_project_category)
                    .must(QueryBuilders.matchPhraseQuery("project_apartment", project_apartment));
        }else{
            qb_project_apartment = QueryBuilders.boolQuery()
                    .must(qb_project_category);
        }

        if (!project_location.equals(empty)) {
            qb_project_location = QueryBuilders.boolQuery()
                    .must(qb_project_apartment)
                    .must(QueryBuilders.matchPhraseQuery("project_location", project_location));
        }else{
            qb_project_location = QueryBuilders.boolQuery()
                    .must(qb_project_apartment);
        }

        if (!project_publish_time.equals(empty)) {
            qb_project_publish_time = QueryBuilders.boolQuery()
                    .must(qb_project_location)
                    .must(QueryBuilders.matchPhraseQuery("project_publish_time", project_publish_time));
        }else{
            qb_project_publish_time = QueryBuilders.boolQuery()
                    .must(qb_project_location);
        }

        if (!project_file_time.equals(empty)) {
            qb_project_file_time = QueryBuilders.boolQuery()
                    .must(qb_project_publish_time)
                    .must(QueryBuilders.matchPhraseQuery("project_file_time", project_file_time));
        }else{
            qb_project_file_time = QueryBuilders.boolQuery()
                    .must(qb_project_publish_time);
        }

        if (!project_file_price.equals(empty)) {
            qb_project_file_price = QueryBuilders.boolQuery()
                    .must(qb_project_file_time)
                    .must(QueryBuilders.matchPhraseQuery("project_file_price", project_file_price));
        }else{
            qb_project_file_price = QueryBuilders.boolQuery()
                    .must(qb_project_file_time);
        }

        if (!project_file_location.equals(empty)) {
            qb_project_file_location = QueryBuilders.boolQuery()
                    .must(qb_project_file_price)
                    .must(QueryBuilders.matchPhraseQuery("project_file_location", project_file_location));
        }else{
            qb_project_file_location = QueryBuilders.boolQuery()
                    .must(qb_project_file_price);
        }

        if (!project_bit_time.equals(empty)) {
            qb_project_bit_time = QueryBuilders.boolQuery()
                    .must(qb_project_file_location)
                    .must(QueryBuilders.matchPhraseQuery("project_bit_time", project_bit_time));
        }else{
            qb_project_bit_time = QueryBuilders.boolQuery()
                    .must(qb_project_file_location);
        }

        if (!project_bit_location.equals(empty)) {
            qb_project_bit_location = QueryBuilders.boolQuery()
                    .must(qb_project_bit_time)
                    .must(QueryBuilders.matchPhraseQuery("project_bit_location", project_bit_location));
        }else{
            qb_project_bit_location = QueryBuilders.boolQuery()
                    .must(qb_project_bit_time);
        }

        if (!project_except_amount.equals(empty)) {
            qb_project_except_amount = QueryBuilders.boolQuery()
                    .must(qb_project_bit_location)
                    .must(QueryBuilders.matchPhraseQuery("project_except", project_except_amount));
        }else{
            qb_project_except_amount = QueryBuilders.boolQuery()
                    .must(qb_project_bit_location);
        }

        if (!project_manager.equals(empty)) {
            qb_project_manager = QueryBuilders.boolQuery()
                    .must(qb_project_except_amount)
                    .must(QueryBuilders.matchPhraseQuery("project_manager", project_manager));
        }else {
            qb_project_manager = QueryBuilders.boolQuery()
                    .must(qb_project_except_amount);
        }

        if (!project_manager_tel.equals(empty)) {
            qb_project_manager_tel = QueryBuilders.boolQuery()
                    .must(qb_project_manager)
                    .must(QueryBuilders.matchPhraseQuery("project_manager_tel", project_manager_tel));
        }else {
            qb_project_manager_tel = QueryBuilders.boolQuery()
                    .must(qb_project_manager);
        }

        return qb_project_manager_tel;

    }

    /***
     * TODO: change ES search method/strategy here
     *
     * @param qb
     * @param index
     * @param type
     */
    public void Search(QueryBuilder qb, String index, String type) {

        SearchRequestBuilder responsebuilder = this.client
                .prepareSearch(index).setTypes(type);
        SearchResponse response = responsebuilder
                    .setQuery(qb)
                    .setFrom(0).setSize(50)
                    .setExplain(true).execute().actionGet();
            SearchHits hits = response.getHits();
            for(int i=0; i<hits.getHits().length; i++){
                System.out.println(hits.getHits()[i].getSourceAsString());
            }

    }


}
