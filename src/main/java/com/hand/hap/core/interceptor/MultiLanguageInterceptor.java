/*
 * #{copyright}#
 */
package com.hand.hap.core.interceptor;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Table;

import com.hand.hap.core.annotation.MultiLanguage;
import com.hand.hap.core.ILanguageProvider;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ITlTableNameProvider;
import com.hand.hap.core.impl.DefaultTlTableNameProvider;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.dto.DTOClassInfo;
import com.hand.hap.system.dto.Language;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hand.hap.system.dto.BaseDTO;

/**
 * 自动数据多语言支持.
 *
 * @author shengyang.zhou@hand-china.com
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class MultiLanguageInterceptor implements Interceptor {

    private ITlTableNameProvider tableNameProvider = DefaultTlTableNameProvider.getInstance();

    @Autowired
    private ILanguageProvider languageProvider;

    private Logger logger = LoggerFactory.getLogger(MultiLanguageInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof Executor) {
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object domain = invocation.getArgs()[1];
            if (domain instanceof BaseDTO) {
                BaseDTO dtoObj = (BaseDTO) domain;
                if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT
                        || mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {
                    Object obj = invocation.proceed();
                    proceedMultiLanguage(dtoObj, invocation, mappedStatement);
                    return obj;
                } else if (mappedStatement.getSqlCommandType() == SqlCommandType.DELETE) {
                    Object obj = invocation.proceed();
                    proceedDeleteMultiLanguage(dtoObj, invocation);
                    return obj;
                }
            }
            return invocation.proceed();
        }
        return invocation.proceed();
    }

    private void proceedMultiLanguage(BaseDTO parameterObject, Invocation invocation, MappedStatement mappedStatement)
            throws IllegalArgumentException, IllegalAccessException, SQLException {
        Class<?> clazz = parameterObject.getClass();
        MultiLanguage multiLanguageTable = clazz.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable == null) {
            return;
        }
        Table table = clazz.getAnnotation(Table.class);
        notNull(table, "annotation @Table not found!");
        String tableName = table.name();
        hasText(tableName, "@Table name not found!");
        tableName = tableNameProvider.getTlTableName(tableName);
        if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
            proceedInsertMultiLanguage(tableName, parameterObject, (Executor) invocation.getTarget());
        } else if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {
            if (parameterObject.get__tls().isEmpty()) {
                proceedUpdateMultiLanguage(tableName, parameterObject, (Executor) invocation.getTarget());
            } else {
                proceedUpdateMultiLanguage2(tableName, parameterObject, (Executor) invocation.getTarget());
            }
        }
    }

    private void proceedDeleteMultiLanguage(BaseDTO parameterObject, Invocation invocation)
            throws IllegalArgumentException, IllegalAccessException, SQLException {
        Class<?> clazz = parameterObject.getClass();
        MultiLanguage multiLanguageTable = clazz.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable == null) {
            return;
        }
        Table table = clazz.getAnnotation(Table.class);
        notNull(table, "annotation @Table not found!");
        String tableName = table.name();
        hasText(tableName, "@Table name not found!");
        tableName = tableNameProvider.getTlTableName(tableName);

        List<Object> objs = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        for (Field f : DTOClassInfo.getIdFields(clazz)) {
            f.setAccessible(true);
            Object v = f.get(parameterObject);
            keys.add(DTOClassInfo.getColumnName(f) + "=?");
            objs.add(v);
        }
        for (Object pkv : objs) {
            if (pkv == null) {
                // 主键中有 null
                return;
            }
        }
        if (keys.size() > 0) {
            Executor executor = (Executor) invocation.getTarget();
            StringBuilder sql = new StringBuilder("DELETE FROM ");
            sql.append(tableName).append(" WHERE ").append(StringUtils.join(keys, " AND "));
            executeSql(executor.getTransaction().getConnection(), sql.toString(), objs);
        }
    }

    private void proceedInsertMultiLanguage(String tableName, BaseDTO parameterObject, Executor executor)
            throws IllegalArgumentException, IllegalAccessException, SQLException {

        Class<?> clazz = parameterObject.getClass();
        List<String> keys = new ArrayList<>();
        List<Object> objs = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + "(");
        for (Field f : DTOClassInfo.getIdFields(clazz)) {
            String columnName = DTOClassInfo.getColumnName(f);
            keys.add(columnName);
            placeholders.add("?");
            objs.add(f.get(parameterObject));
        }
        keys.add("LANG");
        placeholders.add("?");
        objs.add(null); // 占位符

        Field[] mlFields = DTOClassInfo.getMultiLanguageFields(clazz);
        for (Field f : mlFields) {
            keys.add(DTOClassInfo.getColumnName(f));
            placeholders.add("?");
            Map<String, String> tls = parameterObject.get__tls().get(f.getName());
            if (tls == null) {
                // if multi language value not exists in __tls, then use
                // value on current field
                objs.add(f.get(parameterObject));
                continue;
            }
            objs.add(null); // 占位符
        }
        keys.add("CREATED_BY");
        placeholders.add("" + parameterObject.getCreatedBy());

        keys.add("CREATION_DATE");
        placeholders.add("CURRENT_TIMESTAMP");

        keys.add("LAST_UPDATED_BY");
        placeholders.add("" + parameterObject.getCreatedBy());

        keys.add("LAST_UPDATE_DATE");
        placeholders.add("CURRENT_TIMESTAMP");

        sql.append(StringUtils.join(keys, ","));
        sql.append(") VALUES (").append(StringUtils.join(placeholders, ",")).append(")");

        List<Language> languages = languageProvider.getSupportedLanguages();
        for (Language language : languages) {
            objs.set(objs.size() - mlFields.length - 1, language.getLangCode());
            for (int i = 0; i < mlFields.length; i++) {
                int idx = objs.size() - mlFields.length + i;
                Map<String, String> tls = parameterObject.get__tls().get(mlFields[i].getName());
                if (tls != null) {
                    objs.set(idx, tls.get(language.getLangCode()));
                }
                // 当tls为null时,不设置值(使用field的值,旧模式)
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Insert TL(Batch):{}", sql.toString());
                logger.debug("Parameters:{}", StringUtils.join(objs, ", "));
            }
            executeSql(executor.getTransaction().getConnection(), sql.toString(), objs);
        }
    }

    private void proceedUpdateMultiLanguage(String tableName, BaseDTO parameterObject, Executor executor)
            throws SQLException, IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = parameterObject.getClass();
        List<String> sets = new ArrayList<>();
        List<Object> objs = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Field field : DTOClassInfo.getMultiLanguageFields(clazz)) {
            Object value = field.get(parameterObject);
            if (value == null) {
                continue;
            }
            sets.add(DTOClassInfo.getColumnName(field) + "=?");
            objs.add(value);
        }
        if (sets.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("None multi language field has TL value. skip update.");
            }
            return;
        }

        sets.add("LAST_UPDATED_BY=" + parameterObject.getLastUpdatedBy());
        sets.add("LAST_UPDATE_DATE=CURRENT_TIMESTAMP");

        for (Field field : DTOClassInfo.getIdFields(clazz)) {
            keys.add(DTOClassInfo.getColumnName(field) + "=?");
            objs.add(field.get(parameterObject));
        }
        keys.add("LANG=?");
        IRequest iRequest = RequestHelper.getCurrentRequest(true);
        objs.add(iRequest.getLocale());

        sql.append(StringUtils.join(sets, ","));
        sql.append(" WHERE ").append(StringUtils.join(keys, " AND "));
        if (logger.isDebugEnabled()) {
            logger.debug("Update TL(Classic):{}", sql.toString());
            logger.debug("Parameters:{}", StringUtils.join(objs, ","));
        }
        executeSql(executor.getTransaction().getConnection(), sql.toString(), objs);
    }

    private void proceedUpdateMultiLanguage2(String tableName, BaseDTO parameterObject, Executor executor)
            throws SQLException, IllegalArgumentException, IllegalAccessException {

        Class<?> clazz = parameterObject.getClass();
        List<String> sets = new ArrayList<>();
        List<String> updateFieldNames = new ArrayList<>();

        List<Object> objs = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Field field : DTOClassInfo.getMultiLanguageFields(clazz)) {
            Map<String, String> tls = parameterObject.get__tls().get(field.getName());
            if (tls == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("TL value for field '{}' not exists.", field.getName());
                }
                // if tl value not exists in __tls, skip.
                continue;
            }
            sets.add(DTOClassInfo.getColumnName(field) + "=?");
            updateFieldNames.add(field.getName());
            objs.add(null); // just a placeholder
        }
        if (sets.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("None multi language field has TL value. skip update.");
                return;
            }
        }

        sets.add("LAST_UPDATED_BY=" + parameterObject.getLastUpdatedBy());
        sets.add("LAST_UPDATE_DATE=CURRENT_TIMESTAMP");

        for (Field field : DTOClassInfo.getIdFields(clazz)) {
            keys.add(DTOClassInfo.getColumnName(field) + "=?");
            objs.add(field.get(parameterObject));
        }
        keys.add("LANG=?");
        objs.add(null); // just a place holder

        sql.append(StringUtils.join(sets, ","));
        sql.append(" WHERE ").append(StringUtils.join(keys, " AND "));

        List<Language> languages = languageProvider.getSupportedLanguages();
        for (Language language : languages) {
            // 前面几个参数都是多语言数据,需要每次更新
            for (int i = 0; i < updateFieldNames.size(); i++) {
                Map<String, String> tls = parameterObject.get__tls().get(updateFieldNames.get(i));
                objs.set(i, tls.get(language.getLangCode()));
            }

            // 最后一个参数是语言环境
            objs.set(objs.size() - 1, language.getLangCode());

            if (logger.isDebugEnabled()) {
                logger.debug("Update TL(Batch):{}", sql.toString());
                logger.debug("Parameters:{}", StringUtils.join(objs, ", "));
            }
            executeSql(executor.getTransaction().getConnection(), sql.toString(), objs);
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    public ITlTableNameProvider getTableNameProvider() {
        return tableNameProvider;
    }

    public void setTableNameProvider(ITlTableNameProvider tableNameProvider) {
        this.tableNameProvider = tableNameProvider;
    }

    protected void executeSql(Connection connection, String sql, List<Object> params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int i = 1;
            for (Object obj : params) {
                ps.setObject(i++, obj);
            }
            ps.execute();
        }
    }

}
