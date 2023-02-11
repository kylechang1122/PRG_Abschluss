package project.data.classes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.exception.NodeNotFoundException;
import project.utils.XMLHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlenaryProtocol extends PlenaryObject {

    private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
    int index;
    Date date;
    Time startTime;
    Time endTime;
    String title;
    List<AgendaItem> agendaItems = new ArrayList<>();
    HashMap<String, AgendaItem> agendaItemCache = new HashMap<>();

    List<Speech> speeches = new ArrayList<>();

    String place;
    Set<Speaker> speakers;
    long duration;

    public PlenaryProtocol(InputStream xmlStream) throws ParserConfigurationException, IOException, SAXException, NodeNotFoundException, ParseException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(xmlStream);

        this.setElectionPeriod(XMLHelper.getFirstIntValueFromDocumentByName(document, "wahlperiode"));
        this.setId(XMLHelper.getFirstIntValueFromDocumentByName(document, "wahlperiode") + XMLHelper.getFirstStringValueFromDocumentByName(document, "sitzungsnr"));
        this.setTitle(XMLHelper.getFirstStringValueFromDocumentByName(document, "plenarprotokoll-nummer"));
        this.setPlace(XMLHelper.getFirstStringValueFromDocumentByName(document, "ort"));
        String date = XMLHelper.getAttributeFromNodeOfDocument(document, "datum", "date");
        this.setDate(new Date(sdfDate.parse(date).getTime()));
        String startTime = XMLHelper.getAttributeFromNodeOfDocument(document, "sitzungsbeginn", "sitzung-start-uhrzeit");
        this.setStartTime(getTimeFromString(startTime));
        String endTime = XMLHelper.getAttributeFromNodeOfDocument(document, "sitzungsende", "sitzung-ende-uhrzeit");
        this.setEndTime(getTimeFromString(endTime));

        NodeList agendaNodes = document.getElementsByTagName("ivz-block");
        for (int b = 0; b < agendaNodes.getLength(); b++) {
            Node agendaNode = agendaNodes.item(b);
            AgendaItem agendaItem = new AgendaItem(agendaNode, this);

            this.agendaItems.add(agendaItem);
            //this.agendaItemCache.put(agendaItem.getIndex(),agendaItem);
        }

        NodeList items = document.getElementsByTagName("rede");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            String id = item.getAttributes().getNamedItem("id").getTextContent();
            List<Node> reden = XMLHelper.getDeepChildNodesByName(item, "rede");
            List<Speech> speeches = reden.stream().map(node -> new Speech(this.agendaItemCache.get(id), node, this)).collect(Collectors.toList());
            this.speeches.addAll(speeches);
        }

//        NodeList speeches = document.getElementsByTagName("rede");
//        for(int i = 0; i < speeches.getLength(); i++){
//            Node speechNode = speeches.item(i);
//            if(speechNode.getParentNode().hasAttributes() && speechNode.getParentNode().getAttributes().getNamedItem("top-id") != null) {
//                String index = speechNode.getParentNode().getAttributes().getNamedItem("top-id").getTextContent();
//                AgendaItem ag = this.agendaItemCache.get(index);
//
//                Speech speech = new Speech(ag, speechNode, this);
//                agendaItemCache.get(index).getSpeeches().add(speech);
//            }
//        }
    }

    public PlenaryProtocol(org.bson.Document document) {
        setId(document.getString("_id"));
        setElectionPeriod(document.getInteger("wahlperiode"));
        setTitle(document.getString("title"));
        setDate(document.get("datum", java.sql.Date.class));
        setStartTime(document.get("startzeit", java.sql.Time.class));
        setEndTime(document.get("endzeit", java.sql.Time.class));
        setPlace(document.getString("standort"));
        document.getList("speeches", org.bson.Document.class).forEach((speech) -> {
            // speeches.add(new Speech(speech, this));
        });
        document.getList("agendaitems", org.bson.Document.class).forEach((agendaItem) -> {
            agendaItems.add(new AgendaItem(agendaItem, this));
        });
    }

    private static Time getTimeFromString(String time) {
        String formattedTime = time.replaceAll("\\.", ":").replace(" Uhr", "");
        try {
            return new Time(sdfTime.parse(formattedTime).getTime());
        } catch (ParseException ex) {
            return null;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AgendaItem> getAgendaItems() {
        return agendaItems;
    }

    public void setAgendaItems(List<AgendaItem> agendaItems) {
        this.agendaItems = agendaItems;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<Speech> getSpeeches() {
        return speeches;
    }

    public void setSpeeches(List<Speech> speeches) {
        this.speeches = speeches;
    }
}
