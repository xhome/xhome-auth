/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 用户管理面板
 */

Ext.define('XHome.XAuth.Manage.User', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }

        // 输入标签
        var fieldLabelWidth = 40;
        // 用户名输入框
        Ext.define('XHome.XAuth.Manage.User.NameField', {
            extend: 'Ext.form.field.Text', 
            fieldLabel: '用户名',
            labelWidth: fieldLabelWidth, 
            emptyText: '请输入用户名',
            allowBlank: false,
            blankText: '用户名不能为空',
            minLength: 4,
            minLengthText: '用户名不能少于4个字符',
            maxLength: 20,
            maxLengthText: '用户名不能超过20个字符',
            regex: /^[\w-_]+$/,
            regexText: '用户名只能包含字母、数字和-_',
        });

        // 用户昵称输入框
        Ext.define('XHome.XAuth.Manage.User.NickField', {
            extend: 'Ext.form.field.Text', 
            fieldLabel: '昵称',
            labelWidth: fieldLabelWidth, 
            name: 'user.name',
            name: 'user.nick',
            itemId: 'user.nick',
            emptyText: '请输入用户昵称',
            allowBlank: false,
            blankText: '用户昵称不能为空',
            maxLength: 20,
            maxLengthText: '用户昵称不能超过20个字符',
        });

        // 用户邮箱输入框
        Ext.define('XHome.XAuth.Manage.User.EmailField', {
            extend: 'Ext.form.field.Text', 
            fieldLabel: '邮箱',
            labelWidth: fieldLabelWidth, 
            name: 'user.email',
            itemId: 'user.email',
            emptyText: '请输入用户邮箱',
            allowBlank: false,
            blankText: '用户邮箱不能为空',
            maxLength: 50,
            maxLengthText: '用户邮箱不能超过50个字符',
            vtype: 'email',
            vtypeText: '用户邮箱格式不正确',
        });

        // 用户密码输入框
        Ext.define('XHome.XAuth.Manage.User.PasswordField', {
            extend: 'Ext.form.field.Text', 
            fieldLabel: '密码',
            labelWidth: fieldLabelWidth, 
            name: 'user.password',
            itemId: 'user.password',
            inputType: 'password',  
            minLength: 4,
            minLengthText: '用户密码不能少于4个字符',
            maxLength: 20,
            maxLengthText: '用户密码不能超过20个字符',
            regex: /^[\w-_]+$/,
            regexText: '用户密码只能包含字母、数字和-_',  
        });

        // 角色下拉选择框（搜索）
        Ext.define('XHome.XAuth.Manage.User.RoleComboBox', {
            extend: 'Ext.form.field.ComboBox',
            fieldLabel: '角色',
            labelWidth: fieldLabelWidth,
            store: Ext.create('XHome.data.JsonStore', {
                wrapperBeforeload: false,
                wrapperLoad: false, 
                url: 'xauth/role/query.json',
                fields: ['id', 'name'],
            }),
            displayField: 'name',
            pageSize: 20,
        });

        // 角色下拉选择框（弹出窗口）
        Ext.define('XHome.XAuth.Manage.User.RolesField', {
            extend: 'XHome.XAuth.Manage.User.RoleComboBox',
            valueField: 'id',
            editable: false,
            multiSelect: true,
            labelWidth: fieldLabelWidth,
            emptyText: '请选择用户角色',
            allowBlank: false,
            blankText: '用户角色不能为空',
            listeners: {
                select: function(combo, records, eOpts) {
                    var role, comp,
                        form = combo.findParentByType('form'),
                        values = form.getValues();
                    // 删除已经添加的角色
                    for (field in values) {
                        if (field.indexOf('user.roles') == 0) {
                            comp = form.getComponent(field); 
                            form.remove(comp, true); 
                        }
                    }
                    
                    // 添加角色
                    for (var i = 0; i < records.length; i++) {
                        role = records[i].getData();
                        form.add({
                            xtype: 'hidden',
                            name: 'user.roles[' + i + '].id',
                            itemId: 'user.roles[' + i + '].id',
                            value: role.id,
                        });
                        form.add({
                            xtype: 'hidden',
                            name: 'user.roles[' + i + '].name',
                            itemId: 'user.roles[' + i + '].name',
                            value: role.name,
                        });
                    }
                }, 
            }, 
        });

        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["name"]',
                fieldLabel: '用户名',
                maxLength: 20,
                maxLengthText: '用户名不能超过20个字符',
                regex: /^[\w-_]+$/,
                regexText: '用户名只能包含字母、数字和-_',
            }, {
                name: 'parameters["email"]',
                fieldLabel: '邮箱',
                maxLength: 50,
                maxLengthText: '用户邮箱不能超过50个字符',
                vtype: 'email',
                vtypeText: '用户邮箱格式不正确',
                margin: '0 0 0 10',
            }, Ext.create('XHome.XAuth.Manage.User.RoleComboBox', {
                name: 'parameters["role_name"]',
                valueField: 'name',
                anchor: '100%', 
                width: 300,
                labelWidth: 30,
                margin: '0 0 0 10',
            }),
            ],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 10,
            }, {
                text: '用户名',
                dataIndex: 'name',
                width: 50,
            }, {
                text: '昵称',
                dataIndex: 'nick',
                width: 50,
            }, {
                text: '邮箱',
                dataIndex: 'email',
                width: 60,
            }, {
                text: '认证方式',
                dataIndex: 'method',
                width: 30,
            }, {
                text: '角色',
                dataIndex: 'roles',
                renderer: function(value) {
                    // 讲用户所有角色拼接在一起显示
                    var role, roles = [];
                    for (var i = 0; i < value.length; i++) {
                        role = value[i];
                        // 删除不需要的属性，避免注入出错;
                        delete role.created;
                        delete role.modified;
                        roles.push(role.name);
                    }
                    return roles.join(', ');
                },
            }, {
                text: '创建时间',
                dataIndex: 'createdStr',
                hidden: true,
            }, {
                text: '修改时间',
                dataIndex: 'modifiedStr',
                hidden: true,
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'name', 'nick', 'email', 'method', 'roles',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/user/query.json',
            }),

            /**
             * 添加用户
             */
            addUser: function() {
                var store = grid.getStore();
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '添加用户',
                    height: 230,
                    width: 330,
                    url: 'xauth/user/add.json',
                    success: function(result) {
                        store.add(result.data);
                    },
                    items: [
                        Ext.create('XHome.XAuth.Manage.User.NameField', {
                            name: 'user.name',
                            itemId: 'user.name',
                        }),
                        Ext.create('XHome.XAuth.Manage.User.NickField'),
                        Ext.create('XHome.XAuth.Manage.User.EmailField'),
                        Ext.create('XHome.XAuth.Manage.User.PasswordField', {
                            emptyText: '请输入用户密码',
                            allowBlank: false,
                            blankText: '用户密码不能为空',
                        }),
                        Ext.create('XHome.XAuth.Manage.User.RolesField'),
                    ],
                }).show();
            },

            /**
             * 修改用户
             */
            editUser: function() {
                var selection = grid.getSelectionModel().getSelection()[0],
                    user = selection.getData(),
                    roleIds = [], roles = user.roles,
                    formUser = XHome.utils.formEncode(user, 'user'),
                    rolesField = Ext.create('XHome.XAuth.Manage.User.RolesField', {
                    });
                // 自动选择已有的用户角色
                for (var i = 0; i < roles.length; i++) {
                    roleIds.push(roles[i].id);
                }
                rolesField.select(roleIds);
                Ext.create('XHome.Dashboard.FormWindow', {
                    title: '修改用户',
                    height: 230,
                    width: 330,
                    url: 'xauth/user/update.json',
                    success: function(result) {
                        selection.data = result.data;
                        grid.getView().refresh();
                        XHome.Msg.info(result.message);
                    },
                    hiddenParams: formUser,
                    items: [
                        Ext.create('XHome.XAuth.Manage.User.NameField', {
                            value: user.name,
                            disabled: true,
                        }),
                        Ext.create('XHome.XAuth.Manage.User.NickField', {
                            value: user.nick,
                        }),
                        Ext.create('XHome.XAuth.Manage.User.EmailField', {
                            value: user.email,
                        }),
                        Ext.create('XHome.XAuth.Manage.User.PasswordField', {
                            allowBlank: true, 
                        }),
                        rolesField,
                    ],
                }).show();
            },

            /**
             * 删除用户
             */
            deleteUser: function() {
                var store = grid.getStore(),
                    selections = grid.getSelectionModel().getSelection(),
                    record = undefined,
                    data = undefined,
                    users = [],
                    userNames = [];
                if (selections.length == 0) {
                    return;
                }
                for (var i = 0; i < selections.length; i++) {
                    record = selections[i];
                    data = record.getData();
                    users.push(data);
                    userNames.push(record.getData().name);
                }
                var msg = '<font color="red">' + userNames.join(', ') + '</font>';
                XHome.utils.request({
                    confirmMsg: '确认删除用户: ' + msg + ' ?',
                    progressMsg: '正在删除用户: ' + msg + '......',
                    url: 'xauth/user/remove.json',
                    params: XHome.utils.formEncode(users, 'users'),
                    success: function(result) {
                        store.remove(selections);
                    }
                });
            },
        });

        // 右键菜单
        var rightMenu = Ext.create('Ext.menu.Menu', {
            items: [{
                text: '添加用户',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addUser();
                },
            }, {
                text: '修改用户',
                iconAlign: 'left',
                iconCls: 'icon_edit',
                handler: function(button, e) {
                    grid.editUser();
                },
            }, {
                text: '删除用户',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteUser();
                },
            }],
        });

        // 表格工具条
        grid.addDocked({
            xtype: 'toolbar',
            dock: 'top',
            items: ['-', {
                xtype: 'button',
                text: '添加用户',
                iconAlign: 'left',
                iconCls: 'icon_add',
                handler: function(button, e) {
                    grid.addUser();
                },
            }, '-', {
                xtype: 'button',
                text: '删除用户',
                iconAlign: 'left',
                iconCls: 'icon_delete',
                handler: function(button, e) {
                    grid.deleteUser();
                },
            }]
        });

        XHome.utils.bindGridClick(grid, rightMenu, grid.editUser);

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
