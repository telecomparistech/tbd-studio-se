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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.ui.swt.tableviewer.IModifiedBeanListener;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.nosql.NoSQLConnection;
import org.talend.repository.nosql.constants.INoSQLCommonAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBConstants;
import org.talend.repository.nosql.db.model.couchdb.CouchDBReplicateFieldModel;
import org.talend.repository.nosql.db.util.couchdb.CouchDBConnectionUtil;
import org.talend.repository.nosql.exceptions.NoSQLGeneralException;
import org.talend.repository.nosql.factory.NoSQLRepositoryFactory;
import org.talend.repository.nosql.i18n.Messages;
import org.talend.repository.nosql.ui.common.AbstractNoSQLConnForm;
import org.talend.repository.nosql.validator.NonemptyValidator;
import org.talend.repository.nosql.validator.SpecialValueValidator;
import org.talend.utils.json.JSONException;

/**
 * created by cmeng on Dec 24, 2014 Detailled comment
 *
 */
public class CouchDBConnForm extends AbstractNoSQLConnForm {

    protected Composite couchDBContainer;

    protected ScrolledComposite scrollComposite;

    protected Button checkRequireAuthBtn;

    protected Group connectionGroup;

    protected Group authGroup;

    protected Group replicationGroup;

    protected LabelledCombo dbVersionCombo;

    protected LabelledText serverText;

    protected LabelledText portText;

    protected LabelledText databaseText;

    protected LabelledText userText;

    protected LabelledText pwdText;

    protected Button checkUseTriggerReplication;

    protected Composite replicationComposite;

    protected List<Map<String, Object>> replicateList = new ArrayList<Map<String, Object>>();;

    protected CouchDBReplicateTableView replicateTableView;

    /**
     * DOC cmeng CouchDBConnForm constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param existingNames
     * @param creation
     * @param parentWizardPage
     */
    public CouchDBConnForm(Composite parent, ConnectionItem connectionItem, String[] existingNames, boolean creation,
            WizardPage parentWizardPage) {
        super(parent, connectionItem, existingNames, creation, parentWizardPage);
        setupForm(true);
        GridLayout layout = (GridLayout) getLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.nosql.ui.common.AbstractNoSQLConnForm#addConnFields(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void addConnFields(Composite parent) {

        Group parentGroup = new Group(parent, SWT.NONE);
        parentGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        parentGroup.setLayout(new FillLayout());

        scrollComposite = new ScrolledComposite(parentGroup, SWT.V_SCROLL);
        scrollComposite.setLayout(new FillLayout());
        couchDBContainer = new Composite(scrollComposite, SWT.NONE);
        couchDBContainer.setLayout(new GridLayout(1, false));

        addConnectionGroup(couchDBContainer);
        addAuthenticationGroup(couchDBContainer);
        addReplicationGroup(couchDBContainer);

        scrollComposite.setContent(couchDBContainer);
        scrollComposite.setExpandHorizontal(true);
        scrollComposite.setExpandVertical(true);
        scrollComposite.setMinSize(couchDBContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    @Override
    protected void initializeFields() {
        NoSQLConnection conn = getConnection();
        String dbVersion = conn.getAttributes().get(INoSQLCommonAttributes.DB_VERSION);
        String server = conn.getAttributes().get(INoSQLCommonAttributes.HOST);
        String port = conn.getAttributes().get(INoSQLCommonAttributes.PORT);
        String database = conn.getAttributes().get(INoSQLCommonAttributes.DATABASE);
        String user = conn.getAttributes().get(INoSQLCommonAttributes.USERNAME);
        String passwd = conn.getValue(conn.getAttributes().get(INoSQLCommonAttributes.PASSWORD), false);
        boolean isUseRequireAuth = Boolean.parseBoolean(conn.getAttributes().get(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION));
        boolean isUseTriggerReplication = Boolean
                .parseBoolean(conn.getAttributes().get(ICouchDBAttributes.USE_TRIGGER_REPLICATE));
        if (validText(dbVersion)) {
            dbVersionCombo.setText(repositoryTranslator.getLabel(dbVersion));
        } else {
            dbVersionCombo.select(0);
        }
        serverText.setText(server == null ? ICouchDBConstants.DEFAULT_HOST : server);
        portText.setText(port == null ? ICouchDBConstants.DEFAULT_PORT : port);
        databaseText.setText(database == null ? "" : database); //$NON-NLS-1$
        checkRequireAuthBtn.setSelection(isUseRequireAuth);
        checkUseTriggerReplication.setSelection(isUseTriggerReplication);
        if (checkRequireAuthBtn.getSelection()) {
            userText.setText(user == null ? "" : user); //$NON-NLS-1$
            pwdText.setText(passwd == null ? "" : passwd); //$NON-NLS-1$
        }
        updateReplicateField();
        updateAuthGroup();
    }

    @Override
    protected void saveAttributes() {
        super.saveAttributes();
        NoSQLConnection conn = getConnection();
        conn.getAttributes().put(INoSQLCommonAttributes.DB_VERSION, repositoryTranslator.getValue(dbVersionCombo.getText()));
        conn.getAttributes().put(INoSQLCommonAttributes.HOST, serverText.getText());
        conn.getAttributes().put(INoSQLCommonAttributes.PORT, portText.getText());
        conn.getAttributes().put(INoSQLCommonAttributes.DATABASE, databaseText.getText());
        conn.getAttributes().put(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION,
                String.valueOf(checkRequireAuthBtn.getSelection()));
        conn.getAttributes().put(INoSQLCommonAttributes.USERNAME, userText.getText());
        conn.getAttributes().put(INoSQLCommonAttributes.PASSWORD, conn.getValue(pwdText.getText(), true));
        conn.getAttributes().put(ICouchDBAttributes.USE_TRIGGER_REPLICATE,
                String.valueOf(checkUseTriggerReplication.getSelection()));
        saveReplicateModel();
    }

    @Override
    protected void updateAttributes() {
        super.updateAttributes();
        attributes.add(INoSQLCommonAttributes.HOST);
        attributes.add(INoSQLCommonAttributes.PORT);
        attributes.add(INoSQLCommonAttributes.DATABASE);
        if (checkRequireAuthBtn.getSelection()) {
            attributes.add(INoSQLCommonAttributes.USERNAME);
            attributes.add(INoSQLCommonAttributes.PASSWORD);
        }
    }

    private void initReplicateField() {
        String replication = getConnection().getAttributes().get(ICouchDBAttributes.REPLICATE_TARGET_DB);
        replicateList = CouchDBConnectionUtil.getReplicationDBs(replication);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.ui.wizard.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        dbVersionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().getAttributes().put(INoSQLCommonAttributes.DB_VERSION,
                        repositoryTranslator.getValue(dbVersionCombo.getText()));
            }
        });

        serverText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().getAttributes().put(INoSQLCommonAttributes.HOST, serverText.getText());
            }
        });

        portText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().getAttributes().put(INoSQLCommonAttributes.PORT, portText.getText());
            }
        });

        databaseText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                Text textControl = databaseText.getTextControl();
                String text = textControl.getText();
                if (!isContextMode() && !text.toLowerCase().equals(text)) {
                    Point selection = textControl.getSelection();
                    textControl.setText(textControl.getText().toLowerCase());
                    textControl.setSelection(selection);
                }
                checkFieldsValue();
                getConnection().getAttributes().put(INoSQLCommonAttributes.DATABASE, databaseText.getText());
            }
        });

        checkRequireAuthBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkFieldsValue();
                updateAuthGroup();
                updateAttributes();
                getConnection().getAttributes().put(INoSQLCommonAttributes.REQUIRED_AUTHENTICATION,
                        String.valueOf(checkRequireAuthBtn.getSelection()));
            }
        });

        userText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                getConnection().getAttributes().put(INoSQLCommonAttributes.USERNAME, userText.getText());
            }
        });

        pwdText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
                NoSQLConnection conn = getConnection();
                conn.getAttributes().put(INoSQLCommonAttributes.PASSWORD, conn.getValue(pwdText.getText(), true));
            }
        });

        checkUseTriggerReplication.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateReplicateField();
                checkFieldsValue();
                getConnection().getAttributes().put(ICouchDBAttributes.USE_TRIGGER_REPLICATE,
                        String.valueOf(checkUseTriggerReplication.getSelection()));
            }
        });

        replicateTableView.getExtendedTableModel().addAfterOperationListListener(new IListenableListListener() {

            @Override
            public void handleEvent(ListenableListEvent event) {
                saveReplicateModel();
            }
        });

        replicateTableView.getExtendedTableModel().addModifiedBeanListener(new IModifiedBeanListener<Map<String, Object>>() {

            @Override
            public void handleEvent(ModifiedBeanEvent<Map<String, Object>> event) {
                saveReplicateModel();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.nosql.ui.common.AbstractNoSQLConnForm#updateValidatorEntries()
     */
    @Override
    protected void updateValidatorEntries() {
        super.updateValidatorEntries();
        collectValidateEntry(new SpecialValueValidator(dbVersionCombo.getSelectionIndex(), -1), true,
                Messages.getString("AbstractNoSQLConnForm.InvalidDBVersion")); //$NON-NLS-1$
        collectValidateEntry(new NonemptyValidator(serverText.getText()),
                Messages.getString("AbstractNoSQLConnForm.InvalidSever")); //$NON-NLS-1$
        collectValidateEntry(new NonemptyValidator(portText.getText()), Messages.getString("AbstractNoSQLConnForm.InvalidPort")); //$NON-NLS-1$
        if (checkRequireAuthBtn.getSelection()) {
            collectValidateEntry(new NonemptyValidator(userText.getText()),
                    Messages.getString("AbstractNoSQLConnForm.InvalidUser")); //$NON-NLS-1$
            collectValidateEntry(new NonemptyValidator(pwdText.getText()), Messages.getString("AbstractNoSQLConnForm.InvalidPwd")); //$NON-NLS-1$
        }
    }

    @Override
    public void releaseResources() throws NoSQLGeneralException {
        super.releaseResources();
        // TODO
    }

    @Override
    protected void updateEditableStatus(boolean editable) {
        dbVersionCombo.setEnabled(editable);
        checkUseTriggerReplication.setEnabled(editable);
        serverText.setEnabled(editable);
        portText.setEnabled(editable);
        databaseText.setEnabled(editable);
        checkRequireAuthBtn.setEnabled(editable);
        boolean enableAuth = checkRequireAuthBtn.isEnabled() && checkRequireAuthBtn.getSelection();
        userText.setEditable(editable && enableAuth);
        pwdText.setEditable(editable && enableAuth);
    }

    protected void saveReplicateModel() {
        try {
            CouchDBReplicateFieldModel model = (CouchDBReplicateFieldModel) replicateTableView.getExtendedTableModel();
            getConnection().getAttributes().put(ICouchDBAttributes.REPLICATE_TARGET_DB, model.getBeanListString());
        } catch (JSONException e) {
            // nothing to do
        }
    }

    protected void updateAuthGroup() {
        if (!isContextMode()) {
            boolean selection = checkRequireAuthBtn.getSelection();
            userText.setEditable(selection);
            pwdText.setEditable(selection);
        } else {
            userText.setEditable(false);
            pwdText.setEditable(false);
        }
    }

    protected void updateReplicateField() {
        boolean show = checkUseTriggerReplication.getSelection();
        setHideWidgets(replicationComposite, !show);
        couchDBContainer.layout();
    }

    protected void addConnectionGroup(Composite parent) {
        // create connection group
        connectionGroup = Form.createGroup(parent, 4, Messages.getString("CouchDBConnForm.connection")); //$NON-NLS-1$
        connectionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout gpGrid = new GridLayout(4, false);
        gpGrid.marginWidth = 5;
        gpGrid.marginHeight = 5;
        connectionGroup.setLayout(gpGrid);

        // create connection composite
        Composite connComposite = new Composite(connectionGroup, SWT.NONE);
        connComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        GridLayout connCompLayout = new GridLayout(4, false);
        connCompLayout.marginWidth = 0;
        connCompLayout.marginHeight = 0;
        connComposite.setLayout(connCompLayout);

        NoSQLRepositoryFactory repFactory = NoSQLRepositoryFactory.getInstance();
        List<String> dbVersions = repFactory.getDBVersions(getConnection().getDbType());
        List<String> dbVersionLabels = repositoryTranslator.getLabels(dbVersions);
        dbVersionCombo = new LabelledCombo(
                connComposite,
                Messages.getString("CouchDBConnForm.dbVersion"), Messages.getString("CouchDBConnForm.dbVersionTip"), dbVersionLabels.toArray(new String[0]), 3, true); //$NON-NLS-1$ //$NON-NLS-2$
        serverText = new LabelledText(connComposite, Messages.getString("CouchDBConnForm.server"), 1); //$NON-NLS-1$
        portText = new LabelledText(connComposite, Messages.getString("CouchDBConnForm.port"), 1); //$NON-NLS-1$
        databaseText = new LabelledText(connComposite, Messages.getString("CouchDBConnForm.database"), 1, true); //$NON-NLS-1$
    }

    protected void addAuthenticationGroup(Composite parent) {

        authGroup = Form.createGroup(parent, 4, Messages.getString("CouchDBConnForm.authentication")); //$NON-NLS-1$
        authGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout gpGrid = new GridLayout(4, false);
        gpGrid.marginWidth = 5;
        gpGrid.marginHeight = 5;
        authGroup.setLayout(gpGrid);
        checkRequireAuthBtn = new Button(authGroup, SWT.CHECK);
        checkRequireAuthBtn.setText(Messages.getString("CouchDBConnForm.requireAuth")); //$NON-NLS-1$
        checkRequireAuthBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 4, 1));
        userText = new LabelledText(authGroup, Messages.getString("CouchDBConnForm.username"), 1); //$NON-NLS-1$
        pwdText = new LabelledText(authGroup,
                Messages.getString("CouchDBConnForm.password"), 1, SWT.PASSWORD | SWT.BORDER | SWT.SINGLE); //$NON-NLS-1$

    }

    protected void addReplicationGroup(Composite parent) {
        replicationGroup = Form.createGroup(parent, 1, Messages.getString("CouchDBConnForm.replicationGroup")); //$NON-NLS-1$
        replicationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout groupLayout = new GridLayout(1, false);
        groupLayout.marginWidth = 5;
        groupLayout.marginHeight = 5;
        replicationGroup.setLayout(groupLayout);
        checkUseTriggerReplication = new Button(replicationGroup, SWT.CHECK);
        checkUseTriggerReplication.setText(Messages.getString("CouchDBConnForm.checkUseTriggerReplication")); //$NON-NLS-1$
        checkUseTriggerReplication.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false, 1, 1));

        initReplicateField();

        // create replicate field
        replicationComposite = new Composite(replicationGroup, SWT.NONE);
        replicationComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
        GridLayout replicateLayout = new GridLayout();
        replicateLayout.marginWidth = 0;
        replicateLayout.marginHeight = 0;
        replicationComposite.setLayout(replicateLayout);

        CouchDBReplicateFieldModel model = new CouchDBReplicateFieldModel(replicateList,
                Messages.getString("CouchDBConnForm.replicateView")); //$NON-NLS-1$
        replicateTableView = new CouchDBReplicateTableView(model, replicationComposite);
        Composite fieldTableEditorComposite = replicateTableView.getMainComposite();
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        gridData.heightHint = 180;
        fieldTableEditorComposite.setLayoutData(gridData);
        fieldTableEditorComposite.setBackground(null);
    }

}
