package project.userManagement;

import com.google.gson.Gson;
import org.bson.Document;

public class User {
    protected String userId;
    protected String firstName;
    protected String lastName;

    protected String group;
    protected String credential;
    public User(Document mongoDBUser) {
        userId = mongoDBUser.get("userId", String.class);
        firstName = mongoDBUser.get("firstName", String.class);
        lastName = mongoDBUser.get("lastName", String.class);
        group = mongoDBUser.get("group", String.class);
        credential = mongoDBUser.get("credential", String.class);
    }

    public Boolean checkCredential(String credential){
        return credential.equals(this.credential);
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Document toDocument() {
        Document document = new Document();
        document.append("_id", userId);
        document.append("firstName", firstName);
        document.append("lastName", lastName);
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
