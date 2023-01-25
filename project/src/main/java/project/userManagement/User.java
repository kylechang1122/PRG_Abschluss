package project.userManagement;

import com.google.gson.Gson;
import org.bson.Document;

public class User {
    protected String id;
    protected String group;
    protected String credential;

    public User(Document mongoDBUser) {
        id = mongoDBUser.get("_id", String.class);
        group = mongoDBUser.get("group", String.class);
        credential = mongoDBUser.get("credential", String.class);
    }

    public Boolean checkCredential(String credential){
        return credential.equals(this.credential);
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("_id", id);
        document.append("group", group);
        document.append("credential", credential);
        return document;
    }

    public String toJson() {
        Document document = toDocument();
        // credential should not be sent to the frontend
        document.remove("credential");
        return new Gson().toJson(document);
    }
}
