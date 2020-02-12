// Based on example code provided Dr. Moushumi Sharmin

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParseRooms{
        // building a document from the XML file
        // returns a Document object after loading the book.xml file.
        public static Document getDocFromFile(String filename)
        throws ParserConfigurationException{
        {
            
                  
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
        } // exception handling
        
        }  
        
        // reads data from XML file and prints data
        public static void readRoomData(Document d, List<Room> rooms){
            Element root = d.getDocumentElement();

            NodeList office = root.getElementsByTagName("office");
            NodeList trailer = root.getElementsByTagName("trailer");

            NodeList sets = root.getElementsByTagName("set");
            for (int i=0; i<sets.getLength();i++){
                
                System.out.println("Printing information for set "+(i+1));
                

                Node set = sets.item(i);


                //name
                String name = set.getAttributes().getNamedItem("name").getNodeValue();

                List<String> adjacents = new ArrayList<>();
                List<Role> roles = new ArrayList<>();
                int shots = 0;

                //children
                NodeList children = set.getChildNodes();
                for (int j=0; j< children.getLength(); j++){
                    Node sub = children.item(j);
                    NodeList subsub = sub.getChildNodes();
                    for (int k=0; k< subsub.getLength(); k++){
                        Node n = subsub.item(k);

                        //neighbors
                        if("neighbor".equals(n.getNodeName())){
                            String s = n.getAttributes().getNamedItem("name").getNodeValue();
                            adjacents.add(s);
                            System.out.println("neighbor: "+s);
                        }
                        //parts
                        if("part".equals(n.getNodeName())){
                            String s = n.getAttributes().getNamedItem("name").getNodeValue();
                            int rank = Integer.parseInt(n.getAttributes().getNamedItem("level").getNodeValue());
                            roles.add(new Role(s, rank, false));
                            System.out.println("part: "+s);
                        }
                        //takes
                        if("take".equals(n.getNodeName())){
                            shots++;
                        }
                    }
                }
                //add a new room
                rooms.add(new Set(name, shots, roles, adjacents));
                System.out.println("shots: "+shots);
                System.out.println();
            }//for set nodes
        }// method
}//class