package com.server;

import com.system.thread.CoinMainThead;
import com.utils.CommonUtils;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Configuration
@ComponentScan(basePackages = {"com"})
public class ConfigContext {

    private SpringContextHolder springContextHolder;

    public @Bean
    SpringContextHolder getSpringContextHolder() {
        return springContextHolder;
    }

    public void setSpringContextHolder(SpringContextHolder springContextHolder) {
        this.springContextHolder = springContextHolder;
    }

    public ConfigContext() {
        System.out.println("...");
        try {
            String dir = System.getProperty("user.dir");

            String log4j_file = dir + "/conf/log4j.properties";
            PropertyConfigurator.configure(log4j_file);
            ResourcePropertySource config = new ResourcePropertySource("config", "file:" + dir + "/conf/config.properties");
            try {
                Map<String, Object> map = config.getSource();
                Properties configPros = new Properties();
                Set<String> keys = map.keySet();
                for (String key : keys) {
                    configPros.put(key, map.get(key));
                }
                // BeanUtils.
                MyConstant.TRADEPWD = configPros.getProperty("trade_pwd");
                MyConstant.COINTYPE = CommonUtils.upperStr(configPros.getProperty("coin_type"));
                MyConstant.SALERATE = Double.valueOf(configPros.getProperty("sale_rate"));
                MyConstant.USERAGENT = configPros.getProperty("user_agent");
                MyConstant.global_price_watch_markets();
                CoinMainThead thread = new CoinMainThead();
                thread.start();
            } catch (Exception ex) {

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // @Bean(name = "SpringContextHolder")
    // public SpringContextHolder springContextHolder() {
    // return new SpringContextHolder();
    //
    // }

}
