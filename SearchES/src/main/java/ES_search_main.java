import org.elasticsearch.client.transport.TransportClient;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.UnknownHostException;

public class ES_search_main {

    public static void main(String[] args) throws IOException, SAXException {

        ES_utility utility = new ES_utility();
        TransportClient client = utility.getClient();

        ReadXML readXML = new ReadXML(client);
        String file_path = ES_utility.getSearchXML_file();
        readXML.getQueryRules(file_path);
    }
}
