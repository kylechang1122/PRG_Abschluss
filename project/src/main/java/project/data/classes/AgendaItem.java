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

    int number;

    String title;
    PlenaryProtocol protocol;

    /**
     * empty constructor, needed for GSON
     */
    public AgendaItem(){}

    public AgendaItem(Node node, PlenaryProtocol protocol){
        setProtocol(protocol);
        setIndex(XMLHelper.getChildNodeByName(node,"ivz-block-titel").getTextContent().replace(":",""));
        setTitle(XMLHelper.getDeepChildNodeByName(node,"ivz-eintrag-inhalt").getTextContent());
    }

    public AgendaItem(Document document, PlenaryProtocol protocol){
        setProtocol(protocol);
        List<org.bson.Document> speechesList = document.getList("speeches", org.bson.Document.class);
        if (speechesList != null) {
            speechesList.forEach((speech) -> {
                speeches.add(new Speech(this, speech));
            });
        }
        this.setId(document.getString("_id"));
        this.setNumber(document.getInteger("number"));
        //index
        this.setIndex(document.getString("index"));
        //title
        this.setTitle(document.getString("title"));
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Speech> getSpeeches(){
        return speeches;
    }

    public void addSpeech(Speech speech) {
        this.speeches.add(speech);
    }

    public void removeSpeech(Speech speech) {
        this.speeches.remove(speech);
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
