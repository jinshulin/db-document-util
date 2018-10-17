package com.jd.ips;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.jd.ips.entity.TableDesc;
import com.jd.ips.excel.ExcelUtil;
import com.jd.ips.repository.SchemaRepository;

/**
 * @description  
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月15日 18时12分
 */
@SpringBootApplication
@EnableJpaRepositories
public class Application implements InitializingBean {

    @Autowired
    private SchemaRepository schemaRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TableDesc> list = schemaRepository.queryTables();
        ExcelUtil.output("E:\\table.xlsx", list);
    }
}
