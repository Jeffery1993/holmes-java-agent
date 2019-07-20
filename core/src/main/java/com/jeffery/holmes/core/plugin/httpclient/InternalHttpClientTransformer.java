package com.jeffery.holmes.core.plugin.httpclient;

import com.jeffery.holmes.common.consts.ConfigConsts;
import com.jeffery.holmes.common.plugin.httpclient.HttpClientCollector;
import com.jeffery.holmes.core.collect.CollectorManager;
import com.jeffery.holmes.core.consts.EventTypeEnum;
import com.jeffery.holmes.core.transformer.AccurateMatchedTransformer;
import com.jeffery.holmes.core.util.JavassistUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * Transformer for org/apache/http/impl/client/InternalHttpClient.
 */
public class InternalHttpClientTransformer extends AccurateMatchedTransformer {

    private static final String HTTP_CLIENT_COLLECTOR_CLASS_NAME = HttpClientCollector.class.getName();
    private static final String HTTP_CLIENT_COLLECTOR_NAME = HTTP_CLIENT_COLLECTOR_CLASS_NAME + COLLECTOR_INSTANCE_SUFFIX;


    @Override
    public String getAccurateName() {
        return "org/apache/http/impl/client/InternalHttpClient";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = JavassistUtils.makeCtClass(classfileBuffer);
        CollectorManager.register(HttpClientCollector.getInstance());
        HttpClientCollector.getInstance().setVersion(protectionDomain.getCodeSource());
        try {
            CtClass[] params = JavassistUtils.buildParams(loader, "org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.protocol.HttpContext");
            CtMethod doExecuteMethod = ctClass.getDeclaredMethod("doExecute", params);
            enhanceCtMethod(className, doExecuteMethod);
        } catch (Exception e) {
            HttpClientCollector.getInstance().setEnabled(false);
            throw e;
        }
        byte[] bytecode = ctClass.toBytecode();
        ctClass.defrost();
        return bytecode;
    }

    private void enhanceCtMethod(String className, CtMethod ctMethod) throws Exception {
        StringBuilder beforeCode = new StringBuilder();
        beforeCode.append("{");
        beforeCode.append("    String url = $2.getRequestLine().getUri();");
        beforeCode.append("    String method = $2.getRequestLine().getMethod();");
        beforeCode.append("    " + SPAN_EVENT_CLASS_NAME + " spanEvent = " + String.format("%s.start(\"%s\", \"%s\", \"%s\", url);", TRACE_COLLECTOR_CLASS_NAME, className, ctMethod.getName(), EventTypeEnum.HttpClient));
        beforeCode.append("    if (spanEvent != null) {");
        beforeCode.append("        spanEvent.addParameter(\"method\", method);");
        beforeCode.append("        $2.addHeader(\"" + ConfigConsts.TRACE_ID + "\", spanEvent.getTraceId());");
        beforeCode.append("        $2.addHeader(\"" + ConfigConsts.SPAN_ID + "\", spanEvent.attachNextSpanId());");
        beforeCode.append("    }");
        beforeCode.append("    " + HTTP_CLIENT_COLLECTOR_NAME + ".onStart(url, method);");
        beforeCode.append("}");
        ctMethod.insertBefore(beforeCode.toString());

        StringBuilder catchCode = new StringBuilder();
        catchCode.append("{");
        catchCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".exception($e);");
        catchCode.append("    " + HTTP_CLIENT_COLLECTOR_NAME + ".onThrowable($e);");
        catchCode.append("    throw $e;");
        catchCode.append("}");
        ctMethod.addCatch(catchCode.toString(), JavassistUtils.getCtClass("java.lang.Throwable"), "$e");

        StringBuilder afterCode = new StringBuilder();
        afterCode.append("{");
        afterCode.append("    int code = 0;");
        afterCode.append("    if ($_ != null) {");
        afterCode.append("        code = $_.getStatusLine().getStatusCode();");
        afterCode.append("    }");
        afterCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".end(code);");
        afterCode.append("    " + HTTP_CLIENT_COLLECTOR_NAME + ".onFinally();");
        afterCode.append("}");
        ctMethod.insertAfter(afterCode.toString(), true);
    }

}
