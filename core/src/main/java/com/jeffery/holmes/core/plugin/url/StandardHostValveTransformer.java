package com.jeffery.holmes.core.plugin.url;

import com.jeffery.holmes.common.consts.ConfigConsts;
import com.jeffery.holmes.common.plugin.url.UrlCollector;
import com.jeffery.holmes.core.collect.CollectorManager;
import com.jeffery.holmes.core.consts.EventTypeEnum;
import com.jeffery.holmes.core.transformer.AccurateMatchedTransformer;
import com.jeffery.holmes.core.util.JavassistUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * Transformer for org/apache/catalina/core/StandardHostValve.
 */
public class StandardHostValveTransformer extends AccurateMatchedTransformer {

    private static final String URL_COLLECTOR_CLASS_NAME = UrlCollector.class.getName();
    private static final String URL_COLLECTOR_NAME = URL_COLLECTOR_CLASS_NAME + COLLECTOR_INSTANCE_SUFFIX;

    @Override
    public String getAccurateName() {
        return "org/apache/catalina/core/StandardHostValve";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = JavassistUtils.makeCtClass(classfileBuffer);
        CollectorManager.register(UrlCollector.getInstance());
        UrlCollector.getInstance().setVersion(protectionDomain.getCodeSource());
        try {
            CtClass[] params = JavassistUtils.buildParams(loader, "org.apache.catalina.connector.Request", "org.apache.catalina.connector.Response");
            CtMethod ctMethod = ctClass.getDeclaredMethod("invoke", params);
            enhanceCtMethod(className, ctMethod);
        } catch (Exception e) {
            UrlCollector.getInstance().setEnabled(false);
            throw e;
        }
        byte[] bytecode = ctClass.toBytecode();
        ctClass.defrost();
        return bytecode;
    }

    private void enhanceCtMethod(String className, CtMethod ctMethod) throws Exception {
        StringBuilder beforeCode = new StringBuilder();
        beforeCode.append("{");
        beforeCode.append("    String traceId = $1.getHeader(\"" + ConfigConsts.TRACE_ID + "\");");
        beforeCode.append("    String spanId = $1.getHeader(\"" + ConfigConsts.SPAN_ID + "\");");
        beforeCode.append("    String url = $1.getRequestURI();");
        beforeCode.append("    if (!" + URL_COLLECTOR_NAME + ".onFilter(url)) {");
        beforeCode.append("        String method = $1.getMethod();");
        beforeCode.append("        " + SPAN_EVENT_CLASS_NAME + " spanEvent = " + String.format("%s.start(\"%s\", \"%s\", \"%s\", url, traceId, spanId, url, method);", TRACE_COLLECTOR_CLASS_NAME, className, ctMethod.getName(), EventTypeEnum.Tomcat));
        beforeCode.append("        if (spanEvent != null) {");
        beforeCode.append("            spanEvent.addParameter(\"method\", method);");
        beforeCode.append("        }");
        beforeCode.append("        " + URL_COLLECTOR_NAME + ".onStart(url, method);");
        beforeCode.append("    }");
        beforeCode.append("}");
        ctMethod.insertBefore(beforeCode.toString());

        StringBuilder catchCode = new StringBuilder();
        catchCode.append("{");
        catchCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".exception($e);");
        catchCode.append("    " + URL_COLLECTOR_NAME + ".onThrowable($e);");
        catchCode.append("    throw $e;");
        catchCode.append("}");
        ctMethod.addCatch(catchCode.toString(), JavassistUtils.getCtClass("java.lang.Throwable"), "$e");

        StringBuilder afterCode = new StringBuilder();
        afterCode.append("{");
        afterCode.append("    int code = 0;");
        afterCode.append("    if ($2 != null) {");
        afterCode.append("        code = $2.getStatus();");
        afterCode.append("    }");
        afterCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".end(code);");
        afterCode.append("    " + URL_COLLECTOR_NAME + ".onFinally();");
        afterCode.append("}");
        ctMethod.insertAfter(afterCode.toString(), true);
    }

}
