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

import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.db.util.couchdb.CouchDBConnectionUtil;
import org.talend.repository.nosql.exceptions.NoSQLServerException;
import org.talend.repository.nosql.metadata.AbstractMetadataProvider;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public class CouchDBMetadataProvider extends AbstractMetadataProvider {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.nosql.metadata.IMetadataProvider#checkConnection(org.talend.repository.model.nosql.
     * NoSQLConnection)
     */
    @Override
    public boolean checkConnection(NoSQLConnection connection) throws NoSQLServerException {
        return CouchDBConnectionUtil.checkConnection(connection);
    }
}
