package project.userManagement;

import com.google.gson.Gson;
import org.bson.Document;

public class User {
    protected String id;
    protected String group;
    protected String password;

    public User(Document mongoDBUser) {
        id = mongoDBUser.get("_id", String.class);
        group = mongoDBUser.get("group", String.class);
        password = mongoDBUser.get("password", String.class);
    }

    public Boolean checkPassword(String pwd){
        return pwd.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
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
        document.append("password", password);
        return document;
    }

    public String toJson() {
        Document document = toDocument();
        // todo remove password
        return new Gson().toJson(document);
    }
}
