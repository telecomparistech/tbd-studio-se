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
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.advanced.dataeditor.ExtendedToolbarView;
import org.talend.commons.ui.swt.advanced.dataeditor.button.AddPushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButton;
import org.talend.commons.ui.swt.advanced.dataeditor.button.PastePushButtonForExtendedTable;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTablePasteCommand;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBAttributes;
import org.talend.repository.nosql.db.common.couchdb.ICouchDBConstants;
import org.talend.repository.nosql.db.model.couchdb.CouchDBJSONFieldModel;
import org.talend.repository.nosql.i18n.Messages;

/**
 * created by cmeng on Dec 29, 2014 Detailled comment
 *
 */
public class CouchDBRetrieveOptionsTableView extends AbstractDataTableEditorView<Map<String, Object>> {

    public CouchDBRetrieveOptionsTableView(CouchDBJSONFieldModel model, Composite parent) {
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
        createParameterColumn(tableViewerCreator);
        createValueColumn(tableViewerCreator);
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
                        hpt.put(ICouchDBAttributes.PARAMETER, ICouchDBConstants.RETRIEVE_SCHEMA_OPTIONS_PARAMETER_DEFAULT);
                        hpt.put(ICouchDBAttributes.VALUE, ICouchDBConstants.RETRIEVE_SCHEMA_OPTIONS_VALUE_DEFAULT);
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
                                CouchDBJSONFieldModel fieldsModel = (CouchDBJSONFieldModel) extendedTableModel;
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

    public CouchDBJSONFieldModel getModel() {
        return (CouchDBJSONFieldModel) getExtendedTableModel();
    }

    protected TableViewerCreatorColumn<Map<String, Object>, String> createParameterColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, String> column = new TableViewerCreatorColumn<Map<String, Object>, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBRetrieveSchemaForm.optionTable.parameter")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, String>() {

            @Override
            public String get(Map<String, Object> bean) {
                Object obj = bean.get(ICouchDBAttributes.PARAMETER);
                if (obj != null) {
                    return (String) obj;
                } else {
                    return ""; //$NON-NLS-1$
                }
            }

            @Override
            public void set(Map<String, Object> bean, String value) {
                bean.put(ICouchDBAttributes.PARAMETER, value);
            }

        });
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(ICouchDBConstants.RETRIEVE_SCHEMA_OPTIONS_PARAMETER_DEFAULT);
        return column;
    }

    protected TableViewerCreatorColumn<Map<String, Object>, String> createValueColumn(
            TableViewerCreator<Map<String, Object>> tableViewerCreator) {
        TableViewerCreatorColumn<Map<String, Object>, String> column = new TableViewerCreatorColumn<Map<String, Object>, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("CouchDBRetrieveSchemaForm.optionTable.value")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<Map<String, Object>, String>() {

            @Override
            public String get(Map<String, Object> bean) {
                Object obj = bean.get(ICouchDBAttributes.VALUE);
                if (obj != null) {
                    return (String) obj;
                } else {
                    return ""; //$NON-NLS-1$
                }
            }

            @Override
            public void set(Map<String, Object> bean, String value) {
                bean.put(ICouchDBAttributes.VALUE, value);
            }

        });
        column.setCellEditor(new TextCellEditor(tableViewerCreator.getTable()));

        column.setModifiable(true);
        column.setWeight(25);
        column.setMinimumWidth(50);
        column.setDefaultInternalValue(ICouchDBConstants.RETRIEVE_SCHEMA_OPTIONS_VALUE_DEFAULT);
        return column;
    }
}
