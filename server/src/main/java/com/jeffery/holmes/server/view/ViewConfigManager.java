package com.jeffery.holmes.server.view;

import com.jeffery.holmes.server.consts.CollectorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewConfigManager.class);

    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private Map<CollectorEnum, List<ViewConfig>> configMap = new HashMap<>();

    @PostConstruct
    public void init() {
        parseXmls();
    }

    private void parseXmls() {
        File viewDir = new File("server/src/main/resources/common/view");
        LOGGER.debug(viewDir.getAbsolutePath());
        if (viewDir.exists() && viewDir.isDirectory()) {
            for (File file : viewDir.listFiles()) {
                LOGGER.debug(file.getAbsolutePath());
                try {
                    parseXml(file);
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        }
    }

    private void parseXml(File xml) throws Exception {
        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        Document document = db.parse(xml);
        Element root = document.getDocumentElement();
        String collector = root.getAttribute("collector");
        CollectorEnum collectorEnum = CollectorEnum.valueOf(collector);
        if (collectorEnum == null) {
            throw new IllegalArgumentException("Unknown collector name: " + collector);
        }
        LOGGER.debug("collector: " + collectorEnum);

        NodeList childNodes = root.getChildNodes();
        List<ViewConfig> viewConfigs = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                try {
                    viewConfigs.add(ViewConfig.parse((Element) child));
                } catch (Exception e) {
                    LOGGER.warn("Failed to parse view config: " + e.getMessage(), e);
                }
            }
        }
        configMap.put(collectorEnum, viewConfigs);
    }

    public List<ViewConfig> getViewConfigs(CollectorEnum collector) {
        return configMap.get(collector);
    }

}
