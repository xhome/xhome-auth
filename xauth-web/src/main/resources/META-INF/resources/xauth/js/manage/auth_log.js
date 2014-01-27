/**
 * Author:   jhat
 * Date:     2014-01-27
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 认证日志管理
 */

Ext.define('XHome.XAuth.Manage.AuthLog', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }

        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["user"]',
                fieldLabel: '用户',
                labelWidth: 30,
                maxLength: 20,
                maxLengthText: '用户名不能超过20个字符',
                regex: /^[\w-_]+$/,
                regexText: '用户名格式错误',
            }, {
                name: 'parameters["method"]',
                fieldLabel: '认证方式', 
                labelWidth: 55,
                maxLength: 10,
                maxLengthText: '认证方式不能超过10个字符',
                margin: '0 0 0 10',
                disabled: true,
                hidden: true,
            }, {
                name: 'parameters["address"]',
                fieldLabel: '登录地址',
                labelWidth: 55,
                maxLength: 16,
                maxLengthText: '登录地址不能超过16个字符',
                margin: '0 0 0 10',
            }, {
                name: 'parameters["created"]',
                fieldLabel: '认证时间',
                labelWidth: 55,
                maxLength: 20,
                maxLengthText: '认证时间不能超过20个字符',
                margin: '0 0 0 10',
            }],
        });

        // 数据显示表格
        var grid = Ext.create('XHome.Dashboard.EditorGridPanel', {
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 8,
            }, {
                text: '用户',
                dataIndex: 'user',
                width: 30,
                renderer: function(value) {
                    return value.name; 
                },
            }, {
                text: '认证方式',
                dataIndex: 'method',
                width: 25,
            }, {
                text: '登录地址',
                dataIndex: 'address',
                width: 35,
            }, {
                text: '认证时间',
                dataIndex: 'createdStr',
                width: 30,
            }, {
                text: '设备类型',
                dataIndex: 'agent',
                width: 15,
            }, {
                text: '访问设备',
                dataIndex: 'number',
                renderer: function(value, meta, record) {
                    meta.tdAttr = 'data-qtip="' + value + '"';
                    return value;
                },
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'user', 'method', 'address', 'agent', 'number',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/auth_log/query.json',
            }),
        });

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
