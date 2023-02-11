package project.utils;

import java.util.Scanner;

import static spark.Spark.port;

public class RestHelper {

    private Scanner scanner;

    public RestHelper() {
        scanner = new Scanner(System.in);
    }

    public void setPort() {
        port(8080);
    }

    public void getDataSpeaker() {



    }

}
