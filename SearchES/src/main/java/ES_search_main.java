import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.UnknownHostException;

public class ES_search_main {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        ES_utility utility = new ES_utility();
        TransportClient client = utility.getClient();

        ReadXML readXML = new ReadXML(client);
        String file_path = ES_utility.getSearchXML_file();
        QueryBuilder qb = readXML.getQueryRules2(file_path);

        readXML.Search(qb, "original_project_file", "doc");
    }
}
