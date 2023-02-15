package project.data.classes;

import org.bson.Document;
import org.w3c.dom.Node;
import project.utils.XMLHelper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Speaker extends PlenaryObject {

    Party party;
    Fraction fraction;
    String role;
    String title;
    String name;
    String firstName;
    Set<Speech> speaches = new HashSet<>();
    boolean leader;
    float avgLength;
    boolean government;
    Set<PlenaryProtocol> absences;
    String academicTitle;
    String birthday;
    String birthPlace;
    String familyState;
    String religion;
    String job;
    String gender;
    String deathDate;

    String image;

    public Speaker() {

    }

    public Speaker(Node mdbNode) {
        ParliamentFactory factory = ParliamentFactory.getInstance();
        setId(XMLHelper.getDeepChildNodeByName(mdbNode, "ID").getTextContent());
        Node nameNode = XMLHelper.getDeepChildNodeByName(mdbNode, "NAME");
        List<String> attributes = XMLHelper.getChildNodeValuesByNames(nameNode, "NACHNAME", "VORNAME", "ANREDE_TITEL", "AKAD_TITEL");
        setName(attributes.get(0));
        setFirstName(attributes.get(1));
        setTitle(attributes.get(2));
        setAcademicTitle(attributes.get(3));
        String partyName = XMLHelper.getDeepChildNodeByName(mdbNode, "PARTEI_KURZ").getTextContent();
        String birthday = XMLHelper.getDeepChildNodeByName(mdbNode, "GEBURTSDATUM").getTextContent();
        String deathday = XMLHelper.getDeepChildNodeByName(mdbNode, "STERBEDATUM").getTextContent();
        setBirthday(birthday);
        setDeathDate(deathday);
        Node bioNode = XMLHelper.getDeepChildNodeByName(mdbNode, "BIOGRAFISCHE_ANGABEN");
        List<String> attributesBio = XMLHelper.getChildNodeValuesByNames(bioNode, "GEBURTSORT",
                "GESCHLECHT", "BERUF", "FAMILIENSTAND", "RELIGION");
        setBirthPlace(attributesBio.get(0));
        setGender(attributesBio.get(1));
        setJob(attributesBio.get(2));
        setFamilyState(attributesBio.get(3));
        setReligion(attributesBio.get(4));


        Party party = factory.getParty(partyName);
        image = "https://bilddatenbank.bundestag.de/search/picture-result?query=%22" + getFirstName() + " " + getName() + "%22#group-1";

        party.addMember(this);
        setParty(party);
    }

    public static Speaker fromShortNode(Node node) {
        Speaker speaker = new Speaker();
        Node idNode = node.getAttributes().getNamedItem("id");
        speaker.setId(idNode.getTextContent());
        speaker.setFirstName(XMLHelper.getDeepChildNodeByName(node, "vorname").getTextContent());
        speaker.setName(XMLHelper.getDeepChildNodeByName(node, "nachname").getTextContent());
        ParliamentFactory.getInstance().getSpeakers().add(speaker);
        return speaker;
    }

    public Speaker(Document document) {
        setId(document.getString("_id"));
        setName(document.getString("name"));
        setFirstName(document.getString("firstName"));
        setBirthday(document.getString("geburtsdatum"));
        setBirthPlace(document.getString("geburtsort"));
        setDeathDate(document.getString("sterbedatum"));
        setGender(document.getString("geschlecht"));
        setJob(document.getString("beruf"));
        setAcademicTitle(document.getString("akademischertitel"));
        String party = document.getString("party");
        if (party != null) {
            //ParliamentFactory can only be used during XML import
            //setParty(ParliamentFactory.getInstance().getParty(party));
            setParty(new Party(party));
        }
        String fraction = document.getString("fraction");
        if (fraction != null) {
            setFraction(new Fraction(fraction));
        }

        // ... todo
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Fraction getFraction() {
        return fraction;
    }

    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<Speech> getSpeaches() {
        return speaches;
    }

    public void setSpeaches(Set<Speech> speaches) {
        this.speaches = speaches;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public float getAvgLength() {
        return avgLength;
    }

    public void setAvgLength(float avgLength) {
        this.avgLength = avgLength;
    }

    public boolean isGovernment() {
        return government;
    }

    public void setGovernment(boolean government) {
        this.government = government;
    }

    public Set<PlenaryProtocol> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<PlenaryProtocol> absences) {
        this.absences = absences;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getFamilyState() {
        return familyState;
    }

    public void setFamilyState(String familyState) {
        this.familyState = familyState;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Speaker{" +
                "party=" + party +
                ", fraction=" + fraction +
                ", role='" + role + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", speaches=" + speaches +
                ", leader=" + leader +
                ", avgLength=" + avgLength +
                ", government=" + government +
                ", absences=" + absences +
                ", academicTitle='" + academicTitle + '\'' +
                ", birthday=" + birthday +
                ", birthPlace='" + birthPlace + '\'' +
                ", familyState='" + familyState + '\'' +
                ", religion='" + religion + '\'' +
                ", job='" + job + '\'' +
                ", gender='" + gender + '\'' +
                ", deathDate='" + deathDate + '\'' +
                '}';
    }
}
