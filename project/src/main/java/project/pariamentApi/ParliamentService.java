package project.pariamentApi;

import org.bson.Document;
import project.data.classes.PlenaryProtocol;

import java.util.ArrayList;

public class ParliamentService {

    private ParliamentDbHandler dbConnection;
    public ParliamentService(ParliamentDbHandler mongoDBHandler) {

        this.dbConnection = mongoDBHandler;
    }

    public PlenaryProtocol addProtocol (PlenaryProtocol plenaryProtocol){
        dbConnection.addProtocol(plenaryProtocol);
        return plenaryProtocol;
    }

    public ArrayList<PlenaryProtocol> getProtocols (){
        return new ArrayList<PlenaryProtocol>() {
        };
    }
}
