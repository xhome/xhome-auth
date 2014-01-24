Ext.onReady(function() {
   // 不支持IE 8及其以下版本
   if (Ext.isIE8m) {
        Ext.MessageBox.alert('Info', 'Unsupport IE 8 and lower version.');
        return;
    }
    
    // ExtJS初始化提示
    Ext.QuickTips.init();
    
    // 生成界面
    new XHome.Dashboard({
        logoConfig: {
            // html: '<h1>XHome XAuth Dashboard</h1>',
        },
        navigationConfig: { // 导航菜单配置
            title: '导航菜单',
            root: {
                children: [{
                    id: 'xauth_manage',
                    text: '认证管理',
                    iconCls: 'icon-nav-manage',
                    children: [{
                        id: 'xauth_manage-role',
                        text: '角色管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-role',
                        showScript: 'xauth/js/manage/role.js',
                        showClass: 'XHome.XAuth.Manage.Role'
                    },{
                        id: 'xauth_manage-user',
                        text: '用户管理',
                        leaf: true,
                        iconCls: 'icon-nav-manage-user',
                        showScript: 'xauth/js/manage/user.js',
                        showClass: 'XHome.XAuth.Manage.User'
                    }]
                }, {
                    id: 'xauth_system',
                    text: '系统管理',
                    iconCls: 'icon-nav-system',
                    children: [{
                        id: 'xauth_system-config',
                        text: '系统配置',
                        leaf: true,
                        iconCls: 'icon-nav-system-config',
                        showScript: 'xauth/js/system/config.js',
                        showClass: 'XHome.XAuth.System.Config',
                    }]
                }]
            },
        },
    });
});
