package com.jeffery.holmes.core.plugin.mysql;

import com.jeffery.holmes.common.plugin.mysql.MysqlCollector;
import com.jeffery.holmes.core.collect.CollectorManager;
import com.jeffery.holmes.core.consts.EventTypeEnum;
import com.jeffery.holmes.core.transformer.AccurateMatchedTransformer;
import com.jeffery.holmes.core.util.JavassistUtils;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

/**
 * Transformer for com/mysql/jdbc/PreparedStatement.
 */
public class PreparedStatementTransformer extends AccurateMatchedTransformer {

    private static final String MYSQL_COLLECTOR_CLASS_NAME = MysqlCollector.class.getName();
    private static final String MYSQL_COLLECTOR_NAME = MYSQL_COLLECTOR_CLASS_NAME + COLLECTOR_INSTANCE_SUFFIX;

    @Override
    public String getAccurateName() {
        return "com/mysql/jdbc/PreparedStatement";
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        CtClass ctClass = JavassistUtils.makeCtClass(classfileBuffer);
        CollectorManager.register(MysqlCollector.getInstance());
        try {
            CtMethod executeMethod = ctClass.getDeclaredMethod("execute");
            CtMethod executeQueryMethod = ctClass.getDeclaredMethod("executeQuery");
            CtMethod executeUpdateMethod = ctClass.getDeclaredMethod("executeUpdate");
            enhanceCtMethod(className, executeMethod);
            enhanceCtMethod(className, executeQueryMethod);
            enhanceCtMethod(className, executeUpdateMethod);
        } catch (Exception e) {
            MysqlCollector.getInstance().setEnabled(false);
            throw new Exception(e);
        }
        byte[] bytecode = ctClass.toBytecode();
        ctClass.defrost();
        return bytecode;
    }

    private void enhanceCtMethod(String className, CtMethod ctMethod) throws Exception {
        StringBuilder beforeCode = new StringBuilder();
        beforeCode.append("{");
        beforeCode.append("    String sql = $0.originalSql;");
        beforeCode.append("    " + String.format("%s.start(\"%s\", \"%s\", \"%s\", sql);", TRACE_COLLECTOR_CLASS_NAME, className, ctMethod.getName(), EventTypeEnum.Mysql));
        beforeCode.append("    " + MYSQL_COLLECTOR_NAME + ".onStart(sql);");
        beforeCode.append("}");
        ctMethod.insertBefore(beforeCode.toString());

        StringBuilder catchCode = new StringBuilder();
        catchCode.append("{");
        catchCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".exception($e);");
        catchCode.append("    " + MYSQL_COLLECTOR_NAME + ".onThrowable($e);");
        catchCode.append("    throw $e;");
        catchCode.append("}");
        ctMethod.addCatch(catchCode.toString(), JavassistUtils.getCtClass("java.lang.Throwable"), "$e");

        StringBuilder afterCode = new StringBuilder();
        afterCode.append("{");
        afterCode.append("    " + TRACE_COLLECTOR_CLASS_NAME + ".end(0);");
        afterCode.append("    " + MYSQL_COLLECTOR_NAME + ".onFinally();");
        afterCode.append("}");
        ctMethod.insertAfter(afterCode.toString(), true);
    }

}
