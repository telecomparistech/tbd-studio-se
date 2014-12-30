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

import org.talend.repository.nosql.constants.INoSQLCommonAttributes;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public interface ICouchDBAttributes extends INoSQLCommonAttributes {

    public static final String USE_TRIGGER_REPLICATE = "USE_TRIGGER_REPLICATE"; //$NON-NLS-1$

    public static final String REPLICATE_TARGET_DB = "REPLICATE_TARGET_DB"; //$NON-NLS-1$

    public static final String TARGET_DB_NAME = "TARGET_DB_NAME"; //$NON-NLS-1$

    public static final String CONTINUOUS = "CONTINUOUS"; //$NON-NLS-1$

    public static final String CREATE_TARGET = "CREATE_TARGET"; //$NON-NLS-1$

    public static final String CANCEL_REPLICATE = "CANCEL_REPLICATE"; //$NON-NLS-1$

    public static final String PARAMETER = "PARAMETER"; //$NON-NLS-1$

    public static final String VALUE = "VALUE"; //$NON-NLS-1$

    public static final String QUERY_BY_VIEW = "QUERY_BY_VIEW"; //$NON-NLS-1$

    public static final String DOC = "DOC"; //$NON-NLS-1$

    public static final String VIEW = "VIEW"; //$NON-NLS-1$

    public static final String STARTKEY = "STARTKEY"; //$NON-NLS-1$

    public static final String ENDKEY = "ENDKEY"; //$NON-NLS-1$

    public static final String STARTKEY_DOCID = "STARTKEY_DOCID"; //$NON-NLS-1$

    public static final String ENDKEY_DOCID = "ENDKEY_DOCID"; //$NON-NLS-1$

    public static final String ISREDUCE = "ISREDUCE"; //$NON-NLS-1$

    public static final String INCLUDE_DOCS = "INCLUDE_DOCS"; //$NON-NLS-1$

    public static final String DESCENDING = "DESCENDING"; //$NON-NLS-1$

    public static final String GROUP = "GROUP"; //$NON-NLS-1$

    public static final String GROUP_LEVEL = "GROUP_LEVEL"; //$NON-NLS-1$

    public static final String ADD_OPTION = "ADD_OPTION"; //$NON-NLS-1$

    public static final String OPTIONS = "OPTIONS"; //$NON-NLS-1$
}
