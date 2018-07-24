import org.elasticsearch.client.transport.TransportClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ES_fliter_main {

    public static void main(String[] args) throws IOException {

        /*
        ArrayList file_content = new ArrayList();
        ReadProjectFile readProjectFile = new ReadProjectFile();
        file_content = readProjectFile.readFileByLines("/Users/yiqian/Documents/GitHub/Scrapy_Test/Java_Fliter/src/main/java/Flitered_Doc.txt");

        for(int i=0; i<file_content.size(); i++){
            System.out.println(file_content.get(i));
        }*/

        ES_utility utility = new ES_utility();
        TransportClient client = utility.getClient();

        ES_SearchAll searchAll = new ES_SearchAll(client);
        List<ProRecord> ReadRecord = searchAll.SearchAll();

        ES_Insert insert = new ES_Insert(client);
        for(int i = 0; i<ReadRecord.size(); i++){
            insert.insert(ReadRecord.get(i).getID(), ReadRecord.get(i), "original_project_file", "doc");
        }

    }
}
