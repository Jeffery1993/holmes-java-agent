package com.jeffery.holmes.agent;

import com.jeffery.holmes.agent.bootstrap.BootstrapInvoker;
import com.jeffery.holmes.agent.classloader.AgentClassLoader;
import com.jeffery.holmes.agent.util.AgentLoggerFactory;
import com.jeffery.holmes.agent.util.JarFileManager;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The entrance class for java agent.
 */
public class AgentBootstrap {

    private static final Logger LOGGER = AgentLoggerFactory.getLogger(AgentBootstrap.class);

    public static void premain(String args, Instrumentation inst) {
        LOGGER.info("Start to execute agent");
        try {
            main(args, inst);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute agent", e);
            return;
        }
        LOGGER.info("Succeed to execute agent");
    }

    public static void agentmain(String args, Instrumentation inst) {
        LOGGER.info("Start to execute agentmain");
        try {
            main(args, inst);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute agentmain", e);
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
