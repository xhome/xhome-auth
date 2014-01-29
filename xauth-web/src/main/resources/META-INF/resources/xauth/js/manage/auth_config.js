/**
 * Author:   jhat
 * Date:     2014-01-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 系统配置管理
 */

Ext.define('XHome.XAuth.Manage.AuthConfig', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            autoSelModel: false,
            autoPagingBar: false,
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
                hidden: true,
            }, {
                text: '配置项',
                dataIndex: 'item',
                hidden: true,
            }, {
                text: '配置项',
                dataIndex: 'display',
            }, {
                text: '配置值',
                dataIndex: 'value',
                renderer: function(value, meta, record) {
                    var config = record.getData();
                    if (config.item == 'allow_auth_log'
                        || config.item == 'allow_manage_log') {
                        return {0: '关闭', 1: '开启'}[value]; 
                    }
                    return value; 
                },
                editor: {
                    allowBlank: false,
                }, 
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'category', 'item', 'display', 'value',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/config/query.json?parameters["category"]=1',
            }),

            /**
             * 修改配置项
             */
            editConfig: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    config = selection.getData(),
                    formConfig = XHome.utils.formEncode(config, 'config');
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改配置项',
                    height: 150,
                    width: 300,
                    url: 'xauth/config/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formConfig,
                    items: [{
                        fieldLabel: '配置项',
                        value: config.display,
                        disabled: true,
                    }, {
                        fieldLabel: '配置值',
                        name: 'config.value',
                        itemId: 'config.value',
                        emptyText: '请输入配置值',
                        allowBlank: false,
                        blankText: '配置值不能为空',
                        maxLength: 1000,
                        maxLengthText: '配置值不能超过1000个字符',
                        value: config.value,
                    }],
                }).show();
            },
            selType: 'cellmodel',
            plugins: [new Ext.grid.plugin.CellEditing({
                clicksToEdit: 1,
            })],
            /**
            listeners: {
                beforecellclick: function(gridView, td, cellIndex, record, tr, rowIndex, e, eOpts) {
                    return true;
                },
            },
            */
        });

        config.items = [grid];
        this.callParent([config]);
    },
});
