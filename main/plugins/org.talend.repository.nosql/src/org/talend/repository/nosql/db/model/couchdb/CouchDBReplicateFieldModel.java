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
package org.talend.repository.nosql.db.model.couchdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * created by cmeng on Dec 25, 2014 Detailled comment
 *
 */
public class CouchDBReplicateFieldModel extends ExtendedTableModel<Map<String, Object>> {

    public CouchDBReplicateFieldModel(String name) {
        super(name);
        setProperties(new ArrayList<Map<String, Object>>());
    }

    public CouchDBReplicateFieldModel(List<Map<String, Object>> replicaList, String name) {
        super(name);
        setProperties(replicaList);
    }

    public void setProperties(List<Map<String, Object>> properties) {
        registerDataList(properties);
    }

    public Map<String, Object> createReplicateType() {
        return new HashMap<String, Object>();
    }

    public String getBeanListString() throws JSONException {
        JSONArray jsonArr = new JSONArray();
        for (Map<String, Object> map : getBeansList()) {
            JSONObject object = new JSONObject();
            Iterator<String> it = map.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                object.put(key, map.get(key));
            }
            jsonArr.put(object);
        }
        return jsonArr.toString();
    }

}
