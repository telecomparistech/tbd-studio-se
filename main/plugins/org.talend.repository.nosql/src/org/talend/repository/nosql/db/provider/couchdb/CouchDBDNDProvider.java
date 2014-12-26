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
package org.talend.repository.nosql.db.provider.couchdb;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.designerproperties.ComponentToRepositoryProperty;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.constants.INoSQLCommonAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.util.couchdb.CouchDBConnectionUtil;
import org.talend.repository.nosql.ui.dnd.AbstractDNDProvider;
import org.talend.utils.json.JSONException;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public class CouchDBDNDProvider extends AbstractDNDProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.nosql.ui.dnd.IDNDProvider#getRepositoryValue(org.talend.repository.model.nosql.NoSQLConnection
     * , java.lang.String, org.talend.core.model.metadata.IMetadataTable, java.lang.String)
     */
    @Override
    public Object getRepositoryValue(NoSQLConnection connection, String value, IMetadataTable table, String targetComponent) {
        if (INoSQLCommonAttributes.DB_VERSION.equals(value)) {
            return getCanonicalRepositoryValue(connection, connection.getAttributes().get(INoSQLCommonAttributes.DB_VERSION),
                    false);
        } else if (INoSQLCommonAttributes.HOST.equals(value)) {
            return getCanonicalRepositoryValue(connection, connection.getAttributes().get(INoSQLCommonAttributes.HOST));
        } else if (INoSQLCommonAttributes.PORT.equals(value)) {
            return getCanonicalRepositoryValue(connection, connection.getAttributes().get(INoSQLCommonAttributes.PORT), false);
        } else if (INoSQLCommonAttributes.DATABASE.equals(value)) {
            return getCanonicalRepositoryValue(connection, connection.getAttributes().get(INoSQLCommonAttributes.DATABASE));
        } else if (INoSQLCommonAttributes.REQUIRED_AUTHENTICATION.endsWith(value)) {
            return getCanonicalRepositoryValue(connection,
                    connection.getAttributes().get(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION), false);
        } else if (INoSQLCommonAttributes.USERNAME.equals(value)) {
            return getCanonicalRepositoryValue(connection, connection.getAttributes().get(INoSQLCommonAttributes.USERNAME));
        } else if (INoSQLCommonAttributes.PASSWORD.equals(value)) {
            return getCanonicalRepositoryValue(connection,
                    connection.getValue(connection.getAttributes().get(INoSQLCommonAttributes.PASSWORD), false));
        } else if (ICouchDBAttributes.USE_TRIGGER_REPLICATE.equals(value)) {
            return getCanonicalRepositoryValue(connection,
                    connection.getAttributes().get(ICouchDBAttributes.USE_TRIGGER_REPLICATE), false);
        } else if (ICouchDBAttributes.REPLICATE_TARGET_DB.equals(value)) {
            try {
                String replicateSetJsonStr = connection.getAttributes().get(ICouchDBAttributes.REPLICATE_TARGET_DB);
                return CouchDBConnectionUtil.getReplicateSetList(replicateSetJsonStr, true);
            } catch (JSONException e) {
                ExceptionHandler.process(e);
            }
        }

        return null;
    }

    @Override
    public void setRepositoryValue(NoSQLConnection connection, INode node, IElementParameter param) {
        if (INoSQLCommonAttributes.DB_VERSION.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(INoSQLCommonAttributes.DB_VERSION, value);
            }
        } else if (INoSQLCommonAttributes.HOST.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(INoSQLCommonAttributes.HOST, value);
            }
        } else if (INoSQLCommonAttributes.PORT.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(INoSQLCommonAttributes.PORT, value);
            }
        } else if (INoSQLCommonAttributes.DATABASE.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(INoSQLCommonAttributes.DATABASE, value);
            }
        } else if (INoSQLCommonAttributes.REQUIRED_AUTHENTICATION.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION, value);
            }
        } else if (ICouchDBAttributes.USE_TRIGGER_REPLICATE.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(ICouchDBAttributes.USE_TRIGGER_REPLICATE, value);
            }
        } else if (ICouchDBAttributes.REPLICATE_TARGET_DB.equals(param.getRepositoryValue())) {
            String value = ComponentToRepositoryProperty.getParameterValue(connection, node, param);
            if (value != null) {
                connection.getAttributes().put(ICouchDBAttributes.REPLICATE_TARGET_DB, value);
            }
        }
    }
}
