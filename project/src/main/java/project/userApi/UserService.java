package project.userApi;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;

public class UserService {

    private UserDbHandler dbConnection;

    public UserService(UserDbHandler mongoDBHandler) {

        this.dbConnection = mongoDBHandler;
    }

    public User addUser(User user) {
        dbConnection.addUser(user.toDocument());
        return user;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<User>() {
        };
    }

    public User getUser(String id) {
        Document document = dbConnection.getUser(id);
        if (document == null) {
            return null;
        }
        return new User(document);
    }

    public ArrayList<Document> getUserOverview() {
        return dbConnection.getCollection().aggregate(
                        Arrays.asList(new Document("$project",
                                new Document("firstName", 1L)
                                        .append("lastName", 1L)
                                        .append("userId", "$_id"))))
                .into(new ArrayList<>());
    }

    public User editUser(User user) {
        dbConnection.replaceUser(user.toDocument());
        return user;
    }

    public void deleteUser(String id) {
        dbConnection.deleteUser(id);
    }

    public boolean userExists(String id) {
        return dbConnection.userExists(id);
    }
}
