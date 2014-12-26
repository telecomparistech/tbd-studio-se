// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.nosql.db.util.couchdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.constants.INoSQLCommonAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.util.couchdb.LightCouchReflection.CouchDbClient;
import org.talend.repository.nosql.db.util.couchdb.LightCouchReflection.Replication;
import org.talend.repository.nosql.db.util.couchdb.LightCouchReflection.ReplicationResult;
import org.talend.repository.nosql.exceptions.NoSQLServerException;
import org.talend.repository.nosql.i18n.Messages;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * created by cmeng on Dec 25, 2014 Detailled comment
 *
 */
public class CouchDBConnectionUtil {

    public static synchronized boolean checkConnection(NoSQLConnection connection) throws NoSQLServerException {
        boolean checkResult = false;

        // Step 1. get the essential attributes
        EMap<String, String> attributesMap = connection.getAttributes();

        String dbName = attributesMap.get(INoSQLCommonAttributes.DATABASE);
        String protocol = "http"; //$NON-NLS-1$
        String host = attributesMap.get(INoSQLCommonAttributes.HOST);

        String strPort = "0"; //$NON-NLS-1$
        Object objPort = attributesMap.get(INoSQLCommonAttributes.PORT);
        if (objPort != null) {
            strPort = objPort.toString();
        }

        String userName = null;
        String password = null;

        boolean requiredAuth = false;
        Object objRequiredAuth = attributesMap.get(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION);
        if (objRequiredAuth != null) {
            requiredAuth = Boolean.valueOf(objRequiredAuth.toString());
            if (requiredAuth) {
                userName = attributesMap.get(INoSQLCommonAttributes.USERNAME);
                password = connection.getValue(attributesMap.get(INoSQLCommonAttributes.PASSWORD), false);
            }
        }

        boolean useTriggerReplication = false;
        List<Map<String, Object>> replicationDBs = null;
        Object objUseTriggerReplication = attributesMap.get(ICouchDBAttributes.USE_TRIGGER_REPLICATE);
        if (objUseTriggerReplication != null) {
            useTriggerReplication = Boolean.valueOf(objUseTriggerReplication.toString());
            if (useTriggerReplication) {
                String jsonReplicationDBs = attributesMap.get(ICouchDBAttributes.REPLICATE_TARGET_DB);
                if (StringUtils.isNotEmpty(jsonReplicationDBs)) {
                    replicationDBs = getReplicationDBs(jsonReplicationDBs);
                }
            }
        }

        if (connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection);
            dbName = ContextParameterUtils.getOriginalValue(contextType, dbName);
            host = ContextParameterUtils.getOriginalValue(contextType, host);
            if (objPort != null) {
                strPort = ContextParameterUtils.getOriginalValue(contextType, strPort);
            }
            if (requiredAuth) {
                userName = ContextParameterUtils.getOriginalValue(contextType, userName);
                password = ContextParameterUtils.getOriginalValue(contextType, password);
            }
        }

        // Step 2. try to connect
        LightCouchReflection lightCouch = LightCouchReflection.getInstance(connection);
        List<Replication> replications = null;
        CouchDbClient couchDbClient = null;
        try {
            // Just in case the empty dbname excpetion throwed by CouchDbClient
            String dummyDbName = "dummyDbName"; //$NON-NLS-1$
            if (!dbName.isEmpty()) {
                dummyDbName = dbName;
            }
            couchDbClient = lightCouch.new CouchDbClient(dummyDbName, false, protocol, host, Integer.valueOf(strPort), userName,
                    password);
            if (useTriggerReplication && replicationDBs != null) {
                replications = useTriggerReplication(dbName, replicationDBs, couchDbClient);
            }
            if (!dbName.isEmpty()) {
                checkResult = checkDbName(dbName, couchDbClient);
            } else {
                checkResult = true;
            }
        } catch (NoSQLServerException nosqlException) {
            throw nosqlException;
        } catch (Exception e) {
            throw new NoSQLServerException(e);
        } finally {
            closeConnection(replications, couchDbClient);
        }

        return checkResult;
    }

    protected static List<Replication> useTriggerReplication(String dbName, List<Map<String, Object>> replicationDBs,
            CouchDbClient couchDbClient) throws Exception {
        List<Replication> replications = new ArrayList<LightCouchReflection.Replication>();
        for (Map<String, Object> replicationBean : replicationDBs) {
            String targetDBName = (String) replicationBean.get(ICouchDBAttributes.TARGET_DB_NAME);
            boolean continuous = Boolean.valueOf("" + replicationBean.get(ICouchDBAttributes.CONTINUOUS)); //$NON-NLS-1$
            boolean createTarget = Boolean.valueOf("" + replicationBean.get(ICouchDBAttributes.CREATE_TARGET)); //$NON-NLS-1$
            boolean cancelReplicate = Boolean.valueOf("" + replicationBean.get(ICouchDBAttributes.CANCEL_REPLICATE)); //$NON-NLS-1$

            ReplicationResult replicationResult = couchDbClient.replication().source(dbName).target(targetDBName)
                    .createTarget(createTarget).continuous(continuous).trigger();
            if (cancelReplicate && replicationResult.isOk()) {
                Replication replication = couchDbClient.replication().source(dbName).target(targetDBName)
                        .createTarget(createTarget).continuous(continuous).cancel(cancelReplicate);
                replications.add(replication);
            }
        }
        return replications;
    }

    protected static boolean checkDbName(String dbName, CouchDbClient couchDbClient) throws Exception {
        boolean checkResult = false;
        List<String> dbs = couchDbClient.context().getAllDbs();
        boolean find = false;
        for (String db : dbs) {
            if (db.equals(dbName)) {
                find = true;
                break;
            }
        }
        if (find) {
            checkResult = true;
        } else {
            throw new NoSQLServerException(Messages.getString(
                    "CouchDBConnectionUtil.checkConnection.badDBName", new Object[] { dbName })); //$NON-NLS-1$
        }
        return checkResult;
    }

    public static void closeConnection(List<Replication> replications, CouchDbClient couchDbClient) {
        if (replications != null) {
            for (Replication replication : replications) {
                try {
                    replication.trigger();
                } catch (Exception e) {
                    ExceptionHandler.process(e, Priority.WARN);
                }
            }
        }
        if (couchDbClient != null) {
            try {
                couchDbClient.shutdown();
            } catch (Exception e) {
                ExceptionHandler.process(e, Priority.WARN);
            }
        }
    }

    public static List<Map<String, Object>> getReplicationDBs(String replication) {
        List<Map<String, Object>> replicateList = new ArrayList<Map<String, Object>>();
        try {
            if (StringUtils.isNotEmpty(replication)) {
                JSONArray jsonArr = new JSONArray(replication);
                for (int i = 0; i < jsonArr.length(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject object = jsonArr.getJSONObject(i);
                    Iterator<Object> it = object.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        map.put(key, object.get(key));
                    }
                    replicateList.add(map);
                }
            }
        } catch (JSONException e) {
            ExceptionHandler.process(e, Priority.WARN);
        }
        return replicateList;
    }

    public static List<HashMap<String, Object>> getReplicateSetList(String replicaSetJsonStr, boolean includeQuotes)
            throws JSONException {
        List<HashMap<String, Object>> replicaSet = new ArrayList<HashMap<String, Object>>();
        if (StringUtils.isNotEmpty(replicaSetJsonStr)) {
            JSONArray jsonArr = new JSONArray(replicaSetJsonStr);
            for (int i = 0; i < jsonArr.length(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                JSONObject object = jsonArr.getJSONObject(i);
                Iterator<String> it = object.keys();
                while (it.hasNext()) {
                    String key = StringUtils.trimToNull(it.next());
                    String value = StringUtils.trimToNull(String.valueOf(object.get(key)));
                    if (includeQuotes) {
                        value = TalendQuoteUtils.addQuotesIfNotExist(value);
                    } else {
                        value = TalendQuoteUtils.removeQuotesIfExist(value);
                    }
                    if (ICouchDBAttributes.CREATE_TARGET.equals(key) || ICouchDBAttributes.CONTINUOUS.equals(key)
                            || ICouchDBAttributes.CANCEL_REPLICATE.equals(key)) {
                        value = TalendQuoteUtils.removeQuotesIfExist(value);
                    }
                    map.put(key, value);
                }
                replicaSet.add(map);
            }
        }

        return replicaSet;
    }

}
