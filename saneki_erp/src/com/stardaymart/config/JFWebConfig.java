package com.stardaymart.config;

import cn.dreampie.quartz.QuartzPlugin;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.cache.EhCache;
//import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;

import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.beetl.ext.jfinal3.JFinal3BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.plugin.tablebind.SimpleNameStyles;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.jfplugin.mail.MailPlugin;


public class JFWebConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {
        //SqlReporter.setLogger(true);
        me.setErrorView(401, "401.html");
        me.setErrorView(403, "403.html");
        me.setError404View("404.html");
        me.setError500View("500.html");

        // 加载数据库配置
        loadPropertyFile("jdbc.properties");
        // 设定Beetl
        me.setRenderFactory(new JFinal3BeetlRenderFactory());
        // 设定为开发
        me.setDevMode(true);
    }

    @Override
    public void configRoute(Routes me) {
        me.add(new AutoBindRoutes());
    }

    @Override
    public void configPlugin(Plugins me) {
        // mysql
        String url = getProperty("jdbcUrl");
        String username = getProperty("user");
        String password = getProperty("password");
        String driverClass = getProperty("driverClass");
        String filters = getProperty("filters");

        // mysql 数据�?
        DruidPlugin dsMysql = new DruidPlugin(url, username, password, driverClass, filters);
        dsMysql.setMaxActive(200);
        me.add(dsMysql);

        ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("mysql", dsMysql);
        me.add(arpMysql);

        AutoTableBindPlugin atbp = new AutoTableBindPlugin(dsMysql, SimpleNameStyles.LOWER);
        atbp.setShowSql(true);
        atbp.setDialect(new MysqlDialect());// 配置MySql方言
        me.add(atbp);

        
        //定时任务
        QuartzPlugin quartzPlugin =  new QuartzPlugin();
        quartzPlugin.setJobs("job.properties");
        me.add(quartzPlugin);
        quartzPlugin.start();
     
        //邮件
        me.add(new MailPlugin(PropKit.use("mail.properties").getProperties()));
        EhCachePlugin ehCachePlugin=new EhCachePlugin(getClass().getClassLoader().getResource("ehcache.xml"));
        //缓存
        me.add(ehCachePlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {
    }

    @Override
    public void configHandler(Handlers me) {
    }

    @Override
    public void afterJFinalStart() {//容器启动�?
    
    }

	@Override
	public void configEngine(Engine arg0) {
		// TODO Auto-generated method stub
		
	}

}
