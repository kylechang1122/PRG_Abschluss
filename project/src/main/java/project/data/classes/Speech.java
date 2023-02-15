package project.data.classes;

import org.apache.commons.jxpath.ri.model.beans.NullPointer;
import org.bson.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import project.utils.XMLHelper;

import java.util.ArrayList;
import java.util.List;

public class Speech extends PlenaryObject {
    AgendaItem agendaItem;
    List<Text> texts = new ArrayList<Text>();
    String text;
    String plainText;
    PlenaryProtocol protocol;
    Speaker speaker;
    String speakerRole;
    int length;
    List<Speech> insertions = new ArrayList<Speech>();

    public Speech(AgendaItem agendaItem, String id) {
        this.agendaItem = agendaItem;
        this.setId(id);
    }

    public Speech(AgendaItem agendaItem, Node node, PlenaryProtocol protocol) {
        int extraSpeeches = 0;
        this.protocol = protocol;
        this.agendaItem = agendaItem;
        this.agendaItem.addSpeech(this);
        ParliamentFactory factory = ParliamentFactory.getInstance();
        this.setId(node.getAttributes().getNamedItem("id").getTextContent());
        NodeList childNodes = node.getChildNodes();
        Speaker currentSpeaker = null;
        Speech currentSpeech = this;
        for (int a = 0; a < childNodes.getLength(); a++) {
            Node currentNode = childNodes.item(a);
            switch (currentNode.getNodeName()) {
                case "p":
                    String klasse = "";
                    if (currentNode.hasAttributes()) {
                        klasse = currentNode.getAttributes().getNamedItem("klasse").getTextContent();
                    }
                    if (klasse.equalsIgnoreCase("redner")) {
                        Node speakerNode = XMLHelper.getDeepChildNodeByName(currentNode, "redner");
                        String speakerId = speakerNode.getAttributes().getNamedItem("id").getTextContent();
                        Node speakerRoleNode = XMLHelper.getDeepChildNodeByName(speakerNode, "rolle_lang");
                        if (speakerRoleNode != null) {
                            speakerRole = speakerRoleNode.getTextContent();
                        }
                        currentSpeaker = factory.getSpeakerById(speakerId);
                        if (currentSpeaker == null) {
                            currentSpeaker = Speaker.fromShortNode(speakerNode);
                        }

                        setSpeaker(currentSpeaker);
                        currentSpeaker.getSpeaches().add(this);
                    } else {
                        currentSpeech.texts.add(new Text(currentNode.getTextContent()));
                    }
                    break;
                case "name":
                    Speaker speakerByName = factory.getSpeakerByNameTag(currentNode.getTextContent());
                    if (speakerByName == getSpeaker()) {
                        currentSpeech = this;
                    } else if (currentSpeaker != speakerByName && speakerByName != null) {
                        currentSpeaker = speakerByName;
                        currentSpeech = new Speech(agendaItem, getId() + "-" + extraSpeeches);
                        currentSpeaker.getSpeaches().add(currentSpeech);
                        currentSpeech.setSpeaker(currentSpeaker);
                        insertions.add(currentSpeech);
                        extraSpeeches++;
                    }
                    break;
                case "kommentar":
                    texts.add(new Comment(currentNode.getTextContent()));
                    break;
            }

        }

    }

    public Speech(Document document) {
        this.setId(document.getString("_id"));
        List<Document> textList = document.getList("texts", Document.class);
        if (textList != null) {
            textList.forEach((text) -> {
                if (text.getString("type") == "text") {
                    texts.add(new Text(text.getString("text")));
                } else {
                    texts.add(new Comment(text.getString("text")));
                }
            });
        }
    }


    public AgendaItem getAgendaItem() {
        return agendaItem;
    }

    public void setAgendaItem(AgendaItem agendaItem) {
        this.agendaItem = agendaItem;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public PlenaryProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(PlenaryProtocol protocol) {
        this.protocol = protocol;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public String getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(String speakerRole) {
        this.speakerRole = speakerRole;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Speech> getInsertions() {
        return insertions;
    }

    public void setInsertions(List<Speech> insertions) {
        this.insertions = insertions;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }
}
