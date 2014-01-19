/**
 * Author:   jhat
 * Date:     2013-12-24
 * Email:    cpf624@126.com
 * Home:     http://pfchen.org
 * Describe: 
 */

Ext.define('XHome.XAuth.Manage.User', {
    id: 'xhome_xauth_manage_user',
    extend: 'Ext.panel.Panel',
    items: [
        Ext.create('XHome.Dashboard.SearchPanel', {
            items: [{
                xtype: 'button',
                text: '查询',
                // iconAlign: 'left',
                // iconCls: 'icon_query',
                listeners: {'click': function() {alert(1);}},
            }]
        }),
        Ext.create('XHome.Dashboard.EditorGridPanel', {
            id: 'xhome_xauth_manage_user_grid',
            columns: [{
                text: 'Name',
                dataIndex: 'name',
            }, {
                text: 'Nick',
                dataIndex: 'nick',
            }, {
                text: 'Email',
                dataIndex: 'email',
            }, {
                text: 'Create',
                dataIndex: 'createdStr',
            }, {
                text: 'Modify',
                dataIndex: 'modifiedStr',
            }],
            store: Ext.create('Ext.data.JsonStore', {
                fields: ['name', 'nick', 'email', 'createdStr', 'modifiedStr'],
                autoLoad: true,
                proxy: {
                    type: 'ajax',
                    url: 'xauth/user/query.json',
                    reader: {
                        type: 'json',
                        root: 'results',
                        idProperty: 'id'
                    }
                }
            }),
        })
    ]
});
