/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 
 */

Ext.define('XHome.XAuth.Manage.Role', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }
        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["name"]',
                fieldLabel: '名称',
                maxLength: 50,
                regex: /^\w*$/,
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
            }, {
                text: '名称',
                dataIndex: 'name',
            }, {
                text: '创建时间',
                dataIndex: 'createdStr',
            }, {
                text: '修改时间',
                dataIndex: 'modifiedStr',
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'name', 'createdStr', 'modifiedStr', 'owner', 'modifier', 'version', 'status'],
                url: 'xauth/role/query.json',
            }),

            /**
             * 添加角色
             */
            addRole: function() {
                var store = grid.getStore();
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '添加角色',
                    height: 130,
                    width: 300,
                    url: 'xauth/role/add.json',
                    success: function(result) {
                        store.add(result.data);
                    },
                    items: [{
                        fieldLabel: '名称',
                        name: 'role.name',
                        itemId: 'role.name',
                        emptyText: '请输入角色名称',
                        allowBlank: false,
                        blankText: '角色名称不能为空',
                        minLength: 4,
                        minLengthText: '角色名称不能少于4个字符',
                        maxLength: 20,
                        maxLengthText: '角色名称不能超过20个字符',
                        regex: /^[\w-_]+$/,
                        regexText: '角色名称只能包含字母、数字或-_',
                    }],
                }).show();
            },

            /**
             * 修改角色
             */
            editRole: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    role = selection.getData(),
                    formRole = XHome.utils.formEncode(role, 'role');
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改角色',
                    height: 130,
                    width: 300,
                    url: 'xauth/role/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formRole,
                    items: [{
                        fieldLabel: '名称',
                        name: 'role.name',
                        itemId: 'role.name',
                        emptyText: '请输入角色名称',
                        allowBlank: false,
                        blankText: '角色名称不能为空',
                        minLength: 4,
                        minLengthText: '角色名称不能少于4个字符',
                        maxLength: 20,
                        maxLengthText: '角色名称不能超过20个字符',
                        regex: /^[\w-_]+$/,
                        regexText: '角色名称只能包含字母、数字或-_',
                        value: role.name,
                    }],
                }).show();
            },

            /**
             * 删除角色
             */
            deleteRole: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    roles = [],
                    roleNames = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    roles.push(data);
                    roleNames.push(record.getData().name);
                }
                var msg = '<font color="red">' + roleNames.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除角色: ' + msg + ' ?',
                    progressMsg: '正在删除角色: ' + msg + '......',
                    url: 'xauth/role/remove.json',
                    params: XHome.utils.formEncode(roles, 'roles'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '添加角色',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addRole();
                },
            }, {
                text: '修改角色',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editRole();
                },
            }, {
                text: '删除角色',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteRole();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '添加角色',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addRole();
                },
            }, '-', {
                xtype: 'button',
                text: '删除角色',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteRole();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editRole);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
