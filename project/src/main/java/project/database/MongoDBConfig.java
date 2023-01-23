package project.database;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import project.exception.DataBaseException;

import java.io.*;
import java.util.Properties;

public class MongoDBConfig  extends Properties implements DataBaseConfig {

    private String hostname;
    private String username;
    private String password;
    private int port;
    private String database;
    private String collection;
    public MongoDBConfig(File configFile) throws DataBaseException{
        loadConfig(configFile.getAbsolutePath());
    }
    public MongoDBConfig(String configPath) throws DataBaseException{
        loadConfig(configPath);
    }
    private void loadConfig(String configPath) throws DataBaseException {
        BufferedReader configReader = null;
        try {
            InputStreamReader configStream = new InputStreamReader(new FileInputStream(new File(configPath)), "UTF-8");
            configReader = new BufferedReader(configStream);
            super.load(configReader);
            setValues();
        }catch(IOException ex){
            throw new DataBaseException("Failed to get config for Database Connection");
        }finally {
            if(configReader != null){
                try {
                    configReader.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    private void setValues(){
        setHostname(getProperty("remote_host"));
        setUsername(getProperty("remote_user"));
        setPassword(getProperty("remote_password"));
        setPort(Integer.parseInt(getProperty("remote_port")));
        setDatabase(getProperty("remote_database"));
        setCollection(getProperty("remote_collection"));
    }

    private void setHostname(String hostname) {
        this.hostname = hostname;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private void setDatabase(String database) {
        this.database = database;
    }

    private void setCollection(String collection) {
        this.collection = collection;
    }

    @Override
    public String getHostname() {
        return hostname;
    }
    @Override
    public String getUserName() {
        return username;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public int getPort() {
        return port;
    }
    @Override
    public String getDatabase() {
        return database;
    }

    public String getCollection(){
        return collection;
    }

    public MongoCredential getCredentials(){
        return MongoCredential.createScramSha1Credential(getUserName(),getDatabase(),getPassword().toCharArray());
    }
    public ServerAddress getServerAddress(){
        return new ServerAddress(getHostname(),getPort());
    }
    public MongoClientOptions getDefaultClientOptions(){
        return MongoClientOptions.builder()
                .connectionsPerHost(20)
                .socketTimeout(10000)
                .maxWaitTime(10000)
                .connectTimeout(1000)
                .sslEnabled(false)
                .build();
    }
}
