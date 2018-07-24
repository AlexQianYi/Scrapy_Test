import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    public static QueryBuilder getQueryRules(String fileName) throws IOException, SAXException {

        document = db.parse(fileName);

        NodeList rule_list = document.getElementsByTagName("Rule");
        org.w3c.dom.Node node = rule_list.item(0);

        NamedNodeMap namedNodeMap = node.getAttributes();

        //String project_name = namedNodeMap.getNamedItem("project_name").getTextContent();
        String project_location = namedNodeMap.getNamedItem("project_location").getTextContent();

        System.out.println(project_location);

        MatchPhraseQueryBuilder mpq_project_location = QueryBuilders
                                    .matchPhraseQuery("project_location", project_location);

        QueryBuilder qb = QueryBuilders.boolQuery()
                            .must(mpq_project_location);

        return qb;

    }

    private void Search(QueryBuilder qb, String index, String type) {

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
