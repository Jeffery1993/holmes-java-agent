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
import java.io.InputStream;
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
        parseViewConfigs();
    }

    private void parseViewConfigs() {
        LOGGER.info("Start to parse view configs");
        for (CollectorEnum collector : CollectorEnum.values()) {
            String xmlPath = "/view/" + collector + ".xml";
            InputStream inputStream = ViewConfigManager.class.getResourceAsStream(xmlPath);
            if (inputStream == null) {
                LOGGER.error("Could not find view config for collector [" + collector + "]");
            } else {
                try {
                    parseViewConfig(inputStream);
                } catch (Exception e) {
                    LOGGER.error("Failed to parse view config for collector [" + collector + "], " + e.getMessage());
                }
            }
        }
    }

    private void parseViewConfig(InputStream inputStream) throws Exception {
        DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
        Document document = db.parse(inputStream);
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
                    LOGGER.warn("Failed to parse view element [" + child.toString() + "], " + e.getMessage());
                }
            }
        }
        configMap.put(collectorEnum, viewConfigs);
    }

    public List<ViewConfig> getViewConfigs(CollectorEnum collector) {
        return configMap.get(collector);
    }

}
