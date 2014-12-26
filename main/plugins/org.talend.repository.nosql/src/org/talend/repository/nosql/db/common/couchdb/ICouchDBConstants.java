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
package org.talend.repository.nosql.db.common.couchdb;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public interface ICouchDBConstants {

    public static final String DEFAULT_HOST = "localhost"; //$NON-NLS-1$

    public static final String DEFAULT_PORT = "5984"; //$NON-NLS-1$

    public static final String REPLICATE_TARGET_DB_NAME_DEFAULT = "newLine"; //$NON-NLS-1$

    public static final Boolean REPLICATE_CONTINUOUS_DEFAULT = true;

    public static final Boolean REPLICATE_CREATE_TARGET_DEFAULT = true;

    public static final Boolean REPLICATE_CANCEL_REPLICATE_DEFAULT = false;
}
