package project.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.exception.NodeNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class XMLHelper {

    private XMLHelper(){

    }


    public static List<Element> match(NodeList nodeList, Predicate<Element> predicate){
        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .filter(predicate)
                .collect(Collectors.toList());
    }
    public static String getAttributeFromNodeOfDocument(Document document, String nodeName,String attributeName) throws NodeNotFoundException {
        NodeList nodes = document.getElementsByTagName(nodeName);
        if(nodes.getLength() > 0){
            return nodes.item(0).getAttributes().getNamedItem(attributeName).getTextContent();
        }
        throw new NodeNotFoundException();
    }
    public static String getFirstStringValueFromDocumentByName(Document document, String nodeName) throws NodeNotFoundException {
        NodeList nodes =  document.getElementsByTagName(nodeName);
        if(nodes.getLength() > 0){
            return nodes.item(0).getTextContent();
        }
        throw new NodeNotFoundException();
    }

    public static int getFirstIntValueFromDocumentByName(Document document, String nodeName) throws NodeNotFoundException{
        return Integer.parseInt(getFirstStringValueFromDocumentByName(document,nodeName));
    }
    public static Node getDeepChildNodeByName(Node node, String childNodeName){
        if(node.getNodeName().equalsIgnoreCase(childNodeName)){
            return node;
        }else if(node.hasChildNodes()) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                Node childNode = getDeepChildNodeByName(node.getChildNodes().item(i),childNodeName);
                if(childNode != null){
                    return childNode;
                }
            }
        }
        return null;
    }

    public static List<Node> getDeepChildNodesByName(Node node, String childNodeName){
        List<Node> nodes = new ArrayList<>(0);
        if(node.getNodeName().equals(childNodeName)) {
            nodes.add(node);
        }
        else if (node.hasChildNodes()) {
            for (int a = 0; a < node.getChildNodes().getLength(); a++) {
                nodes.addAll(getDeepChildNodesByName(node.getChildNodes().item(a), childNodeName));
            }
        }
        return nodes;
    }

    public static Node getChildNodeByName(Node node, String childNodeName){
        if(node.hasChildNodes()){
            for(int i = 0; i < node.getChildNodes().getLength(); i++){
                Node childNode = node.getChildNodes().item(i);
                if(childNode.getNodeName().equalsIgnoreCase(childNodeName)){
                    return childNode;
                }
            }
        }
        return null;
    }

    public static List<String> getChildNodeValuesByNames(Node node, String... childNodeNames){
        List<String> nodeValues = new ArrayList<>(childNodeNames.length);
        Arrays.stream(childNodeNames).forEach((String childNodeName)->nodeValues.add( getChildNodeByName(node,childNodeName).getTextContent() ));
        return nodeValues;
    }
}
