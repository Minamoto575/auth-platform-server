package cn.krl.authplatformserver.common.config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @description 代码模板快速生成
 * @author kuang
 * @date 2021/11/11
 */
public class CodeGenerator {
    public static void main(String[] args) {

        AutoGenerator autoGenerator = new AutoGenerator();

        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        globalConfig.setAuthor("kuang");
        globalConfig.setOpen(false);
        globalConfig.setSwagger2(true);

        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(
                "jdbc:mysql://localhost:13306/sso_center?serverTimezone=GMT%2B8&useUnicode=true"
                        + "&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("sso_center");
        dataSourceConfig.setPassword("X6ZogX$WD%@uCU");

        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        // 模块名
        pc.setModuleName(null);
        pc.setParent("cn.krl.authplatformserver");
        pc.setController("controller");
        pc.setEntity("model.po");
        pc.setService("service");
        pc.setMapper("mapper");

        autoGenerator.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // 对那一张表生成代码
        strategy.setInclude("user", "subsystem");
        // 数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // lombok 模型 @Accessors(chain = true) setter链式操作
        strategy.setEntityLombokModel(true);
        // restful api风格控制器
        strategy.setRestControllerStyle(true);
        // url中驼峰转连字符
        strategy.setControllerMappingHyphenStyle(true);

        autoGenerator.setStrategy(strategy);

        autoGenerator.execute();
    }
}
