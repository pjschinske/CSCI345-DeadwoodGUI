package deadwood.xml;// Example Code for parsing XML file
// Dr. Moushumi Sharmin
// CSCI 345

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import deadwood.board.Board;
import deadwood.board.Card;
import deadwood.board.Role;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class ParseXML {

   
    // building a document from the XML file
    // returns a Document object after loading the book.xml file.
    public static Document getDocFromFile(String filename)
        throws ParserConfigurationException {
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

    public static List<Card> readCardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList cards = root.getElementsByTagName("card");
        List<Card> scenes = new ArrayList<>();
        for (int i = 0; i < cards.getLength(); i++) {
            Node card = cards.item(i);
//            System.out.println(card.getAttributes().item(0));
            //checks to see if this is the external or internal "card" node
            if (card.getAttributes().getNamedItem("name") != null) {
                String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
                String image = card.getAttributes().getNamedItem("img").getNodeValue();
                int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
                NodeList children = card.getChildNodes();
                int sceneNumber = 0;
                String sceneDescription = null;
                List<Role> roles = new ArrayList<>();
                for (int j = 0; j < children.getLength(); j++) {
                    Node sub = children.item(j);
                    if (sub.getNodeName().equals("card")) {
                        sceneNumber = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
                        sceneDescription = sub.getTextContent();
                    } else if (sub.getNodeName().equals("part")) {
                        String roleName = sub.getAttributes().getNamedItem("name").getNodeValue();
                        int roleLevel = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
                        String line = "";
                        int x = 0, y = 0, width = 0, height = 0;
                        NodeList sceneSub = sub.getChildNodes();
                        for (int k = 0; k < sceneSub.getLength(); k++) {
                            Node sceneSubNode = sceneSub.item(k);
                            if (sceneSubNode.getNodeName().equals("area")) {
                                x = Integer.parseInt(sceneSubNode.getAttributes().getNamedItem("x").getNodeValue());
                                y = Integer.parseInt(sceneSubNode.getAttributes().getNamedItem("y").getNodeValue());
                                width = Integer.parseInt(sceneSubNode.getAttributes().getNamedItem("w").getNodeValue());
                                height = Integer.parseInt(sceneSubNode.getAttributes().getNamedItem("h").getNodeValue());
                            } else if (sceneSubNode.getNodeName().equals("line")) {
                                line = sceneSubNode.getTextContent();
                            }
                        }
                        roles.add(0, new Role(roleName, line, roleLevel, x, y, width, height));
                    }
                }
                scenes.add(new Card(cardName, image, sceneDescription, sceneNumber, roles, budget));
            }

        }
        return scenes;
    }

    public static void readBoardData(Document d) {
        Element root = d.getDocumentElement();
        NodeList sets = root.getElementsByTagName("set");
        for (int i = 0; i < sets.getLength(); i++) {
            Node set = sets.item(i);
            String setName = set.getAttributes().getNamedItem("name").getNodeValue();

            NodeList setChildren = set.getChildNodes();
            for (int j = 0; j < setChildren.getLength(); j++) {
                Node setChild = setChildren.item(j);

                if (setChild.getNodeName().equals("parts")) {
                    NodeList parts = setChild.getChildNodes();
                    List<Role> roles = new ArrayList<>();
                    for (int k = 0; k < parts.getLength(); k++) {
                        Node part = parts.item(k);
                        if (part.getNodeName().equals("part")) {
                            String roleName = part.getAttributes().getNamedItem("name").getNodeValue();
                            int roleLevel = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
                            String line = "";
                            int x = 0, y = 0, width = 0, height = 0;
                            NodeList partChildren = part.getChildNodes();
                            for (int l = 0; l < parts.getLength(); l++) {
                                Node partChild = partChildren.item(l);
                                if (partChild != null && partChild.getNodeName().equals("area")) {
                                    x = Integer.parseInt(partChild.getAttributes().getNamedItem("x").getNodeValue());
                                    y = Integer.parseInt(partChild.getAttributes().getNamedItem("y").getNodeValue());
                                    width = Integer.parseInt(partChild.getAttributes().getNamedItem("w").getNodeValue());
                                    height = Integer.parseInt(partChild.getAttributes().getNamedItem("h").getNodeValue());
                                } else if (partChild != null && partChild.getNodeName().equals("line")) {
                                    line = partChildren.item(l).getTextContent();
                                }
                            }
                            roles.add(new Role(roleName, line, roleLevel, x, y, width, height));
                        }
                    }
                    switch (setName) {
                        case "Main Street":
                            Board.mainStreet.setRoles(roles);
                            break;
                        case "Saloon":
                            Board.saloon.setRoles(roles);
                            break;
                        case "Ranch":
                            Board.ranch.setRoles(roles);
                            break;
                        case "Secret Hideout":
                            Board.secretHideout.setRoles(roles);
                            break;
                        case "Bank":
                            Board.bank.setRoles(roles);
                            break;
                        case "Church":
                            Board.church.setRoles(roles);
                            break;
                        case "Hotel":
                            Board.hotel.setRoles(roles);
                            break;
                        case "Train Station":
                            Board.trainStation.setRoles(roles);
                            break;
                        case "Jail":
                            Board.jail.setRoles(roles);
                            break;
                        case "General Store":
                            Board.generalStore.setRoles(roles);
                            break;
                    }
                }
            }
        }
    }

}