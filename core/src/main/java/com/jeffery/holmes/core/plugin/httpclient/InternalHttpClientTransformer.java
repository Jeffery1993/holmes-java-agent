package com.jeffery.holmes.core.plugin.httpclient;

import com.jeffery.holmes.core.consts.EventTypeEnum;
import com.jeffery.holmes.core.transformer.AccurateMatchedTransformer;
import com.jeffery.holmes.core.util.JavassistUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class InternalHttpClientTransformer extends AccurateMatchedTransformer {

    @Override
    public String getAccurateName() {
        return "org/apache/http/impl/client/InternalHttpClient";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = JavassistUtils.getCtClass(className);
        CtClass[] params = JavassistUtils.constructParams("org.apache.http.HttpHost", "org.apache.http.HttpRequest", "org.apache.http.protocol.HttpContext");
        CtMethod doExecuteMethod = ctClass.getDeclaredMethod("doExecute", params);
        enhanceCtMethod(className, doExecuteMethod);
        ctClass.defrost();
        return ctClass.toBytecode();
    }

    private void enhanceCtMethod(String className, CtMethod ctMethod) throws Exception {
        StringBuilder beforeCode = new StringBuilder();
        beforeCode.append("{");
        beforeCode.append("    " + String.format("%s.start(%s, %s, %s, null, null, null, null, null);", TRACE_COLLECTOR_NAME, className, ctMethod.getName(), EventTypeEnum.HttpClient));
        beforeCode.append("}");
        ctMethod.insertBefore(beforeCode.toString());

        StringBuilder catchCode = new StringBuilder();
        catchCode.append("{");
        catchCode.append("    " + TRACE_COLLECTOR_NAME + ".exception($e);");
        catchCode.append("    throw $e;");
        catchCode.append("}");
        ctMethod.addCatch(catchCode.toString(), JavassistUtils.getCtClass("java.lang.Throwable"));

        StringBuilder afterCode = new StringBuilder();
        afterCode.append("{");
        afterCode.append("    " + TRACE_COLLECTOR_NAME + ".end(code, true);");
        afterCode.append("}");
        ctMethod.insertAfter(afterCode.toString(), true);
    }

}
