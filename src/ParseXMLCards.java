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

        for (int i = 0; i < cards.getLength(); i++) {
            Node card = cards.item(i);
            String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
            System.out.println("Card Name = " + cardName);
        }
    }
}

