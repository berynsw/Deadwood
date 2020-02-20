// Based on example code provided Dr. Moushumi Sharmin

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ParseXML{
        // building a document from the XML file
        // returns a Document object after loading the book.xml file.
        public Document getDocFromFile(String filename) throws ParserConfigurationException{
           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           DocumentBuilder db = dbf.newDocumentBuilder();
           Document doc = null;

           try{
               doc = db.parse(filename);
           } catch (Exception ex){
               System.out.println("XML parse failure");
               ex.printStackTrace();
           }
           return doc;

        }

        // returns office
        public Room readOfficeData(Document d) {
            Element root = d.getDocumentElement();
            //each room
            NodeList roomNodes = root.getChildNodes();
            List<String> neighbors = new ArrayList<>();
            for (int i = 0; i < roomNodes.getLength(); i++) {
                Node room = roomNodes.item(i);
                if("office".equals(room.getNodeName())){
                    //children
                    NodeList children = room.getChildNodes();
                    for (int j=0; j< children.getLength(); j++){
                        Node sub = children.item(j);
                        NodeList subsub = sub.getChildNodes();
                        for (int k=0; k< subsub.getLength(); k++){
                            Node n = subsub.item(k);

                            //neighbors
                            if("neighbor".equals(n.getNodeName())){
                                String s = n.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                                neighbors.add(s);
                            }
                        }
                    }
                }
            }
            return new Room("office", neighbors, false);
        }

    // returns office
    public Room readTrailerData(Document d) {
        Element root = d.getDocumentElement();
        //each room
        NodeList roomNodes = root.getChildNodes();
        List<String> neighbors = new ArrayList<>();
        for (int i = 0; i < roomNodes.getLength(); i++) {
            Node room = roomNodes.item(i);
            if("trailer".equals(room.getNodeName())){
                //children
                NodeList children = room.getChildNodes();
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);
                    NodeList subsub = sub.getChildNodes();
                    for (int k=0; k< subsub.getLength(); k++){
                        Node n = subsub.item(k);

                        //neighbors
                        if("neighbor".equals(n.getNodeName())){
                            String s = n.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                            neighbors.add(s);
                        }
                    }
                }
            }
        }
        return new Room("trailer", neighbors, false);
    }

        // populates set data
        public HashMap<String, Set> readSetData(Document d){
            HashMap<String, Set> sets = new HashMap<>();
            Element root = d.getDocumentElement();
            //each room
            NodeList roomNodes = root.getChildNodes();
            for (int i=0; i<roomNodes.getLength();i++){
                Node room = roomNodes.item(i);

                List<String> neighbors = new ArrayList<>();
                //sets
                if("set".equals(room.getNodeName())){
                    //name
                    String setName = room.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                    List<Role> roles = new ArrayList<>();
                    int shots = 0;
                    NodeList setChild = room.getChildNodes();
                    for (int j=0; j< setChild.getLength(); j++){
                        Node sub = setChild.item(j);
                        NodeList subsub = sub.getChildNodes();
                        for (int k=0; k< subsub.getLength(); k++){
                            Node n = subsub.item(k);
                            //neighbors
                            if("neighbor".equals(n.getNodeName())){
                                String s = n.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                                neighbors.add(s);
                            }
                            //parts
                            if("part".equals(n.getNodeName())){
                                String s = n.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
                                int rank = Integer.parseInt(n.getAttributes().getNamedItem("level").getNodeValue());
                                roles.add(new Role(s, rank, false));
                            }
                            //takes
                            if("take".equals(n.getNodeName())){
                                shots++;
                            }
                        }
                    }
                    //add a new room
                    sets.put(setName, new Set(setName, shots, roles, neighbors, true));
                }
            }
            return sets;
        }



        //cards need to be added to deck structure
        public void readCardData(Document d, Stack<Card> deck) {
            Element root = d.getDocumentElement();
            NodeList cards = root.getElementsByTagName("card");

            //Parse through all the cards in the xml
            for (int i = 0; i < cards.getLength(); i++) {

                Node card = cards.item(i);
                String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
                String b = card.getAttributes().getNamedItem("budget").getNodeValue();
                int budget = Integer.parseInt(b);

                //Card c = new Card(cardName, budget, );
                List<Role> roles = new ArrayList<>();

                NodeList children = card.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    Node sub = children.item(j);

                    // get on card roles/levels
                    if ("part".equals(sub.getNodeName())) {
                        String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
                        String l = sub.getAttributes().getNamedItem("level").getNodeValue();
                        int rank = Integer.parseInt(l);

                        Role r = new Role(roleName, rank, true);
                        roles.add(r);

                    } else if ("scene".equals(sub.getNodeName())) {
                        String scene = sub.getAttributes().getNamedItem("number").getNodeValue();
                        int sceneNumber = Integer.parseInt(scene);
                    }
                }
                Card c = new Card(cardName, roles, budget);
                deck.add(c);
            }
        }

}//class