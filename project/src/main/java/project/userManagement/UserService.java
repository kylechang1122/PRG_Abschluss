package project.userManagement;

import org.bson.Document;
import project.database.MongoDBHandler;

import java.util.ArrayList;

public class UserService {

    private UserDbHandler dbConnection;
    public UserService(UserDbHandler mongoDBHandler) {
        dbConnection = mongoDBHandler;
    }

    public void addUser (User user){
        // not implemented
    }

    public ArrayList<User> getUsers (){
        return new ArrayList<User>() {
        };
    }
    public User getUser (String id){
        Document document = dbConnection.getUser(id);
        return new User(document);
    }

    public void editUser (User user){
        dbConnection.replaceUser(user.toDocument());
    }

    public void deleteUser (String id){
        dbConnection.deleteUser(id);
    }

    public boolean userExist (String id){
        return dbConnection.userExists(id);
    }
}
