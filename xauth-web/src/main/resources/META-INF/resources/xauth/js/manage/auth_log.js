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
                fieldLabel: '认证地址',
                labelWidth: 55,
                maxLength: 16,
                maxLengthText: '认证地址不能超过16个字符',
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
            autoSelModel: false,
            columns: [{
                text: '编号',
                dataIndex: 'id',
                width: 8,
                hidden: true,
            }, {
                text: '用户',
                dataIndex: 'user',
                width: 20,
                renderer: function(value) {
                    return value.name; 
                },
            }, {
                text: '认证方式',
                dataIndex: 'method',
                width: 25,
                hidden: true,
            }, {
                text: '认证地址',
                dataIndex: 'address',
                width: 20,
            }, {
                text: '认证时间',
                dataIndex: 'createdStr',
                width: 25,
            }, {
                text: '认证结果',
                dataIndex: 'status',
                width: 20,
                renderer: function(value) {
                    return {1: '认证成功', 5: '用户被锁定', 7: '用户不存在',
                        9: '密码错误', 10: '验证码错误', 11: '禁止认证'}[value]; 
                },
            }, {
                text: '设备类型',
                dataIndex: 'agent',
                width: 20,
                renderer: function(value) {
                    return {0: 'OTHER', 1: 'IE', 2: 'CHROME', 3: 'FIREFOX',
                        4: 'OPERA', 5: 'SAFARI', 6: 'BAIDU', 7: 'QIHU',
                        8: 'SOGOU', 9: 'MAXTHON', 10: 'TT', 11: 'UC',
                        12: 'DORPHIN', 50: 'ANDROID', 51: 'IOS',
                        52: 'WINPHONE', 53: 'SYMBIAN'}[value]; 
                },
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
