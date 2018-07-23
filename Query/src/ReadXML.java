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

        NodeList ruleList = document.getElementsByTagName("")

        QueryRule rule = new QueryRule();



        NodeList ruleList = document.getElementsByTagName()

    }


}
