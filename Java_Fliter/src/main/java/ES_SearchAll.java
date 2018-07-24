
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import java.net.UnknownHostException;
import java.net.InetAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.*;

public class ES_SearchAll {

    TransportClient client;

    public ES_SearchAll(TransportClient client) {

        this.client = client;

    }

    public List<ProRecord> SearchAll() throws UnknownHostException{

        //构造查询对象
        QueryBuilder query= QueryBuilders.matchAllQuery();
        //搜索结果存入SearchResponse
        SearchResponse response=client.prepareSearch("original_project_file")
                .setQuery(query) //设置查询器
                .setSize(3)      //一次查询文档数
                .get();
        SearchHits hits=response.getHits();

        List<ProRecord> Record = new ArrayList<ProRecord>();
        for(SearchHit hit:hits){
            ProRecord tempRecord = new ProRecord();

            System.out.println("source:"+hit.getSourceAsString());
            System.out.println("index:"+hit.getIndex());
            System.out.println("type:"+hit.getType());
            System.out.println("id:"+hit.getId());
            tempRecord.setID(hit.getId());
            //遍历文档的每个字段
            Map<String,Object> map=hit.getSourceAsMap();
            for(String key:map.keySet()){
                //System.out.println(key+"="+map.get(key).getClass());
                if (key == "project_name") {
                    tempRecord.setProject_name(map.get(key));
                    System.out.println(tempRecord.getProject_name());
                }
                if (key == "project_url"){
                    tempRecord.setProject_url(map.get(key));
                    System.out.println(tempRecord.getProject_url());
                }
                if (key == "project_category") {
                    tempRecord.setProject_category(map.get(key));
                    System.out.println(tempRecord.getProject_category());
                }
                if (key == "project_location") {
                    tempRecord.setProject_location(map.get(key));
                    System.out.println(tempRecord.getProject_location());
                }
                if (key == "project_manager_tel") {
                    tempRecord.setProject_manager_tel(map.get(key));
                    System.out.println(tempRecord.getProject_manager_tel());
                }
                if (key == "project_file_price") {
                    tempRecord.setProject_file_price(map.get(key));
                    System.out.println(tempRecord.getProject_file_price());
                }
                if (key == "project_file_location") {
                    tempRecord.setProject_file_location(map.get(key));
                    System.out.println(tempRecord.getProject_file_location());
                }
                if (key == "project_publish_time") {
                    tempRecord.setProject_publish_time(map.get(key));
                    System.out.println(tempRecord.getProject_publish_time());
                }
                if (key == "project_bit_location") {
                    tempRecord.setProject_bit_location(map.get(key));
                    System.out.println(tempRecord.getProject_bit_location());
                }
                if (key == "project_file_time") {
                    tempRecord.setProject_file_time(map.get(key));
                    System.out.println(tempRecord.getProject_file_time());
                }
                if (key == "project_manager") {
                    tempRecord.setProject_manager(map.get(key));
                    System.out.println(tempRecord.getProject_manager());
                }
                if (key == "project_apartment") {
                    tempRecord.setProject_apartment(map.get(key));
                    System.out.println(tempRecord.getProject_apartment());
                }
                if (key == "project_bit_time") {
                    tempRecord.setProject_bit_time(map.get(key));
                    System.out.println(tempRecord.getProject_bit_time());
                }
            }
            System.out.println("--------------------");
            System.out.println("Is there anything wrong with this record? Yes/No");
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            System.out.println(s.getClass());

            while(s.compareToIgnoreCase("Yes") != 0 && s.compareToIgnoreCase("No") != 0){
                System.out.println("--------------------");
                System.out.println("Wrong choice!!! Input again");
                System.out.println("--------------------");
                s = in.nextLine();
            }
            // the record is correct
            if (s.compareToIgnoreCase("No") == 0) {
                System.out.println("--------------------");
                System.out.println("Write record in ES....");
                System.out.println("--------------------");
                Record.add(tempRecord);
            }
            // the record is wrong
            else {
                ProRecord changedRecord = Correct(tempRecord);
                System.out.println("--------------------");
                System.out.println("Write changed record in ES....");
                System.out.println("--------------------");
                Record.add(changedRecord);
            }

        }
        return Record;
    }

    private ProRecord Correct(ProRecord tempRecord){
        ProRecord changedRecord = tempRecord;
        Scanner in = new Scanner(System.in);

        String Continue = "Yes";
        while (Continue.compareToIgnoreCase("Yes") == 0){
            System.out.println("--------------------");
            System.out.println("Input the Key ....");
            System.out.println("--------------------");
            String s = in.nextLine();
            Set<String>  KeySet = new HashSet<String>(Arrays.asList(ES_utility.getKeyArray()));
            while (!KeySet.contains(s)){
                System.out.println("--------------------");
                System.out.println("Wrong KEY!!! Input again");
                System.out.println("--------------------");
                s = in.nextLine();
            }

                changedRecord = CorrectRecord(tempRecord, s);
                System.out.println("--------------------");
                System.out.println("Continue Correct? Yes/No");
                System.out.println("--------------------");
                Continue = in.nextLine();
        }

        return changedRecord;
    }

    private ProRecord CorrectRecord(ProRecord tempRecord, String key){

        System.out.println("--------------------");
        System.out.println("Input the key: " + key + " contains: ");
        System.out.println("--------------------");
        Scanner in = new Scanner(System.in);
        String value = in.nextLine();

        if (key.compareToIgnoreCase("project_url") == 0){
            tempRecord.setProject_url(value);
            System.out.println(tempRecord.getProject_url());
        }
        if (key == "project_name") {
            tempRecord.setProject_name(value);
            System.out.println(tempRecord.getProject_name());
        }
        if (key == "project_category") {
            tempRecord.setProject_category(value);
            System.out.println(tempRecord.getProject_category());
        }
        if (key.compareToIgnoreCase("project_location") == 0) {
            tempRecord.setProject_location(value);
            System.out.println(tempRecord.getProject_location());
        }
        if (key == "project_manager_tel") {
            tempRecord.setProject_manager_tel(value);
            System.out.println(tempRecord.getProject_manager_tel());
        }
        if (key == "project_file_price") {
            tempRecord.setProject_file_price(value);
            System.out.println(tempRecord.getProject_file_price());
        }
        if (key == "project_file_location") {
            tempRecord.setProject_file_location(value);
            System.out.println(tempRecord.getProject_file_location());
        }
        if (key == "project_publish_time") {
            tempRecord.setProject_publish_time(value);
            System.out.println(tempRecord.getProject_publish_time());
        }
        if (key == "project_bit_location") {
            tempRecord.setProject_bit_location(value);
            System.out.println(tempRecord.getProject_bit_location());
        }
        if (key == "project_file_time") {
            tempRecord.setProject_file_time(value);
            System.out.println(tempRecord.getProject_file_time());
        }
        if (key == "project_manager") {
            tempRecord.setProject_manager(value);
            System.out.println(tempRecord.getProject_manager());
        }
        if (key == "project_apartment") {
            tempRecord.setProject_apartment(value);
            System.out.println(tempRecord.getProject_apartment());
        }
        if (key == "project_bit_time") {
            tempRecord.setProject_bit_time(value);
            System.out.println(tempRecord.getProject_bit_time());
        }

        return tempRecord;
    }

}