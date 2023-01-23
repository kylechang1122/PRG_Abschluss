package org.example;

import project.database.MongoDBConfig;
import project.database.MongoDBHandler;
import project.exception.DataBaseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        try {
            MongoDBConfig config = new MongoDBConfig("C:\\Users\\Driton\\Desktop\\Fiverr Project\\parliamentreader\\project\\src\\main\\resources\\dbconnectionconfig.txt");
            MongoDBHandler dbConnection = new MongoDBHandler(config);
            System.out.println("Hello World");
        }catch (DataBaseException ex){
            ex.printStackTrace();
        }

    }
}
