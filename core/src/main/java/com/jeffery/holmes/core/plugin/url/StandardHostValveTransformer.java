package com.jeffery.holmes.core.plugin.url;

import com.jeffery.holmes.core.consts.EventTypeEnum;
import com.jeffery.holmes.core.transformer.AccurateMatchedTransformer;
import com.jeffery.holmes.core.util.JavassistUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class StandardHostValveTransformer extends AccurateMatchedTransformer {

    @Override
    public String getAccurateName() {
        return "org/apache/catalina/core/StandardHostValve";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = JavassistUtils.getCtClass(className);
        CtClass[] params = JavassistUtils.constructParams("org.apache.catalina.connector.Request", "org.apache.catalina.connector.Response");
        CtMethod ctMethod = ctClass.getDeclaredMethod("invoke", params);
        enhanceCtMethod(className, ctMethod);
        ctClass.defrost();
        return ctClass.toBytecode();
    }

    private void enhanceCtMethod(String className, CtMethod ctMethod) throws Exception {
        StringBuilder beforeCode = new StringBuilder();
        beforeCode.append("{");
        beforeCode.append("    " + String.format("%s.start(%s, %s, %s, null, null, null, url, method);", TRACE_COLLECTOR_NAME, className, ctMethod.getName(), EventTypeEnum.Tomcat));
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
        afterCode.append("    " + TRACE_COLLECTOR_NAME + ".end(code, false);");
        afterCode.append("}");
        ctMethod.insertAfter(afterCode.toString(), true);
    }

}
