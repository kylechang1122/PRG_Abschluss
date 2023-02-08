package project.database;

public interface DataBaseConfig {

    /**
     * @return Hostname for the Connection
     */
    public String getHostname();

    /**
     * @return UserName for the Connection
     */
    public String getUserName();

    /**
     * @return Password for the Connection
     */
    public String getPassword();
    /**
     * @return Port for the Connection
     */
    public int getPort();
    /**
     * @return Database Name for the Connection
     */
    public String getDatabase();


}
