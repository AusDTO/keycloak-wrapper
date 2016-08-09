package com.dto.identity;

import java.io.Serializable;

/**
 * Created by mohanambalavanan on 8/08/2016.
 */
public class KeyCloakServerConfig implements Serializable {
    private String host = "localhost";
    private int port = 8081;
    private int workerThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2) * 8;
    private String resourcesHome;

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
