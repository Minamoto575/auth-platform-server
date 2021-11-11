package cn.krl.authplatformserver.common.config;

/**
 * @description 代码模板快速生成
 * @author kuang
 * @data 2021/11/11
 */
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

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
                "jdbc:mysql://localhost:3306/cas?serverTimezone=GMT%2B8&useUnicode=true"
                        + "&useSSL=false&characterEncoding=utf8");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("990225");

        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null); // 模块名
        pc.setParent("cn.krl.authplatformserver");
        pc.setController("controller");
        pc.setEntity("model.vo");
        pc.setService("service");
        pc.setMapper("mapper");

        autoGenerator.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("user"); // 对那一张表生成代码
        strategy.setNaming(NamingStrategy.underline_to_camel); // 数据库表映射到实体的命名策略

        strategy.setColumnNaming(NamingStrategy.underline_to_camel); // 数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true); // restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); // url中驼峰转连字符

        autoGenerator.setStrategy(strategy);

        autoGenerator.execute();
    }
}
