package com.example.pharmacure.Utils.qrlogin;

public class Configuration {
    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    private String chatServerIp = "192.168.1.102";
    private String chatServerPort = "9000";

    public String getChatServerIp() {
        return chatServerIp;
    }

    public String getChatServerPort() {
        return chatServerPort;
    }
}
