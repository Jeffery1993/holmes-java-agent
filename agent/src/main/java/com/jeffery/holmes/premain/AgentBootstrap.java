package com.jeffery.holmes.premain;

import com.jeffery.holmes.premain.bootstrap.BootstrapInvoker;
import com.jeffery.holmes.premain.classloader.AgentClassLoader;
import com.jeffery.holmes.premain.util.JarFileManager;

import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class AgentBootstrap {

    public static void premain(String args, Instrumentation inst) {
        // commonJarFile should be appendToBootstrapClassLoaderSearch
        JarFile commonJarFile = JarFileManager.getCommonJarFile();
        inst.appendToBootstrapClassLoaderSearch(commonJarFile);

        // self-defined AgentClassLoader used for isolation
        AgentClassLoader agentClassloader = new AgentClassLoader(JarFileManager.getCoreJarFileURLs());

        // invoke via reflection and swop the ContextClassloader
        BootstrapInvoker bootstrapInvoker = new BootstrapInvoker(agentClassloader);
        bootstrapInvoker.boot(args, inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        // TODO
    }

}
