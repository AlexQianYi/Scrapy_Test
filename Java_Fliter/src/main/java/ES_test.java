import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ES_test {

    public static void main(String[] args) throws UnknownHostException {
        // 设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "Bit_Project").build();
        // 创建client
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        // 搜索数据
        GetResponse response = client.prepareGet("original_project_file", "doc", "PkjppWQB_SVtOb9NeBt6").execute().actionGet();
        // 输出结果
        System.out.println(response.getSourceAsString());
        System.out.println(response.getSourceAsString().indexOf("project_url"));
        // 关闭client
        client.close();
    }
}