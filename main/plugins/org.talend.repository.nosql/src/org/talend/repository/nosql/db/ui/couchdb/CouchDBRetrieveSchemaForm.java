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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.utils.data.text.IndiceHelper;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditor;
import org.talend.core.ui.metadata.editor.MetadataEmfTableEditorView;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBConstants;
import org.talend.repository.nosql.db.model.couchdb.CouchDBJSONFieldModel;
import org.talend.repository.nosql.db.util.couchdb.CouchDBConnector;
import org.talend.repository.nosql.db.util.couchdb.LightCouchReflection.CouchDbClient;
import org.talend.repository.nosql.exceptions.NoSQLServerException;
import org.talend.repository.nosql.i18n.Messages;
import org.talend.repository.nosql.metadata.NoSQLSchemaUtil;
import org.talend.repository.nosql.ui.common.AbstractNoSQLRetrieveSchemaForm;
import org.talend.repository.nosql.validator.NonemptyValidator;
import org.talend.utils.json.JSONException;

/**
 * created by cmeng on Dec 26, 2014 Detailled comment
 *
 */
public class CouchDBRetrieveSchemaForm extends AbstractNoSQLRetrieveSchemaForm {

    protected static final String TABLE_NAME = "TABLE_NAME"; //$NON-NLS-1$

    protected static final String TABLE_COLUMNS_NUMBER = "TABLE_COLUMNS_NUMBER"; //$NON-NLS-1$

    protected List<Map<String, Object>> optionList = new ArrayList<Map<String, Object>>();

    protected List<Map<String, Object>> selectedTableList = new ArrayList<Map<String, Object>>();

    protected Composite searchGroupContainer;

    protected Button queryByViewButton;

    protected LabelledText designDocument;

    protected LabelledText view;

    protected LabelledText startKey;

    protected LabelledText endKey;

    protected LabelledText startKeyDocId;

    protected LabelledText endKeyDocId;

    protected Button isReduce;

    protected Button includeDocs;

    protected Button descending;

    protected Button group;

    protected LabelledText groupLevel;

    protected Button addOptionsButton;

    protected Composite optionsTableComposite;

    protected CouchDBJSONFieldModel optionListModel;

    protected Button retrieveButton;

    protected LabelledText tableName;

    protected Button addButton;

    protected LabelledText rowLimit;

    protected MetadataEmfTableEditorView tableView;

    protected MetadataEmfTableEditor tableEditor;

    protected Button selectAll;

    protected Button selectNone;

    protected Button deleteSelected;

    protected CheckboxTableViewer checkBoxTableViewer;

    protected MetadataTable table;

    /**
     * DOC cmeng CouchDBRetrieveSchemaForm constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param existingNames
     * @param creation
     * @param parentWizardPage
     */
    public CouchDBRetrieveSchemaForm(Composite parent, ConnectionItem connectionItem, String[] existingNames, boolean creation,
            WizardPage parentWizardPage) {
        super(parent, connectionItem, existingNames, creation, parentWizardPage);
        setupForm();
    }

    @Override
    protected void initialize() {
        // TODO
    };

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.ui.wizard.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        // TODO Auto-generated method stub
        SashForm verticalSashForm = new SashForm(this, SWT.VERTICAL | SWT.SMOOTH);
        verticalSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        verticalSashForm.setLayout(new GridLayout(1, true));

        addSearchConditionGroup(verticalSashForm);
        addStep2AndStep3(verticalSashForm);

        verticalSashForm.setSashWidth(2);
        verticalSashForm.SASH_WIDTH = 2;
        verticalSashForm.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        verticalSashForm.setWeights(new int[] { 50, 50 });
    }

    @Override
    protected void addFieldsListeners() {
        // TODO
        queryByViewButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateValidatorEntries();
                updateQueryByView();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });

        isReduce.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateValidatorEntries();
                updateIsReduce();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });

        group.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateValidatorEntries();
                updateGroup();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });

        addOptionsButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateValidatorEntries();
                updateAddOption();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });

        rowLimit.getTextControl().addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                boolean b = "0123456789".indexOf(e.text) >= 0; //$NON-NLS-1$
                e.doit = b;
                return;
            }
        });

        retrieveButton.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                retrieve();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });
    }

    @Override
    protected void updateValidatorEntries() {
        super.updateValidatorEntries();
        collectValidateEntry(new NonemptyValidator(rowLimit.getText()),
                Messages.getString("CouchDBRetrieveSchemaForm.updateValidatorEntries.rowLimit.shouldNotEmpty")); //$NON-NLS-1$
        collectValidateEntry(new NonemptyValidator(tableName.getText()),
                Messages.getString("CouchDBRetrieveSchemaForm.updateValidatorEntries.tableName.shouldNotEmpty")); //$NON-NLS-1$
    }

    @Override
    public void restoreCheckItems() {
        // TODO
    }

    protected Composite addSearchConditionGroup(Composite curParent) {

        Group searchGroup = Form.createGroup(curParent, 1, Messages.getString("CouchDBRetrieveSchemaForm.step1.groupName")); //$NON-NLS-1$

        ScrolledComposite scrollComposite = new ScrolledComposite(searchGroup, SWT.V_SCROLL);
        scrollComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrollComposite.setLayout(new FillLayout());

        Label line = new Label(searchGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        line.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        retrieveButton = new Button(searchGroup, SWT.NONE);
        retrieveButton.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.retrieve")); //$NON-NLS-1$
        retrieveButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        searchGroupContainer = new Composite(scrollComposite, SWT.NONE);
        searchGroupContainer.setLayout(new GridLayout(4, false));

        GridLayout gridLayout = null;

        queryByViewButton = new Button(searchGroupContainer, SWT.CHECK);
        queryByViewButton.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.queryByView")); //$NON-NLS-1$
        queryByViewButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false, 4, 1));

        designDocument = new LabelledText(searchGroupContainer,
                Messages.getString("CouchDBRetrieveSchemaForm.step1.queryByView.designDocument"), 1); //$NON-NLS-1$
        view = new LabelledText(searchGroupContainer, Messages.getString("CouchDBRetrieveSchemaForm.step1.queryByView.view"), 1); //$NON-NLS-1$

        startKey = new LabelledText(searchGroupContainer, Messages.getString("CouchDBRetrieveSchemaForm.step1.startKey"), 1); //$NON-NLS-1$

        endKey = new LabelledText(searchGroupContainer, Messages.getString("CouchDBRetrieveSchemaForm.step1.endKey"), 1); //$NON-NLS-1$

        startKeyDocId = new LabelledText(searchGroupContainer,
                Messages.getString("CouchDBRetrieveSchemaForm.step1.startKeyDocId"), 1); //$NON-NLS-1$

        endKeyDocId = new LabelledText(searchGroupContainer, Messages.getString("CouchDBRetrieveSchemaForm.step1.endKeyDocId"), 1); //$NON-NLS-1$

        Composite checkOptions = new Composite(searchGroupContainer, SWT.NONE);
        checkOptions.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        gridLayout = new GridLayout(6, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        checkOptions.setLayout(gridLayout);

        isReduce = new Button(checkOptions, SWT.CHECK);
        isReduce.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.isReduce")); //$NON-NLS-1$

        includeDocs = new Button(checkOptions, SWT.CHECK);
        includeDocs.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.includeDocs")); //$NON-NLS-1$
        includeDocs.setSelection(ICouchDBConstants.INCLUDE_DOCS_DEFAULT);

        descending = new Button(checkOptions, SWT.CHECK);
        descending.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.descending")); //$NON-NLS-1$

        group = new Button(checkOptions, SWT.CHECK);
        group.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.group")); //$NON-NLS-1$

        groupLevel = new LabelledText(checkOptions, Messages.getString("CouchDBRetrieveSchemaForm.step1.groupLevel"), 1); //$NON-NLS-1$

        Composite addOptionsComposite = new Composite(searchGroupContainer, SWT.NONE);
        addOptionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
        gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        addOptionsComposite.setLayout(gridLayout);

        addOptionsButton = new Button(addOptionsComposite, SWT.CHECK);
        addOptionsButton.setText(Messages.getString("CouchDBRetrieveSchemaForm.step1.addOptions")); //$NON-NLS-1$
        addOptionsButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));

        optionsTableComposite = new Composite(addOptionsComposite, SWT.NONE);
        optionsTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        optionsTableComposite.setLayout(gridLayout);

        optionListModel = new CouchDBJSONFieldModel(optionList, Messages.getString("CouchDBRetrieveSchemaForm.optionsList")); //$NON-NLS-1$
        CouchDBRetrieveOptionsTableView optionTableView = new CouchDBRetrieveOptionsTableView(optionListModel,
                optionsTableComposite);
        Composite fieldTableEditorComposite = optionTableView.getMainComposite();
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.heightHint = 180;
        fieldTableEditorComposite.setLayoutData(gridData);
        fieldTableEditorComposite.setBackground(null);

        rowLimit = new LabelledText(searchGroupContainer, Messages.getString("CouchDBRetrieveSchemaForm.step1.retrieveLimit"), 1); //$NON-NLS-1$
        rowLimit.setText(ICouchDBConstants.COUCHDB_RETRIEVE_LIMIT_DEFAULT.toString());

        scrollComposite.setContent(searchGroupContainer);
        scrollComposite.setExpandHorizontal(true);
        scrollComposite.setExpandVertical(true);
        scrollComposite.setMinSize(searchGroupContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        updateQueryByView();
        updateAddOption();
        return searchGroup;
    }

    protected Composite addStep2AndStep3(Composite parent) {
        SashForm step2AndStep3SashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
        step2AndStep3SashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        step2AndStep3SashForm.setLayout(new GridLayout(1, true));
        step2AndStep3SashForm.SASH_WIDTH = 2;
        step2AndStep3SashForm.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        addSearchResultGroup(step2AndStep3SashForm);
        AddSelectedResultGroup(step2AndStep3SashForm);

        step2AndStep3SashForm.setSashWidth(2);
        step2AndStep3SashForm.setWeights(new int[] { 50, 50 });

        return step2AndStep3SashForm;
    }

    protected Composite addSearchResultGroup(Composite parent) {
        Group searchResultGroup = Form.createGroup(parent, 1, Messages.getString("CouchDBRetrieveSchemaForm.step2")); //$NON-NLS-1$

        Composite managementArea = new Composite(searchResultGroup, SWT.NONE);
        managementArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        managementArea.setLayout(new GridLayout(3, false));
        tableName = new LabelledText(managementArea,
                Messages.getString("CouchDBRetrieveSchemaForm.step2.retrieveResult.tableName")); //$NON-NLS-1$
        addButton = new Button(managementArea, SWT.NONE);
        addButton.setText(Messages.getString("CouchDBRetrieveSchemaForm.step2.retrieveResult.add") + " >>"); //$NON-NLS-1$ //$NON-NLS-2$
        addButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        tableView = new MetadataEmfTableEditorView(searchResultGroup, SWT.NONE, false, false);
        tableView.setShowDbTypeColumn(false, false, false);
        tableView.setShowDbColumnName(true, false);
        // String trueDbmsID = metadataconnection.getMapping();
        // tableView.setCurrentDbms(trueDbmsID);
        tableView.initGraphicComponents();
        Composite metaComposite = tableView.getMainComposite();
        metaComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        tableEditor = new MetadataEmfTableEditor(Messages.getString("CouchDBRetrieveSchemaForm.step2.retrieveResult.tableDetail")); //$NON-NLS-1$
        tableView.setMetadataEditor(tableEditor);

        tableView.setReadOnly(true);
        return searchResultGroup;
    }

    protected Composite AddSelectedResultGroup(Composite parent) {
        Group selectedResultGroup = Form.createGroup(parent, 1, Messages.getString("CouchDBRetrieveSchemaForm.step3")); //$NON-NLS-1$

        Composite managementArea = new Composite(selectedResultGroup, SWT.NONE);
        managementArea.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        managementArea.setLayout(new GridLayout(3, false));

        selectAll = new Button(managementArea, SWT.NONE);
        selectAll.setText(Messages.getString("CouchDBRetrieveSchemaForm.selectedTable.selectAll")); //$NON-NLS-1$
        selectAll.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
        selectNone = new Button(managementArea, SWT.NONE);
        selectNone.setText(Messages.getString("CouchDBRetrieveSchemaForm.selectedTable.selectNone")); //$NON-NLS-1$
        selectNone.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        deleteSelected = new Button(managementArea, SWT.NONE);
        deleteSelected.setText(Messages.getString("CouchDBRetrieveSchemaForm.selectedTable.deleteSelected")); //$NON-NLS-1$
        deleteSelected.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        final Table selectedResultTable = new Table(selectedResultGroup, SWT.CHECK | SWT.MULTI | SWT.V_SCROLL
                | SWT.FULL_SELECTION | SWT.BORDER);
        selectedResultTable.setLinesVisible(true);
        selectedResultTable.setHeaderVisible(true);
        selectedResultTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        checkBoxTableViewer = new CheckboxTableViewer(selectedResultTable);

        TableViewerColumn nameColumnViewer = new TableViewerColumn(checkBoxTableViewer, SWT.LEFT);
        TableColumn nameColumn = nameColumnViewer.getColumn();
        nameColumn.setWidth(200);
        nameColumn.setText(Messages.getString("CouchDBRetrieveSchemaForm.selectedTable.nameColumn")); //$NON-NLS-1$
        TableViewerColumn numberColumnViewer = new TableViewerColumn(checkBoxTableViewer, SWT.LEFT);
        TableColumn columnNumColumn = numberColumnViewer.getColumn();
        columnNumColumn.setWidth(100);
        columnNumColumn.setText(Messages.getString("CouchDBRetrieveSchemaForm.selectedTable.columnNumColumn")); //$NON-NLS-1$

        checkBoxTableViewer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public void removeListener(ILabelProviderListener listener) {
                // nothing to do
            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                // nothing to do
                return false;
            }

            @Override
            public void dispose() {
                // nothing to do
            }

            @Override
            public void addListener(ILabelProviderListener listener) {
                // nothing to do
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                String tableColumnLabel = ""; //$NON-NLS-1$
                Map<String, Object> mapElement = (Map<String, Object>) element;
                switch (columnIndex) {
                case 0:
                    tableColumnLabel = "" + mapElement.get(TABLE_NAME); //$NON-NLS-1$
                    break;
                case 1:
                    tableColumnLabel = "" + mapElement.get(TABLE_COLUMNS_NUMBER); //$NON-NLS-1$
                    break;
                }
                return tableColumnLabel;
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }
        });
        checkBoxTableViewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                // TODO Auto-generated method stub

            }

            @Override
            public void dispose() {
                // TODO Auto-generated method stub

            }

            @Override
            public Object[] getElements(Object inputElement) {
                List<Map<String, Object>> inputList = (List<Map<String, Object>>) inputElement;
                return inputList.toArray();
            }
        });
        checkBoxTableViewer.setCheckStateProvider(new ICheckStateProvider() {

            @Override
            public boolean isGrayed(Object element) {
                return false;
            }

            @Override
            public boolean isChecked(Object element) {
                return false;
            }
        });
        createDummyDataForSelectedTableList();
        checkBoxTableViewer.setInput(selectedTableList);
        checkBoxTableViewer.setAllChecked(true);

        return selectedResultGroup;
    }

    public void createDummyDataForSelectedTableList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(TABLE_NAME, "a");
        map.put(TABLE_COLUMNS_NUMBER, 2);
        selectedTableList.add(map);
    }

    protected void updateQueryByView() {
        if (!isContextMode()) {
            boolean selection = queryByViewButton.getSelection();
            designDocument.setEditable(selection);
            view.setEditable(selection);
            isReduce.setEnabled(selection);
        } else {
            designDocument.setEditable(false);
            view.setEditable(false);
            isReduce.setEnabled(false);
        }
        updateIsReduce();
    }

    protected void updateIsReduce() {
        if (!isContextMode()) {
            boolean selection = isReduce.isEnabled() && isReduce.getSelection();
            includeDocs.setEnabled(!selection);
            group.setEnabled(selection);
        } else {
            includeDocs.setEnabled(false);
            group.setEnabled(false);
        }
        updateGroup();
    }

    protected void updateGroup() {
        if (!isContextMode()) {
            boolean selection = group.isEnabled() && group.getSelection();
            groupLevel.setEditable(selection);
        } else {
            groupLevel.setEditable(false);
        }
    }

    protected void updateAddOption() {
        boolean show = addOptionsButton.isEnabled() && addOptionsButton.getSelection();
        setHideWidgets(optionsTableComposite, !show);
        searchGroupContainer.layout();
    }

    protected void addOrUpdateTable() {
        if (table == null) {
            existingNames = NoSQLSchemaUtil.getAllTableLabels(getConnection());
            table = ConnectionFactory.eINSTANCE.createMetadataTable();
            table.setId(ProxyRepositoryFactory.getInstance().getNextId());
            String tableLabel = IndiceHelper.getIndexedLabel(table.getLabel(), existingNames);
            table.setLabel(tableLabel);
            table.setName(tableLabel);
            NoSQLSchemaUtil.addTable2Connection(getConnection(), table, ICouchDBConstants.COUCHDB_SCHEMA);
            hitTablesMap.put(tableLabel, table);
        }
        // table.getAdditionalProperties().put(INeo4jConstants.CYPHER, colorText.getText());
        EMap<String, String> additionalProperties = table.getAdditionalProperties();
        Boolean bQueryByView = queryByViewButton.getSelection();
        additionalProperties.put(ICouchDBAttributes.QUERY_BY_VIEW, bQueryByView.toString());
        String sDesignDocument = ""; //$NON-NLS-1$
        String sView = ""; //$NON-NLS-1$
        if (bQueryByView) {
            sDesignDocument = designDocument.getText();
            sView = view.getText();
        }
        additionalProperties.put(ICouchDBAttributes.DOC, sDesignDocument);
        additionalProperties.put(ICouchDBAttributes.VIEW, sView);
        additionalProperties.put(ICouchDBAttributes.STARTKEY, startKey.getText());
        additionalProperties.put(ICouchDBAttributes.ENDKEY, endKey.getText());
        additionalProperties.put(ICouchDBAttributes.STARTKEY_DOCID, startKeyDocId.getText());
        additionalProperties.put(ICouchDBAttributes.ENDKEY_DOCID, endKeyDocId.getText());

        Boolean bIsReduce = isReduce.isEnabled() && isReduce.getSelection();
        additionalProperties.put(ICouchDBAttributes.ISREDUCE, bIsReduce.toString());

        Boolean bIncludeDocs = includeDocs.isEnabled() && includeDocs.getSelection();
        additionalProperties.put(ICouchDBAttributes.INCLUDE_DOCS, bIncludeDocs.toString());

        Boolean bDescending = descending.getSelection();
        additionalProperties.put(ICouchDBAttributes.DESCENDING, bDescending.toString());

        Boolean bGroup = group.isEnabled() && group.getSelection();

        String sGroupLevel = ""; //$NON-NLS-1$
        if (bGroup) {
            sGroupLevel = groupLevel.getText();
        }
        additionalProperties.put(ICouchDBAttributes.GROUP_LEVEL, sGroupLevel);

        Boolean bAddOption = false;
        additionalProperties.put(ICouchDBAttributes.ADD_OPTION, bAddOption.toString());
        if (bAddOption) {
            try {
                additionalProperties.put(ICouchDBAttributes.OPTIONS, optionListModel.getBeanListString());
            } catch (JSONException jsonException) {
                // nothing to do
            }
        }
    }

    protected void retrieve() {
        addOrUpdateTable();
    }

    protected void retrieveSchema() throws NoSQLServerException {
        CouchDBConnector couchDBConnector = new CouchDBConnector(getConnection());
        CouchDbClient couchDbClient = couchDBConnector.getCouchDbClient();

        couchDBConnector.closeConnection();
    }
}
