package p.zestianstaff.database;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class DatabaseCredentials {

    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;

    // the ObjectMapper resolves settings based on fields -- these methods are provided as a convenience
    public String hostname() {
        return this.hostname;
    }

    public String port() {
        return this.port;
    }

    public String database() {
        return this.database;
    }
    public String username() {
        return this.username;
    }
    public String password() {
        return this.password;
    }


}
