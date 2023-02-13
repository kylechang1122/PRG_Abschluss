package project.data.classes;

import org.bson.Document;
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

    public List<Speech> getSpeeches(){
        return speeches;
    }

    public AgendaItem(Node node,PlenaryProtocol protocol){
        setProtocol(protocol);
        setIndex(XMLHelper.getChildNodeByName(node,"ivz-block-titel").getTextContent().replace(":",""));
        setTitle(XMLHelper.getDeepChildNodeByName(node,"ivz-eintrag-inhalt").getTextContent());
    }

    public AgendaItem(Document document, PlenaryProtocol protocol){
        setProtocol(protocol);
        String index = document.keySet().iterator().next();
        setIndex(index);
        setTitle(document.getString(index));
    }

    public void addSpeech(Speech speech) {
        this.speeches.add(speech);
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
