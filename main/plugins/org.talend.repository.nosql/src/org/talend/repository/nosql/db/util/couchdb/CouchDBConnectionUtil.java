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

import org.apache.commons.lang.StringUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.constants.INoSQLCommonAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.exceptions.NoSQLServerException;
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

        CouchDBConnector couchDBConnector = null;
        try {
            couchDBConnector = new CouchDBConnector(connection);
            String dbName = connection.getAttributes().get(INoSQLCommonAttributes.DATABASE);
            if (StringUtils.isNotEmpty(dbName)) {
                couchDBConnector.checkDbName(dbName);
            }
            checkResult = true;
        } catch (NoSQLServerException e) {
            throw e;
        } finally {
            if (couchDBConnector != null) {
                couchDBConnector.closeConnection();
            }
        }

        return checkResult;
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
