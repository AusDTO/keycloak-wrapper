package com.dto.identity;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DefaultServletConfig;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ServletInfo;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.services.filters.KeycloakSessionServletFilter;
import org.keycloak.services.managers.ApplianceBootstrap;
import org.keycloak.services.managers.RealmManager;
import org.keycloak.services.resources.KeycloakApplication;

import javax.servlet.DispatcherType;

/**
 * Created by mohanambalavanan on 8/08/2016.
 */
public class KeyCloakBootStrapper {
    public void importRealm(RealmRepresentation rep,KeycloakSessionFactory sessionFactory) {
        KeycloakSession session = sessionFactory.create();;
        session.getTransaction().begin();

        try {
            RealmManager manager = new RealmManager(session);

            if (rep.getId() != null && manager.getRealm(rep.getId()) != null) {
                info("Not importing realm " + rep.getRealm() + " realm already exists");
                return;
            }

            if (manager.getRealmByName(rep.getRealm()) != null) {
                info("Not importing realm " + rep.getRealm() + " realm already exists");
                return;
            }
            manager.setContextPath("/auth");
            RealmModel realm = manager.importRealm(rep);

            info("Imported realm " + realm.getName());

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    protected void setupDevConfig(KeycloakSessionFactory sessionFactory) {
        if (System.getProperty("keycloak.createAdminUser", "true").equals("true")) {
            KeycloakSession session = sessionFactory.create();
            try {
                session.getTransaction().begin();
                if (new ApplianceBootstrap(session).isNoMasterUser()) {
                    new ApplianceBootstrap(session).createMasterRealmUser("admin", "admin");
                }
                session.getTransaction().commit();
            } finally {
                session.close();
            }
        }
    }

    public void start(KeyCloakServerConfig config) throws Throwable {
        long start = System.currentTimeMillis();
        UndertowJaxrsServer server = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(KeycloakApplication.class.getName());

        Undertow.Builder builder = Undertow.builder()
                .addHttpListener(config.getPort(), config.getHost())
                .setWorkerThreads(config.getWorkerThreads())
                .setIoThreads(config.getWorkerThreads() / 8);

        try {
            server.start(builder);

            DeploymentInfo di = server.undertowDeployment(deployment, "");
            di.setClassLoader(getClass().getClassLoader());
            di.setContextPath("/auth");
            di.setDeploymentName("Keycloak");
            di.setDefaultEncoding("UTF-8");

            di.setDefaultServletConfig(new DefaultServletConfig(true));

            ServletInfo restEasyDispatcher = Servlets.servlet("Keycloak REST Interface", HttpServlet30Dispatcher.class);

            restEasyDispatcher.addInitParam("resteasy.servlet.mapping.prefix", "/");
            restEasyDispatcher.setAsyncSupported(true);

            di.addServlet(restEasyDispatcher);

            FilterInfo filter = Servlets.filter("SessionFilter", KeycloakSessionServletFilter.class);

            filter.setAsyncSupported(true);

            di.addFilter(filter);
            di.addFilterUrlMapping("SessionFilter", "/*", DispatcherType.REQUEST);

            server.deploy(di);

            KeycloakSessionFactory sessionFactory =
                    ((KeycloakApplication) deployment.getApplication()).getSessionFactory();

            setupDevConfig(sessionFactory);

            if (config.getResourcesHome() != null) {
                info("Loading resources from " + config.getResourcesHome());
            }

            info("Started Keycloak (http://" + config.getHost() + ":" + config.getPort() + "/auth) in "
                    + (System.currentTimeMillis() - start) + " ms\n");
        } catch (RuntimeException e) {
            server.stop();
            throw e;
        }
    }
    private void info(String message) {
            System.out.println(message);
    }

}
