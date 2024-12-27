package ru.sfedu.sprintspherepk;

import com.mongodb.client.MongoCollection;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;
import ru.sfedu.sprintspherepk.models.HistoryContent;
import ru.sfedu.sprintspherepk.api.MongoDBUtil;

import java.util.HashMap;
import java.util.Map;

public class SprintSphereClient {
    private static Logger log = Logger.getLogger(SprintSphereClient.class);

    public SprintSphereClient() {
        log.debug("MyProjectClient[0]: starting application.........");
    }

    protected void logBasicSystemInfo() {
        log.info("Launching the application...");
        log.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        log.info("JRE: " + System.getProperty("java.version"));
        log.info("Java Launched From: " + System.getProperty("java.home"));
        log.info("Class Path: " + System.getProperty("java.class.path"));
        log.info("Library Path: " + System.getProperty("java.library.path"));
        log.info("User Home Directory: " + System.getProperty("user.home"));
        log.info("User Working Directory: " + System.getProperty("user.dir"));
        log.info("Test INFO logging.");
    }

}
