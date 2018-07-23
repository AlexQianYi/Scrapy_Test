import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {

    private static DocumentBuilderFactory dbFactory = null;
    private static DocumentBuilder db = null;
    private static Document document = null;
    private static List<QueryRule> queryRules = null;

    static{
        try{
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static List<QueryRule> getQueryRules(String fileName) throws IOException, SAXException {

        document = db.parse(fileName);

        NodeList project_name_list = document.getElementsByTagName("project_name");
        NodeList project_category_list = document.getElementsByTagName("project_category");
        NodeList project_apartment_list = document.getElementsByTagName("project_apartment");
        NodeList project_location_list = document.getElementsByTagName("project_location");
        NodeList project_publish_time_list = document.getElementsByTagName("project_publish_time");
        NodeList project_file_time_list = document.getElementsByTagName("project_file_time");
        NodeList project_file_price_list = document.getElementsByTagName("project_file_price");
        NodeList project_file_location_list = document.getElementsByTagName("project_file_location");
        NodeList project_bit_time_list = document.getElementsByTagName("project_bit_time");
        NodeList project_bit_location_list = document.getElementsByTagName("project_bit_location");
        NodeList project_except_amount_list = document.getElementsByTagName("project_except_amount");
        NodeList project_manager_list = document.getElementsByTagName("project_manager");
        NodeList project_manager_tel_list = document.getElementsByTagName("project_manager_tel");



    }


}
