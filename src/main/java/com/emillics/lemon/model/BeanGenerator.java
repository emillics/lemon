/*
 * Copyright 2016-2020 the original author.All rights reserved.
 * Kingstar(honeysoft@126.com)
 * The license,see the LICENSE file.
 */

package com.emillics.lemon.model;

import org.teasoft.bee.osql.BeeException;
import org.teasoft.honey.osql.autogen.GenBean;
import org.teasoft.honey.osql.autogen.GenConfig;
import org.teasoft.honey.osql.core.HoneyConfig;

/**
 * @author Kingstar
 * @since 1.7.2
 */
public class BeanGenerator {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try {
//            DBUtils.a();
            String dbName = HoneyConfig.getHoneyConfig().getDbName();
//			driverName,url,username,password config in bee.properties.

            GenConfig config = new GenConfig();
            config.setDbName(dbName);
            config.setGenToString(true);//生成toString方法
            config.setGenSerializable(true);

//			更改成本地的具体路径  change to your real path
//			config.setBaseDir("D:\\xxx\\yyy\\bee-exam\\src\\main\\java\\");
            config.setBaseDir("/Users/whl/Documents/develop/macOS/IDEA/lemon/src/main/java");
            config.setPackagePath("com.emillics.lemon.model");
//            config.setPackagePath("com.heda.zhjiaofx.model.anhui");
//            config.setPackagePath("com.heda.zhjiaofx.model.system");
//            config.setPackagePath("com.heda.zhjiaofx.model.shaoxing");
//            config.setPackagePath("com.heda.zhjiaofx.model.qiantang");
//			config.setPackagePath("org.teasoft.exam.bee.osql.entity.dynamic");
//			config.setPackagePath("org.teasoft.exam.bee.osql.entity.sqlite");
//			config.setPackagePath("org.teasoft.exam.bee.osql.entity.postgreSQL");
//			config.setPackagePath("org.teasoft.exam.bee.osql.entity.h2");

            GenBean genBean = new GenBean(config);
//			genBean.genSomeBeanFile("leaf_alloc,Orders");
//			genBean.genSomeBeanFile("Orders,user");
//			genBean.genSomeBeanFile("POSTGRESQL_TYPE");
//			genBean.genSomeBeanFile("H2_TYPE");

//            genBean.genSomeBeanFile("district");
//            genBean.genSomeBeanFile("street");
//            genBean.genSomeBeanFile("apper");
//            genBean.genSomeBeanFile("visit_record");
//            genBean.genSomeBeanFile("apper_device");
//            genBean.genSomeBeanFile("version");
//            genBean.genSomeBeanFile("apper_location_log");
//            genBean.genSomeBeanFile("leave_req");
//            genBean.genSomeBeanFile("user_street_map");
//            genBean.genSomeBeanFile("rule_sign_code");
//            genBean.genSomeBeanFile("user");
//			genBean.genSomeBeanFile("tableinfo");
//			genBean.genSomeBeanFile("leftsz_info");
//			genBean.genSomeBeanFile("t_test");
//            genBean.genSomeBeanFile("notice_send_detail");
//            genBean.genSomeBeanFile("apper_risk_assess");
//            genBean.genSomeBeanFile("disruptor_fail_log");
//            genBean.genSomeBeanFile("tx_video");
            genBean.genSomeBeanFile("video");
//
        } catch (BeeException e) {
            e.printStackTrace();
        }
    }
}