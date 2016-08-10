package com.dto.identity;

import java.io.Serializable;

/**
 * Created by mohanambalavanan on 8/08/2016.
 */
public class KeyCloakServerConfig implements Serializable {
    private String host;
    private int port;
    private int workerThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2) * 8;
    private String resourcesHome;

    public KeyCloakServerConfig(){
        this.host = "0.0.0.0";
        if(System.getProperty("PORT")!=null){
           this.port = Integer.valueOf(System.getProperty("PORT"));
        }else {
            this.port =8081;
        }
    }
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getResourcesHome() {
        return resourcesHome;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setResourcesHome(String resourcesHome) {
        this.resourcesHome = resourcesHome;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }
}
