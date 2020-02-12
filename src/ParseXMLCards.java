import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class ParseXMLCards {
    public Document getDoc(String filename) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = null;

        try {
            doc = db.parse(filename);
        } catch (Exception ex) {
            System.out.println("Failed to parse XML");
            ex.printStackTrace();
        }
        return doc;
    }

    public void readCardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");

        //Parse through all the cards in the xml
        for (int i = 0; i < cards.getLength(); i++) {
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            String b = card.getAttributes().getNamedItem("budget").getNodeValue();
            int budget = Integer.parseInt(b);
            System.out.printf("Card Name = %s, Budget = %d", cardName, budget);

            NodeList children = card.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {
                Node sub = children.item(j);

                // get on card roles/levels
                if ("part".equals(sub.getNodeName())) {
                    String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
                    String l = sub.getAttributes().getNamedItem("level").getNodeValue();
                    int level = Integer.parseInt(l);

                    System.out.printf("    Role = %s, Level = %d\n", roleName, level);
                } else if ("scene".equals(sub.getNodeName())) {
                    String scene = sub.getAttributes().getNamedItem("number").getNodeValue();
                    int sceneNumber = Integer.parseInt(scene);
                    System.out.printf(", Scene = %d\n",sceneNumber);
                }
            }
        }
    }
}

