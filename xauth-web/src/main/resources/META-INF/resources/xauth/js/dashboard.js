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
            html: '<h1>XHome XAuth Dashboard</h1>',
        },
        navigationConfig: { // 导航菜单配置
            title: '导航菜单1',
            root: {
                children: [{
                    text: '系统管理',
                    leaf: true,
                    iconCls: 'icon-test', // 重置默认图标
                    showClass: 'Ext.Panel', // 显示面板类
                    showConfig: { // 显示面板配置
                        id: 'abcdef',
                        html: 'Good Job',
                    }
                }, {
                    text: '用户管理',
                    leaf: false,
                    children: [{
                        text: '角色管理',
                        leaf: true,
                        showClass: 'Ext.Panel',
                        showConfig: {
                            html: 'Hello World'
                        }
                    }]
                }]
            },
        },
    });
});
