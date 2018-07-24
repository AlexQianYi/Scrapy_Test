import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;


public class ES_utility {

    private  TransportClient client;
    private static String[] KeyArray = {"project_name", "project_category", "project_apartment",
                        "project_location", "project_publish_time", "project_file_time",
                        "project_file_price", "project_file_location", "project_bit_time",
                        "project_bit_location", "project_except_amount", "project_manager",
                        "project_manager_tel"};

    public ES_utility() throws UnknownHostException{

        connectES();

    }

    private void connectES() throws  UnknownHostException{
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "Bit_Project").build();
        // 创建client
        this.client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    public TransportClient getClient(){
        return this.client;
    }

    public static String[] getKeyArray(){
        return KeyArray;
    }
}
