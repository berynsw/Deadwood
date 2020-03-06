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
        public Office readOfficeData(Document d) {
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
            return new Office("office", neighbors);
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
        return new Room("trailer", neighbors, 991, 248, 194, 201);
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
                    List<Shot> shotList = new ArrayList<>();

                    int shots = 0;
                    int x = 0;
                    int y = 0;
                    int h = 0;
                    int w = 0;
                    NodeList setChild = room.getChildNodes();
                    for (int j=0; j< setChild.getLength(); j++){
                        Node sub = setChild.item(j);


                        if ("area".equals(sub.getNodeName())) {
                            String xS = sub.getAttributes().getNamedItem("x").getNodeValue();
                            String yS = sub.getAttributes().getNamedItem("y").getNodeValue();
                            String hS = sub.getAttributes().getNamedItem("h").getNodeValue();
                            String wS = sub.getAttributes().getNamedItem("w").getNodeValue();
                            x = Integer.parseInt(xS);
                            y = Integer.parseInt(yS);
                            h = Integer.parseInt(hS);
                            w = Integer.parseInt(wS);
                        }

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

                                // get coordinates of cards on board
                                Element e = (Element)n;
                                NodeList areas = e.getElementsByTagName("area");
                                Element area = (Element)areas.item(0);

                                String xS = area.getAttribute("x");
                                String yS = area.getAttribute("y");
                                int xVal = Integer.parseInt(xS);
                                int yVal = Integer.parseInt(yS);
                                roles.add(new Role(s, rank, false, xVal, yVal));
                            }
                            //takes
                            if("take".equals(n.getNodeName())){
                                shots++;

                                // get coordinates of shot counters

                                Element e = (Element)n;
                                NodeList areas = e.getElementsByTagName("area");
                                Element area = (Element)areas.item(0);

                                String xS = area.getAttribute("x");
                                String yS = area.getAttribute("y");
                                int xVal = Integer.parseInt(xS);
                                int yVal = Integer.parseInt(yS);

                                Shot shot = new Shot(xVal, yVal);
                                shotList.add(shot);
                            }
                        }
                    }
                    //add a new room
                    sets.put(setName, new Set(setName, shots, roles, neighbors, x, y, h, w, shotList));
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
                String png = card.getAttributes().getNamedItem("img").getNodeValue();
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

                        // get coordinates of roles on card
                        Element e = (Element)sub;
                        NodeList areas = e.getElementsByTagName("area");
                        Element area = (Element)areas.item(0);
                        String xS = area.getAttribute("x");
                        String yS = area.getAttribute("y");
                        int xVal = Integer.parseInt(xS);
                        int yVal = Integer.parseInt(yS);

                        Role r = new Role(roleName, rank, true, xVal, yVal);
                        roles.add(r);

                    } else if ("scene".equals(sub.getNodeName())) {
                        String scene = sub.getAttributes().getNamedItem("number").getNodeValue();
                        int sceneNumber = Integer.parseInt(scene);
                    }
                }
                String image = "images/cards/"+png;
                Card c = new Card(cardName, roles, budget, image);
                deck.add(c);
            }
        }

}
