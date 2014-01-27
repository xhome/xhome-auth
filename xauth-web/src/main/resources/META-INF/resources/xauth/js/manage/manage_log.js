/**
 * Author:   jhat
 * Date:     2014-01-27
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 认证日志管理
 */

Ext.define('XHome.XAuth.Manage.ManageLog', {
    extend: 'XHome.Dashboard.WorkPanel',
    constructor: function(config) {
        if (!config) {
            config = {};
        }

        // 搜索面板
        var spanel = Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                name: 'parameters["action"]',
                fieldLabel: '动作',
                labelWidth: 30,
            }, {
                name: 'parameters["type"]',
                fieldLabel: '类型', 
                labelWidth: 30,
                margin: '0 0 0 10',
            }, {
                name: 'parameters["created"]',
                fieldLabel: '时间',
                labelWidth: 30,
                maxLength: 20,
                maxLengthText: '时间不能超过20个字符',
                margin: '0 0 0 10',
            }, {
                name: 'parameters["content"]',
                fieldLabel: '描述',
                labelWidth: 30,
                maxLength: 16,
                maxLengthText: '描述不能超过16个字符',
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
                text: '分类',
                dataIndex: 'category',
                width: 10,
                hidden: true,
                renderer: function(value) {
                    return {1: 'XAuth', 2: 'XBlog', 3: 'SMS',
                        4: 'Statistics'}[value];
                },
            }, {
                text: '动作',
                dataIndex: 'action',
                width: 15,
                renderer: function(value) {
                    return {0: '其它', 1: '添加', 2: '修改', 3: '锁定',
                        4: '解锁', 5: '移除', 6: '删除', 7: '是否存在',
                        8: '是否可更新', 9: '是否锁定', 10: '是否可移除',
                        11: '是否可删除', 12: '获取', 13: '查询',
                        14: '统计'}[value]; 
                },
            }, {
                text: '类型',
                dataIndex: 'type',
                width: 15,
                renderer: function(value) {
                    return {0: '配置项', 1: '角色', 2: '用户', 3: '用户角色',
                        4: '认证日志', 5: '管理日志'}[value];
                },
            }, {
                text: '对象',
                dataIndex: 'obj',
                width: 10,
                hidden: true,
            }, {
                text: '时间',
                dataIndex: 'createdStr',
                width: 30,
            }, {
                text: '描述',
                dataIndex: 'content',
                // width: 50,
            }],
            store: Ext.create('XHome.data.JsonStore', {
                fields: ['id', 'category', 'content', 'action', 'type', 'obj',
                    'createdStr', 'modifiedStr',
                    'owner', 'modifier', 'version', 'status'],
                url: 'xauth/manage_log/query.json',
            }),
        });

        config.items = [spanel, grid];
        this.callParent([config]);
    },
});
