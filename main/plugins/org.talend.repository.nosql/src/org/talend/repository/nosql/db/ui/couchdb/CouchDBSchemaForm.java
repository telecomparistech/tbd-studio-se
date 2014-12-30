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
package org.talend.repository.nosql.db.ui.couchdb;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.nosql.ui.common.AbstractNoSQLSchemaForm;

/**
 * created by cmeng on Dec 29, 2014 Detailled comment
 *
 */
public class CouchDBSchemaForm extends AbstractNoSQLSchemaForm {

    /**
     * DOC cmeng CouchDBSchemaForm constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param metadataTable
     * @param creation
     * @param parentWizardPage
     */
    public CouchDBSchemaForm(Composite parent, ConnectionItem connectionItem, MetadataTable metadataTable, boolean creation,
            WizardPage parentWizardPage) {
        super(parent, connectionItem, metadataTable, creation, parentWizardPage);
        setupForm();
        // TODO Auto-generated constructor stub
    }

}
