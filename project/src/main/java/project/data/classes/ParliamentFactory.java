package project.data.classes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.utils.FileHelper;
import project.utils.WebHelper;
import project.utils.XMLHelper;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParliamentFactory {

    private static ParliamentFactory factory;

    private List<PlenaryProtocol> protocols = new ArrayList<>();
    private List<Speaker> speakers = new ArrayList<>();
    private List<Party> partys = new ArrayList<>();
    private ParliamentFactory(){

    }

    public static ParliamentFactory getInstance(){
        if(factory == null){
            factory = new ParliamentFactory();
                try {
                    factory.loadMdbFromXML();
                    WebHelper.downloadProtocols();
                } catch (ParserConfigurationException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SAXException e) {
                    throw new RuntimeException(e);
                }
        }
        return factory;
    }

    public void loadMdbFromXML() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = null;
        Set<File> mdbFiles = FileHelper.getMDBXMLFilesFromWeb();
        xmlFile = mdbFiles.stream().filter(file->file.getName().contains(".XML")).findFirst().orElse(null);

        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDocument = documentBuilder.parse(xmlFile);
        NodeList mdbNodes = xmlDocument.getElementsByTagName("MDB");

        for(int i = 0; i < mdbNodes.getLength(); i++){
            Node mdbNode = mdbNodes.item(i);
            Node idNode = XMLHelper.getChildNodeByName(mdbNode,"id");
            String id = idNode.getNodeValue();
            if(!hasSpeakerWithId(id)){
                speakers.add(new Speaker(mdbNode));
            }
        }
    }

    public void loadPlenaryProtocolsFromWeb() throws IOException, MalformedURLException {
            List<Path> links = WebHelper.downloadProtocols();
            //links.stream().forEach(this::loadPlenaryProtocolFromPath);
    }

    public void loadPlenaryProtocolsFromFiles() throws IOException{
        List<File> files = WebHelper.getDownloadedXmlFiles();
        files.stream().map(file -> Paths.get(file.getAbsolutePath())).forEach(this::loadPlenaryProtocolFromPath);
    }
    public void loadPlenaryProtocolFromPath(Path path){
        try {
            System.out.println("Parsing " + path.toAbsolutePath().toString());
            PlenaryProtocol protocol = new PlenaryProtocol(new FileInputStream(path.toAbsolutePath().toString()));
            protocols.add(protocol);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private boolean hasSpeakerWithId(String id){
        return getSpeakerById(id) != null;
    }

    public Party getParty(String name){
        Party fromList = partys.stream().filter((Party party)->party.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if(fromList == null){
            fromList = new Party(name);
            partys.add(fromList);
        }
        return fromList;
    }

    public List<Party> getPartys() {
        return partys;
    }

    public List<PlenaryProtocol> getProtocols(){
        return protocols;
    }

    public Speaker getSpeakerById(String id){
        return speakers.stream().filter(speaker-> speaker.getId()!=null && speaker.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public Speaker getSpeakerByNameTag(String name){
        Speaker speakerByName = speakers.stream().filter(speaker -> name.contains(speaker.firstName) && name.contains(speaker.name)).findFirst().orElse(null);
        if(speakerByName == null) {
            speakerByName = new Speaker();
            speakerByName.setName(name);
            speakerByName.setFirstName("");
            getSpeakers().add(speakerByName);
        }
        return speakerByName;
    }

    public List<Speaker> getSpeakers(){
        return speakers;
    }
}
