package com.jeffery.holmes.premain;

import com.jeffery.holmes.premain.bootstrap.BootstrapInvoker;
import com.jeffery.holmes.premain.classloader.AgentClassLoader;
import com.jeffery.holmes.premain.util.AgentLogFactory;
import com.jeffery.holmes.premain.util.JarFileManager;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class AgentBootstrap {

    private static final Logger LOGGER = AgentLogFactory.getLogger(AgentBootstrap.class);

    public static void premain(String args, Instrumentation inst) {
        LOGGER.info("Start to execute premain");
        try {
            main(args, inst);
        } catch (Exception e) {
            LOGGER.severe("Failed to execute premain: " + e);
            return;
        }
        LOGGER.info("Succeed to execute premain");
    }

    public static void agentmain(String args, Instrumentation inst) {
        LOGGER.info("Start to execute agentmain");
        try {
            main(args, inst);
        } catch (Exception e) {
            LOGGER.severe("Failed to execute agentmain: " + e);
            return;
        }
        LOGGER.info("Succeed to execute agentmain");
    }

    public static void main(String args, Instrumentation inst) throws Exception {
        // commonJarFile should be appendToBootstrapClassLoaderSearch
        JarFile commonJarFile = JarFileManager.getCommonJarFile();
        inst.appendToBootstrapClassLoaderSearch(commonJarFile);

        // self-defined AgentClassLoader used for isolation
        AgentClassLoader agentClassloader = new AgentClassLoader(JarFileManager.getCoreJarFileURLs());

        // invoke via reflection and swop the ContextClassloader
        BootstrapInvoker bootstrapInvoker = new BootstrapInvoker(agentClassloader);
        bootstrapInvoker.boot(args, inst);
    }

}
