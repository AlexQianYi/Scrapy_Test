import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class ES_Insert {

    TransportClient client;

    public ES_Insert(TransportClient client){
        this.client = client;
    }


    public boolean insert(String id, ProRecord record, String index, String type) throws IOException {


        // json format

        XContentBuilder source = XContentFactory.jsonBuilder()
                .startObject()
                    .field("project_name", record.getProject_name())
                    .field("project_category", record.getProject_category())
                    .field("project_apartment", record.getProject_apartment())
                    .field("project_location", record.getProject_location())
                    .field("project_publish_time", record.getProject_publish_time())
                    .field("project_file_time", record.getProject_file_time())
                    .field("project_file_price", record.getProject_file_price())
                    .field("project_file_location", record.getProject_file_location())
                    .field("project_bit_time", record.getProject_bit_time())
                    .field("project_bit_location", record.getProject_bit_location())
                    .field("project_except_amount", record.getProject_except_amount())
                    .field("project_manager", record.getProject_manager())
                    .field("project_manager_tel", record.getProject_manager_tel())
                .endObject();


        System.out.println(source);


        IndexResponse response = client.prepareIndex(index, type, id)
                        .setSource(source)
                        .get();
        System.out.println(response.status());

        String ind = response.getIndex();
        String t = response.getType();
        String ID = response.getId();
        long version = response.getVersion();
        System.out.println(ind + " : " + t + ": " + ID + ": " + version);

        return true;
    }

}
