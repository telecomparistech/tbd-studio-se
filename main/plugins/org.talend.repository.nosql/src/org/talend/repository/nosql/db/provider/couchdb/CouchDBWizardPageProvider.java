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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.nosql.db.ui.couchdb.CouchDBConnForm;
import org.talend.repository.nosql.ui.common.AbstractNoSQLConnForm;
import org.talend.repository.nosql.ui.provider.AbstractWizardPageProvider;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public class CouchDBWizardPageProvider extends AbstractWizardPageProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.nosql.ui.provider.IWizardPageProvider#createConnectionForm(org.eclipse.swt.widgets.Composite
     * , org.talend.core.model.properties.ConnectionItem, java.lang.String[], boolean,
     * org.eclipse.jface.wizard.WizardPage)
     */
    @Override
    public AbstractNoSQLConnForm createConnectionForm(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            boolean creation, WizardPage parentWizardPage) {
        return new CouchDBConnForm(parent, connectionItem, existingNames, creation, parentWizardPage);
    }

}
