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
                    id: 'mxauth',
                    text: '认证管理',
                    iconCls: 'icon-nav-user',
                    children: [{
                        id: 'mxauth_role',
                        text: '角色管理',
                        leaf: true,
                        iconCls: 'icon-nav-user-role',
                        showScript: 'xauth/js/manage/role.js',
                        showClass: 'XHome.XAuth.Manage.Role'
                    },{
                        id: 'mxauth_user',
                        text: '用户管理',
                        leaf: true,
                        iconCls: 'icon-nav-user-user',
                        showScript: 'xauth/js/manage/user.js',
                        showClass: 'XHome.XAuth.Manage.User'
                    }]
                }, {
                    text: '系统管理',
                    iconCls: 'icon-nav-sys',
                }]
            },
        },
    });
});
