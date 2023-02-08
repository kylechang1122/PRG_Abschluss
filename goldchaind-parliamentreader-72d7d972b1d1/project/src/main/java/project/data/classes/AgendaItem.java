package project.data.classes;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.utils.XMLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AgendaItem extends PlenaryObject{

    List<Speech> speeches = new ArrayList<>();
    String index;
    String title;

    PlenaryProtocol protocol;

    List<Speech> getSpeeches(){
        return speeches;
    }

    public AgendaItem(Node node,PlenaryProtocol protocol){
        setProtocol(protocol);
        setIndex(XMLHelper.getChildNodeByName(node,"ivz-block-titel").getTextContent().replace(":",""));
        setTitle(XMLHelper.getDeepChildNodeByName(node,"ivz-eintrag-inhalt").getTextContent());
    }

    public void setSpeeches(List<Speech> speeches) {
        this.speeches = speeches;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PlenaryProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(PlenaryProtocol protocol) {
        this.protocol = protocol;
    }
}
