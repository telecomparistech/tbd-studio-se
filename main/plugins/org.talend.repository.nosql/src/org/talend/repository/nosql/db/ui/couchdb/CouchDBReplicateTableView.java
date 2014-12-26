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

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.CheckColumnSelectionListener;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.ui.swt.tableviewer.tableeditor.CheckboxTableEditorContent;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBConstants;
import org.talend.repository.nosql.db.model.couchdb.CouchDBReplicateFieldModel;
import org.talend.repository.nosql.i18n.Messages;

/**
 * created by cmeng on Dec 25, 2014 Detailled comment
 *
 */
public class CouchDBReplicateTableView extends AbstractDataTableEditorView<Map<String, Object>> {

    public CouchDBReplicateTableView(CouchDBReplicateFieldModel model, Composite parent) {
        super(parent, SWT.NONE, model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView#createColumns(org.talend.commons.ui
     * .swt.tableviewer.TableViewerCreator, org.eclipse.swt.widgets.Table)
     */
    @Override
    protected void createColumns(TableViewerCreator<Map<String, Object>> tableViewerCreator, Table table) {
        createTargetDBNameColumn(tableViewerCreator);
        createContinuousColumn(tableViewerCreator);
        createCreateTargetDBColumn(tableViewerCreator);
        createIsCanceledColumn(tableViewerCreator);
    }

    @Override
    protected ExtendedToolbarView initToolBar() {
        return new ExtendedToolbarView(getMainComposite(), SWT.NONE, getExtendedTableViewer()) {

            @Override
            protected AddPushButtonForExtendedTable createAddPushButton() {
                return new AddPushButtonForExtendedTable(this.toolbar, getExtendedTableViewer()) {

                    @Override
                    protected Object getObjectToAdd() {
                        Map<String, Object> hpt = getModel().createReplicateType();
                        hpt.put(ICouchDBAttributes.TARGET_DB_NAME, ICouchDBConstants.REPLICATE_TARGET_DB_NAME_DEFAULT);
                        hpt.put(ICouchDBAttributes.CONTINUOUS, ICouchDBConstants.REPLICATE_CONTINUOUS_DEFAULT);
                        hpt.put(ICouchDBAttributes.CREATE_TARGET, ICouchDBConstants.REPLICATE_CREATE_TARGET_DEFAULT);
                        hpt.put(ICouchDBAttributes.CANCEL_REPLICATE, ICouchDBConstants.REPLICATE_CANCEL_REPLICATE_DEFAULT);
                        return hpt;
                    }

                };
            }

            @Override
            protected PastePushButton createPastePushButton() {
                return new PastePushButtonForExtendedTable(toolbar, extendedTableViewer) {

                    @Override
                    protected Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert) {
                        return new ExtendedTablePasteCommand(extendedTableModel, indexWhereInsert) {

                            @Override
                            public List<Map<String, Object>> createPastableBeansList(ExtendedTableModel extendedTableModel,
                                    List copiedObjectsList) {
                                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                CouchDBReplicateFieldModel fieldsModel = (CouchDBReplicateFieldModel) extendedTableModel;
                                for (Object current : copiedObjectsList) {
                                    if (current instanceof HashMap) {
                                        Map<String, Object> original = (Map<String, Object>) current;
                                        Map<String, Object> copy = fieldsModel.createReplicateType();
                                        copy.putAll(original);
                                        list.add(copy);
                                    }
                                }
                                return list;
                            }
                        };
                    }

                };
            }
        };
    }

    public CouchDBReplicateFieldModel getModel() {
        return (CouchDBReplicateFieldModel) getExtendedTableModel();
    }

    protected TableViewerCreatorColumn<Map<String, Object>, String> createTargetDBNameColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, String> column = new TableViewerCreatorColumn<Map<String, Object>, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBConnForm.replicateView.targetDBName")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, String>() {

            @Override
            public String get(Map<String, Object> bean) {
                Object obj = bean.get(ICouchDBAttributes.TARGET_DB_NAME);
                if (obj != null) {
                    return (String) bean.get(ICouchDBAttributes.TARGET_DB_NAME);
                } else {
                    return ""; //$NON-NLS-1$
                }
            }

            @Override
            public void set(Map<String, Object> bean, String value) {
                bean.put(ICouchDBAttributes.TARGET_DB_NAME, value);
            }

        });
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(ICouchDBConstants.REPLICATE_TARGET_DB_NAME_DEFAULT);
        return column;
    }

    protected TableViewerCreatorColumn<Map<String, Object>, Boolean> createContinuousColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, Boolean> column = new TableViewerCreatorColumn<Map<String, Object>, Boolean>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBConnForm.replicateView.continuous")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, Boolean>() {

            @Override
            public Boolean get(Map<String, Object> bean) {
                return Boolean.valueOf("" + bean.get(ICouchDBAttributes.CONTINUOUS)); //$NON-NLS-1$
            }

            @Override
            public void set(Map<String, Object> bean, Boolean value) {
                bean.put(ICouchDBAttributes.CONTINUOUS, value);
            }

        });
        CheckColumnSelectionListener tableColumnSelectionListener = new CheckColumnSelectionListener(column, tableViewerCreator);
        column.setTableColumnSelectionListener(tableColumnSelectionListener);
        column.setTableEditorContent(new CheckboxTableEditorContent());
        boolean defaultValue = ICouchDBConstants.REPLICATE_CREATE_TARGET_DEFAULT.booleanValue();
        tableColumnSelectionListener.setChecked(defaultValue);
        if (defaultValue) {
            column.setImageHeader(ImageProvider.getImage(EImage.CHECKED_ICON));
        } else {
            column.setImageHeader(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        }
        column.setDisplayedValue(""); //$NON-NLS-1$

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        return column;
    }

    protected TableViewerCreatorColumn<Map<String, Object>, Boolean> createCreateTargetDBColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, Boolean> column = new TableViewerCreatorColumn<Map<String, Object>, Boolean>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBConnForm.replicateView.createTargetDB")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, Boolean>() {

            @Override
            public Boolean get(Map<String, Object> bean) {
                return Boolean.valueOf("" + bean.get(ICouchDBAttributes.CREATE_TARGET)); //$NON-NLS-1$
            }

            @Override
            public void set(Map<String, Object> bean, Boolean value) {
                bean.put(ICouchDBAttributes.CREATE_TARGET, value);
            }

        });
        CheckColumnSelectionListener tableColumnSelectionListener = new CheckColumnSelectionListener(column, tableViewerCreator);
        column.setTableColumnSelectionListener(tableColumnSelectionListener);
        column.setTableEditorContent(new CheckboxTableEditorContent());
        boolean defaultValue = ICouchDBConstants.REPLICATE_CREATE_TARGET_DEFAULT.booleanValue();
        tableColumnSelectionListener.setChecked(defaultValue);
        if (defaultValue) {
            column.setImageHeader(ImageProvider.getImage(EImage.CHECKED_ICON));
        } else {
            column.setImageHeader(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        }
        column.setDisplayedValue(""); //$NON-NLS-1$

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        return column;
    }

    protected TableViewerCreatorColumn<Map<String, Object>, Boolean> createIsCanceledColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, Boolean> column = new TableViewerCreatorColumn<Map<String, Object>, Boolean>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBConnForm.replicateView.isCanceled")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, Boolean>() {

            @Override
            public Boolean get(Map<String, Object> bean) {
                return Boolean.valueOf("" + bean.get(ICouchDBAttributes.CANCEL_REPLICATE)); //$NON-NLS-1$
            }

            @Override
            public void set(Map<String, Object> bean, Boolean value) {
                bean.put(ICouchDBAttributes.CANCEL_REPLICATE, value);
            }

        });
        CheckColumnSelectionListener tableColumnSelectionListener = new CheckColumnSelectionListener(column, tableViewerCreator);
        column.setTableColumnSelectionListener(tableColumnSelectionListener);
        column.setTableEditorContent(new CheckboxTableEditorContent());
        boolean defaultValue = ICouchDBConstants.REPLICATE_CREATE_TARGET_DEFAULT.booleanValue();
        tableColumnSelectionListener.setChecked(defaultValue);
        if (defaultValue) {
            column.setImageHeader(ImageProvider.getImage(EImage.CHECKED_ICON));
        } else {
            column.setImageHeader(ImageProvider.getImage(EImage.UNCHECKED_ICON));
        }
        column.setDisplayedValue(""); //$NON-NLS-1$

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        return column;
    }
}
