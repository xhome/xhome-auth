/**
 * Author:   jhat
 * Date:     2014-01-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 系统配置管理
 */

Ext.define('XHome.XAuth.System.Config', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }
        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["display"]',
                fieldLabel: '配置项',
                labelWidth: 40,
                maxLength: 50,
                regex: /^\w*$/,
            }, {
                name: 'parameters["category"]',
                hidden: true,
                value: 0,
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
            }, {
                text: '配置项',
                dataIndex: 'display',
            }, {
                text: '配置值',
                dataIndex: 'value',
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'category', 'item', 'display', 'value',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/config/query.json',
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

        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '修改配置项',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editConfig();
                },
            }],
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editConfig);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
