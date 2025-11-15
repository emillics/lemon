package com.emillics.lemon.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.emillics.lemon.base.config.Constants;
import com.emillics.lemon.model.Result;
import com.emillics.lemon.model.Version;
import org.teasoft.bee.osql.PreparedSql;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.HoneyConfig;
import org.teasoft.honey.osql.core.HoneyContext;

import javax.sql.DataSource;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class DBUtils {
    private static SuidRich suidRich = BeeFactory.getHoneyFactory().getSuidRich();
    private static PreparedSql preparedSql = BeeFactory.getHoneyFactory().getPreparedSql();

    static {
        initDS();
        initConfig();
    }

    private static void initDS() {
        DruidDataSource dataSource1;
        dataSource1 = new DruidDataSource();
        dataSource1.setUrl("jdbc:sqlite:" + System.getProperty("user.dir") + File.separator + "db/test.db");
//        dataSource1.setUsername("heda_admin");
//        dataSource1.setPassword("Zhjiao@heda");

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        String dbs = Preferences.systemRoot().get("dbs" + Constants.APP_VERSION, "ds1");
        List<String> dbList = Arrays.stream(dbs.split(",")).collect(Collectors.toList());

        if (dbList.contains("ds1")) dataSourceMap.put("ds1", dataSource1);
        BeeFactory.getInstance().setDataSourceMap(dataSourceMap);
    }

    private static void initConfig() {
        HoneyConfig.getHoneyConfig().multiDS_enable = true;
        HoneyConfig.getHoneyConfig().multiDS_type = 2;
        HoneyConfig.getHoneyConfig().multiDS_differentDbType = true;
        HoneyConfig.getHoneyConfig().multiDS_defalutDS = "ds1";
        HoneyConfig.getHoneyConfig().multiDS_matchEntityClassPath =
                "ds1:com.emillics.lemon.model.**";
        HoneyContext.setConfigRefresh(true);
        HoneyConfig.getHoneyConfig().showSQL = true;
    }

    /*public static Result<IUser> login(String userClassPath, String account, String passwd) {
        IUser user = null;
        String error = null;
        try {
            user = (IUser) Class.forName(userClassPath + ".User").newInstance();
            user.setName(account);
            user.setPassword(MD5.getMd5(passwd));
            List<IUser> users = suidRich.select(user);
            if (users.size() == 0) {
                user = null;
                error = "用户不存在，或密码不正确";
            } else user = users.get(0);
        } catch (InstantiationException | IllegalAccessException | BeeException | ClassNotFoundException e) {
            e.printStackTrace();
            user = null;
            error = e.getMessage();
        }
        return new Result<>(user, error);
    }*/

//    public static Result<List<IDistrict>> getDistrictList(String districtClassPath) {
//        lazyInitDB(districtClassPath);
//        List<IDistrict> districtList = null;
//        String error = null;
//        try {
//            IDistrict district = (IDistrict) Class.forName(districtClassPath + ".District").newInstance();
//            districtList = suidRich.select(district);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(districtList, error);
//    }
//
//    public static Result<List<StreetBase>> getStreetList(String streetClassPath, Integer districtId, Integer streetId) {
//        lazyInitDB(streetClassPath);
//        List<StreetBase> streetList = null;
//        String error = null;
//        try {
//            StreetBase street = (StreetBase) Class.forName(streetClassPath + ".Street").newInstance();
//            if (districtId == null && streetId != null) {
//                StreetBase s = suidRich.selectById(street, streetId);
//                if (s != null) {
//                    districtId = s.getDistrictId();
//                }
//            }
//            street.setDistrictId(districtId);
//            streetList = suidRich.select(street);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(streetList, error);
//    }
//
//    public static Result<Integer> getApperTotalCount(String apperClassPath,
//                                                     String apperName,
//                                                     Integer districtId,
//                                                     Integer streetId,
//                                                     Boolean status) {
//        lazyInitDB(apperClassPath);
//        Integer count = null;
//        String error = null;
//        try {
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            Result<Condition> result = hasApperCondition(apperClassPath, apperName, districtId, streetId, -1, 0, status);
//            if (result.getData() != null) {
//                count = Integer.valueOf(suidRich.selectWithFun(apper, FunctionType.COUNT, "*", result.getData()));
//            } else {
//                if (result.getError() != null) {
//                    error = result.getError();
//                    return new Result<>(count, error);
//                }
//                count = Integer.valueOf(suidRich.selectWithFun(apper, FunctionType.COUNT, "*"));
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(count, error);
//    }
//
//    public static Result<List<IApper>> getApperByPage(String apperClassPath,
//                                                      String apperName,
//                                                      Integer districtId,
//                                                      Integer streetId,
//                                                      int page,
//                                                      int size,
//                                                      Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IApper> apperList = null;
//        String error = null;
//        try {
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            StreetBase street = (StreetBase) Class.forName(apperClassPath + ".Street").newInstance();
//            Result<Condition> result = hasApperCondition(apperClassPath, apperName, districtId, streetId, page, size, status);
//            MoreTable moreTable = BeeFactory.getHoneyFactory().getMoreTable();
//            if (result.getData() != null) {
//                apper.setStreet(street);
//                apperList = moreTable.select(apper, result.getData());
//            } else {
//                if (result.getError() != null) {
//                    error = result.getError();
//                    return new Result<>(null, error);
//                }
//                apperList = moreTable.select(apper);
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperList, error);
//    }
//
//    private static Result<Condition> hasApperCondition(String apperClassPath,
//                                                       String apperName,
//                                                       Integer districtId,
//                                                       Integer streetId,
//                                                       Integer page,
//                                                       Integer size,
//                                                       Boolean status) {
//        lazyInitDB(apperClassPath);
//        Condition condition = new ConditionImpl();
//        String error;
//        boolean hasCondition = false;
//        if (apperName != null && !apperName.isEmpty()) {
//            condition.op("apper.apper_name", Op.like, "%" + apperName + "%");
//            hasCondition = true;
//        }
//        if (streetId != -1) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            condition.op("street_id", Op.eq, streetId);
//        } else if (districtId != -1) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            Result<List<StreetBase>> streetListResult = getStreetList(apperClassPath, districtId, streetId);
//            if (streetListResult.getError() != null) {
//                error = streetListResult.getError();
//                return new Result<>(null, error);
//            }
//            List<Integer> streetIdList = streetListResult.getData().stream().map(StreetBase::getId).collect(Collectors.toList());
//            StringBuffer sb = new StringBuffer();
//            for (Integer sid : streetIdList) {
//                sb.append(",").append(sid);
//            }
//            condition.op("street_id", Op.in, sb.toString().replaceFirst(",", ""));
//        }
//        if (page > -1) {
//            hasCondition = true;
//            condition.start(page * size).size(size);
//        }
//
//        if (status != null) {
//            condition.op("status", Op.eq, status ? 1 : 0);
//        }
//        if (!hasCondition) hasCondition = true;
//
//        return new Result<>(hasCondition ? condition : null, null);
//    }
//
//    public static Result<List<IApper>> getAppers(String apperClassPath,
//                                                 String[] apperNames,
//                                                 Integer districtId,
//                                                 Integer streetId,
//                                                 String[] statusList) {
//        lazyInitDB(apperClassPath);
//        List<IApper> apperList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "a.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR a.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND a.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND a.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String statusCondition = "";
//            if (statusList.length > 0) {
//                statusCondition = " AND (";
//                for (String status : statusList) {
//                    statusCondition += "a.`status` = " + status + " OR ";
//                }
//                statusCondition = statusCondition.substring(0, statusCondition.length() - 4) + ")";
//            }
//
//            String sql = "SELECT a.id,a.apper_name,s.district_name,a.street_id,a.street_name,a.sex,a.age,a.id_card," +
//                    "a.tel,a.nation,a.native_place,a.charge,a.regulate_type,a.regulate_level,a.regulate_date_start," +
//                    "a.regulate_date_end,a.create_time,a.uncorrect_time,a.`status` FROM apper AS a,street as s " +
//                    "WHERE a.street_id = s.id" + apperNamesCondition + streetCondition + statusCondition +
//                    " ORDER BY s.district_name,a.street_name,a.apper_name";
//            System.out.println(sql);
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            apperList = preparedSql.select(sql, apper, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperList, error);
//    }
//
//    public static Result<Boolean> setOriginPhoto(String user, String apperClassPath, Integer apperId, Integer streetId, String photoUrl) {
//        lazyInitDB(apperClassPath);
//        Boolean result = null;
//        String error = null;
//        try {
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            apper.setId(apperId);
//            apper.setRegulateBasePhoto(photoUrl);
//            apper.setModifier(user);
//            Condition condition = new ConditionImpl();
//            condition.op("street_id", Op.eq, streetId);
//            int update = suidRich.updateById(apper, condition);
//            if (update == 1) result = true;
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<Integer> setSignRule(String user,
//                                              String ruleSignClassPath,
//                                              Integer ruleId,
//                                              Integer apperId,
//                                              String apperName,
//                                              String signCode) {
//        lazyInitDB(ruleSignClassPath);
//        Integer result = null;
//        String error = null;
//        try {
//            ISignRule sign = (ISignRule) Class.forName(ruleSignClassPath + ".RuleSignCode").newInstance();
//            if (ruleId != null) {
//                sign.setId(ruleId);
//                sign.setRuleSignCode(signCode);
//                sign.setModifier(user);
//                Condition condition = new ConditionImpl();
//                condition.op("apper_id", Op.eq, apperId);
//                int update = suidRich.updateById(sign, condition);
//                if (update == 1) result = ruleId;
//            } else {
//                sign.setApperId(apperId);
//                sign.setApperName(apperName);
//                sign.setRuleSignCode(signCode);
//                sign.setCreator(user);
//                sign.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
//                int insert = (int) suidRich.insertAndReturnId(sign);
//                if (insert > 0) result = insert;
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    private static Result<Condition> hasUserCondition(String account,
//                                                      String personName,
//                                                      Integer districtId,
//                                                      Integer streetId,
//                                                      Boolean isLocked,
//                                                      Integer page,
//                                                      Integer size) {
//        Condition condition = new ConditionImpl();
//        String error;
//        boolean hasCondition = false;
//        if (account != null && !account.isEmpty()) {
//            condition.op("name", Op.like, "%" + account + "%");
//            hasCondition = true;
//        }
//        if (personName != null && !personName.isEmpty()) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            condition.op("person_name", Op.like, "%" + personName + "%");
//        }
//        if (streetId != -1) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            condition.op("street_id", Op.eq, streetId);
//        } else if (districtId != -1) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            condition.op("district_id", Op.eq, districtId);
//        }
//
//        if (isLocked != null && isLocked) {
//            if (hasCondition) {
//                condition.and();
//            } else {
//                hasCondition = true;
//            }
//            condition.op("is_locked", Op.eq, 1);
//        }
//
//        if (page > -1) {
//            hasCondition = true;
//            condition.start(page * size).size(size);
//        }
//
//        condition.op("is_deleted", Op.eq, 0);
//        if (!hasCondition) hasCondition = true;
//
//        return new Result<>(hasCondition ? condition : null, null);
//    }
//
//    public static Result<Integer> getUserTotalCount(String userClassPath,
//                                                    String account,
//                                                    String personName,
//                                                    Integer districtId,
//                                                    Integer streetId,
//                                                    Boolean isLocked) {
//        lazyInitDB(userClassPath);
//        Integer count = null;
//        String error = null;
//        try {
//            IUser user = (IUser) Class.forName(userClassPath + ".User").newInstance();
//            Result<Condition> result = hasUserCondition(account, personName, districtId, streetId, isLocked, -1, 0);
//            if (result.getData() != null) {
//                count = Integer.valueOf(suidRich.selectWithFun(user, FunctionType.COUNT, "*", result.getData()));
//            } else {
//                if (result.getError() != null) {
//                    error = result.getError();
//                    return new Result<>(count, error);
//                }
//                count = Integer.valueOf(suidRich.selectWithFun(user, FunctionType.COUNT, "*"));
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(count, error);
//    }
//
//    public static Result<List<IUser>> getUserByPage(String userClassPath,
//                                                    String account,
//                                                    String apperName,
//                                                    Integer districtId,
//                                                    Integer streetId,
//                                                    Boolean isLocked,
//                                                    int page,
//                                                    int size) {
//        lazyInitDB(userClassPath);
//        List<IUser> userList = null;
//        String error = null;
//        try {
//            IUser user = (IUser) Class.forName(userClassPath + ".User").newInstance();
//            Result<Condition> result = hasUserCondition(account, apperName, districtId, streetId, isLocked, page, size);
////            MoreTable moreTable = BeeFactory.getHoneyFactory().getMoreTable();
//            if (result.getData() != null) {
//                userList = suidRich.select(user, result.getData());
//            } else {
//                if (result.getError() != null) {
//                    error = result.getError();
//                    return new Result<>(null, error);
//                }
//                userList = suidRich.select(user);
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(userList, error);
//    }
//
//    public static Result<Integer> unlockUser(String operator,
//                                             String userClassPath,
//                                             Integer userId) {
//        lazyInitDB(userClassPath);
//        Integer result = null;
//        String error = null;
//        try {
//            IUser user = (IUser) Class.forName(userClassPath + ".User").newInstance();
//            if (userId != null) {
//                user.setId(userId);
//                user.setIsLocked(false);
//                user.setPwdTryTimes(0);
//                user.setModifier(operator);
//                Condition condition = new ConditionImpl();
//                condition.op("is_locked", Op.eq, 1);
//                int update = suidRich.updateById(user, condition);
//                if (update == 1) result = userId;
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<IApper>> getApperStatistics(String apperClassPath,
//                                                          Integer[] districtIds,
//                                                          String startDate,
//                                                          String endDate,
//                                                          boolean deviceType) {
//        lazyInitDB(apperClassPath);
//        List<IApper> apperList = null;
//        String error = null;
//        try {
//            StringBuilder sb = new StringBuilder("");
//            if (districtIds != null && districtIds.length > 0) {
//                sb.append(" AND a.street_id IN(SELECT id FROM street WHERE district_id IN(");
//                for (int i = 0; i < districtIds.length; i++) {
//                    sb.append(districtIds[i]);
//                    if (i < districtIds.length - 1) sb.append(",");
//                }
//                sb.append("))");
//            }
//            String districtCondition = sb.toString();
//
////            String sql = "SELECT a.apper_name,a.tel,s.district_name,a.street_name,a.id_card,a.sex,a.regulate_level," +
////                    "a.remark,a.regulate_date_start,a.regulate_date_end,a.create_time,a.uncorrect_time " +
////                    (deviceType ? ",(SELECT device_id FROM apper_location_log WHERE apper_id=a.id ORDER BY last_location_time DESC LIMIT 1) AS device_id " : "") +
////                    "FROM apper AS a,street AS s WHERE EXISTS(SELECT 1 FROM apper_location_log WHERE apper_id=a.id AND " +
////                    "last_location_time<'" + endDate + "' LIMIT 1) AND a.street_id=s.id AND (a.uncorrect_time IS NULL OR " +
////                    "a.uncorrect_time>='" + startDate + "')" + districtCondition +
////                    " ORDER BY s.district_id,a.street_name,a.apper_name";
//            String sql = "SELECT a.apper_name,a.tel,s.district_name,a.street_name,a.id_card,a.sex,a.regulate_level," +
//                    "a.remark,a.regulate_date_start,a.regulate_date_end,a.create_time,a.uncorrect_time " +
//                    (deviceType ? ",(SELECT device_id FROM apper_location_log WHERE apper_id=a.id ORDER BY last_location_time DESC LIMIT 1) AS device_id " : "") +
//                    "FROM apper AS a,street AS s Where a.street_id=s.id AND (a.uncorrect_time IS NULL OR " +
//                    "a.uncorrect_time>='" + startDate + "') AND a.create_time < '" + endDate + "'" + districtCondition +
//                    " ORDER BY s.district_id,a.street_name,a.regulate_date_start asc";
//            System.out.println(sql);
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            apperList = preparedSql.select(sql, apper, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperList, error);
//    }
//
//    public static Result<List<IApperSignLog>> getApperSignLogs(String apperClassPath,
//                                                               String[] apperNames,
//                                                               Integer districtId,
//                                                               Integer streetId,
//                                                               String startDate,
//                                                               String endDate,
//                                                               String[] signTypes,
//                                                               Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IApperSignLog> signLogList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "l.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR l.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND a.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND a.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String signTypeCondition = " AND (";
//            for (String signType : signTypes) {
//                signTypeCondition += "l.sign_type = " + signType + " OR ";
//            }
//            signTypeCondition = signTypeCondition.substring(0, signTypeCondition.length() - 4) + ")";
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String sql = "SELECT a.apper_name,s.district_name,a.street_name,a.regulate_base_photo,l.id,l.sign_type," +
//                    "l.act_sign_time,l.act_sign_address,l.act_img_url,l.img_similarity FROM apper_sign_log as l," +
//                    "apper AS a,street as s WHERE l.apper_id = a.id AND a.street_id = s.id" + statusCondition +
//                    apperNamesCondition + streetCondition + " AND l.act_sign_time >= '" + startDate + "' AND l.act_sign_time < '" +
//                    endDate + "'" + signTypeCondition + " ORDER BY s.district_name,a.street_name,a.apper_name,l.act_sign_time";
//            System.out.println(sql);
//            IApperSignLog signLog = (IApperSignLog) Class.forName(apperClassPath + ".ApperSignLog").newInstance();
//            signLogList = preparedSql.select(sql, signLog, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(signLogList, error);
//    }
//
//    public static Result<List<IViolationEvent>> getViolationEvents(String apperClassPath,
//                                                                   String[] apperNames,
//                                                                   Integer districtId,
//                                                                   Integer streetId,
//                                                                   String startDate,
//                                                                   String endDate,
//                                                                   String[] violationTypes,
//                                                                   String[] violationStatus,
//                                                                   String[] violationResults,
//                                                                   Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IViolationEvent> violationEventList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "v.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR v.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND a.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND a.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String violationTypeCondition = "";
//            if (violationTypes != null && violationTypes.length > 0) {
//                violationTypeCondition = " AND (";
//                for (String violationType : violationTypes) {
//                    violationTypeCondition += "v.type = " + violationType + " OR ";
//                }
//                violationTypeCondition = violationTypeCondition.substring(0, violationTypeCondition.length() - 4) + ")";
//            }
//
//            String violationStatusCondition = "";
//            if (violationStatus != null && violationStatus.length > 0) {
//                violationStatusCondition = " AND (";
//                for (String vStatus : violationStatus) {
//                    violationStatusCondition += "v.`status` = " + vStatus + " OR ";
//                }
//                violationStatusCondition = violationStatusCondition.substring(0, violationStatusCondition.length() - 4) + ")";
//            }
//
//            String violationResultCondition = "";
//            if (violationResults != null && violationResults.length > 0) {
//                violationResultCondition = " AND (";
//                for (String violationResult : violationResults) {
//                    violationResultCondition += "v.result_type = " + violationResult + " OR ";
//                }
//                violationResultCondition = violationResultCondition.substring(0, violationResultCondition.length() - 4) + ")";
//            }
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String sql = "SELECT v.id,v.apper_id,a.apper_name,s.district_name,a.street_name,v.type,v.content,v.create_time,v.rule_sign_time," +
//                    "v.`status`,v.result_type,v.handler,v.result_remark,v.is_push_wechat,v.modifier,v.modify_time FROM violation_event as v," +
//                    "apper AS a,street as s WHERE v.apper_id = a.id" + " AND a.street_id = s.id" + statusCondition + apperNamesCondition +
//                    streetCondition + " AND v.create_time >= '" + startDate + "' AND v.create_time < '" + endDate + "'" +
//                    violationTypeCondition + violationStatusCondition + violationResultCondition +
//                    " ORDER BY s.district_name,a.street_name,a.apper_name,v.create_time";
//            System.out.println(sql);
//            IViolationEvent violationEvent = (IViolationEvent) Class.forName(apperClassPath + ".ViolationEvent").newInstance();
//            violationEventList = preparedSql.select(sql, violationEvent, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(violationEventList, error);
//    }
//
//    public static Result<Integer> setViolationEventResult(String user,
//                                                          String ruleSignClassPath,
//                                                          Integer eventId,
//                                                          Integer apperId,
//                                                          Integer resultType,
//                                                          String remark) {
//        lazyInitDB(ruleSignClassPath);
//        Integer result = null;
//        String error = null;
//        try {
//            IViolationEvent violation = (IViolationEvent) Class.forName(ruleSignClassPath + ".ViolationEvent").newInstance();
//            violation.setId(eventId);
//            violation.setResultType(resultType);
//            violation.setResultRemark(remark);
//            violation.setModifier(user);
//            Condition condition = new ConditionImpl();
//            condition.op("apper_id", Op.eq, apperId);
//            int update = suidRich.updateById(violation, condition);
//            if (update == 1) result = eventId;
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<IVisitRecord>> getVisitRecords(String apperClassPath,
//                                                             String[] apperNames,
//                                                             Integer districtId,
//                                                             Integer streetId,
//                                                             String startDate,
//                                                             String endDate,
//                                                             String[] statusTypes,
//                                                             String reportStart,
//                                                             String reportEnd,
//                                                             String confirmStart,
//                                                             String confirmEnd,
//                                                             boolean onlyHasVideo,
//                                                             Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IVisitRecord> visitRecordList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "v.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR v.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND v.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND v.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String statusTypeCondition = "";
//            if (statusTypes != null && statusTypes.length > 0) {
//                statusTypeCondition = " AND (";
//                for (String statusType : statusTypes) {
//                    statusTypeCondition += "v.record_status = " + statusType + " OR ";
//                }
//                statusTypeCondition = statusTypeCondition.substring(0, statusTypeCondition.length() - 4) + ")";
//            }
//
//            String reportTimeCondition = "";
//            if (reportStart != null && reportEnd != null) {
//                reportTimeCondition = " AND v.report_time >= '" + reportStart + "' AND v.report_time < '" + reportEnd + "'";
//            }
//
//            String confirmTimeCondition = "";
//            if (confirmStart != null && confirmEnd != null) {
//                confirmTimeCondition = " AND v.confirm_time >= '" + confirmStart + "' AND v.confirm_time < '" + confirmEnd + "'";
//            }
//
//            String videoCondition = "";
//            if (onlyHasVideo) {
//                videoCondition = " AND v.file_id IS NOT NULL";
//            }
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String sql = "SELECT v.id,s.district_name,v.active_date,v.start_time,v.end_time,v.street_name,v.worker," +
//                    "v.report_time,v.confirm_time,v.record_status,v.apper_name,v.file_id,e.work_unit,e.visitor,e.visit_time," +
//                    "e.personnel,e.visit_reason,e.remark,e.place FROM visit_record as v LEFT JOIN apper as a ON v.apper_id = a.id " +
//                    "LEFT JOIN street as s ON v.street_id = s.id LEFT JOIN visit_evaluate as e ON v.id = e.record_id WHERE 1=1" +
//                    statusCondition + apperNamesCondition + streetCondition +
//                    " AND v.active_date >= '" + startDate + "' AND v.active_date < '" + endDate + "'" +
//                    statusTypeCondition + reportTimeCondition + confirmTimeCondition + videoCondition +
//                    " ORDER BY s.district_name,v.street_name,v.apper_name,v.active_date";
//            System.out.println(sql);
//            IVisitRecord visitRecord = (IVisitRecord) Class.forName(apperClassPath + ".VisitRecord").newInstance();
//            visitRecordList = preparedSql.select(sql, visitRecord, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(visitRecordList, error);
//    }
//
//    public static Result<Integer> changeStreet(String userClassPath,
//                                               Integer id,
//                                               Integer oldStreetId,
//                                               Integer newStreetId,
//                                               String streetName,
//                                               boolean downGrade,
//                                               String operator) {
//        lazyInitDB(userClassPath);
//        Integer result = null;
//        String error = null;
//        try {
//            IUser user = (IUser) Class.forName(userClassPath + ".User").newInstance();
//            user.setId(id);
//            user.setStreetId(newStreetId);
//            user.setStreetName(streetName);
//            user.setDepartment(streetName);
//            if (downGrade) user.setType(2);
//            user.setModifier(operator);
//            int updateUser = suidRich.updateById(user, null);
//            if (updateUser == 1) {
//                IUserStreetMap userStreetMap = (IUserStreetMap) Class.forName(userClassPath + ".UserStreetMap").newInstance();
//                if (downGrade) {
//                    userStreetMap.setUserId(id);
//                    suidRich.delete(userStreetMap);
//                    userStreetMap.setUserId(id);
//                    userStreetMap.setStreetId(newStreetId);
//                    userStreetMap.setCreator(operator);
//                    userStreetMap.setCreateTime(new Timestamp(new Date().getTime()));
//                    int insert = (int) suidRich.insertAndReturnId(userStreetMap);
//                    if (insert > 0) result = newStreetId;
//                } else {
//                    userStreetMap.setUserId(id);
//                    userStreetMap.setStreetId(oldStreetId);
//                    Condition condition = new ConditionImpl();
//                    condition.set("street_id", newStreetId);
//                    condition.set("modifier", operator);
//                    int updateUserStreetMap = suidRich.update(userStreetMap, condition);
//                    if (updateUserStreetMap == 1) result = newStreetId;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<IUser> upgradeUser(String userClassPath,
//                                            Integer id,
//                                            Integer districtId,
//                                            String districtName,
//                                            Integer streetId,
//                                            String operator) {
//        lazyInitDB(userClassPath);
//        IUser result = null;
//        String error = null;
//        try {
//            if (districtId == null) {
//                StreetBase street = (StreetBase) Class.forName(userClassPath + ".Street").newInstance();
//                StreetBase s = suidRich.selectById(street, streetId);
//                if (s != null) {
//                    districtId = s.getDistrictId();
//                    districtName = s.getDistrictName();
//                }
//            }
//            if (districtId != null) {
//                IUser user = (IUser) Class.forName(userClassPath + ".User").newInstance();
//                user.setId(id);
//                user.setDistrictId(districtId);
//                user.setDistrictName(districtName);
//                user.setDepartment(districtName);
//                user.setType(4);
//                user.setModifier(operator);
//                int updateUser = suidRich.updateById(user, null);
//                if (updateUser == 1) {
//                    IUserStreetMap userStreetMap = (IUserStreetMap) Class.forName(userClassPath + ".UserStreetMap").newInstance();
//                    userStreetMap.setUserId(id);
//                    suidRich.delete(userStreetMap);
//
//                    StreetBase street = (StreetBase) Class.forName(userClassPath + ".Street").newInstance();
//                    street.setDistrictId(districtId);
//                    List<StreetBase> streetList = suidRich.select(street);
//                    if (streetList != null) {
//                        Timestamp createTime = new Timestamp(new Date().getTime());
//                        for (StreetBase s : streetList) {
//                            userStreetMap.setUserId(id);
//                            userStreetMap.setStreetId(s.getId());
//                            userStreetMap.setCreator(operator);
//                            userStreetMap.setCreateTime(createTime);
//                            suidRich.insert(userStreetMap);
//                        }
//                    }
//                    result = (IUser) Class.forName(userClassPath + ".User").newInstance();
//                    result.setDistrictId(districtId);
//                    result.setDistrictName(districtName);
//                    result.setDepartment(districtName);
//                    result.setType(4);
//                    result.setModifier(operator);
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<ILeaveReq>> getLeaveRequests(String apperClassPath,
//                                                           String[] apperNames,
//                                                           Integer districtId,
//                                                           Integer streetId,
//                                                           Boolean status,
//                                                           String startDate,
//                                                           String endDate,
//                                                           String[] types,
//                                                           Boolean noSign,
//                                                           String[] days) {
//        lazyInitDB(apperClassPath);
//        List<ILeaveReq> leaveReqList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "r.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR r.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND r.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND r.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String typeCondition = "";
//            if (types != null) {
//                for (String type : types) {
//                    if (type != null && !type.trim().isEmpty()) {
//                        if (!typeCondition.startsWith(" AND (")) {
//                            typeCondition += " AND (";
//                            typeCondition += "r.`status` = " + type;
//                        } else {
//                            typeCondition += " OR r.`status` = " + type;
//                        }
//                    }
//                }
//                if (!typeCondition.isEmpty()) {
//                    typeCondition += ")";
//                }
//            }
//
//            String noSignCondition = "";
//            if (noSign != null) {
//                noSignCondition = " AND r.apply_no_sign =" + (noSign ? 1 : 0);
//            }
//
//            String daysCondition = "";
//            if (days != null) {
//                for (String day : days) {
//                    if (day != null && !day.trim().isEmpty()) {
//                        if (!daysCondition.startsWith(" AND (")) {
//                            daysCondition += " AND (";
//                            daysCondition += "r.days = " + day;
//                        } else {
//                            daysCondition += " OR r.days = " + day;
//                        }
//                    }
//                }
//                if (!daysCondition.isEmpty()) {
//                    daysCondition += ")";
//                }
//            }
//
//            String sql = "SELECT r.*,s.district_name FROM leave_req as r,street as s, apper as a WHERE r.street_id = s.id AND r.apper_id = a.id" +
//                    " AND r.start_date >= '" + startDate + "' AND r.end_date < '" + endDate + "'" + statusCondition +
//                    apperNamesCondition + streetCondition + typeCondition + noSignCondition + daysCondition +
//                    " ORDER BY s.district_name,r.street_name,r.apper_name,r.start_date";
//            System.out.println(sql);
//            ILeaveReq leaveReq = (ILeaveReq) Class.forName(apperClassPath + ".LeaveReq").newInstance();
//            leaveReqList = preparedSql.select(sql, leaveReq, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(leaveReqList, error);
//    }
//
//    public static Result<Boolean> deleteLeaveRequest(String apperClassPath, Integer leaveReqId, String operator) {
//        lazyInitDB(apperClassPath);
//        Boolean result = null;
//        String error = null;
//        try {
//            Class clazz = Class.forName(apperClassPath + ".LeaveReq");
//            int delete = suidRich.deleteById(clazz, leaveReqId);
//            if (delete == 1) result = true;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<IDevice>> getAppVersionList(String apperClassPath) {
//        lazyInitDB(apperClassPath);
//        List<IDevice> versionList = null;
//        String error = null;
//        try {
//            String sql = "SELECT version_name FROM apper_device GROUP BY version_name ORDER BY version_name";
//            System.out.println(sql);
//            IDevice device = (IDevice) Class.forName(apperClassPath + ".ApperDevice").newInstance();
//            versionList = preparedSql.select(sql, device, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(versionList, error);
//    }
//
//    public static Result<List<IDevice>> getApperAppVersions(String apperClassPath,
//                                                            String[] apperNames,
//                                                            Integer districtId,
//                                                            Integer streetId,
//                                                            String[] versionTypes,
//                                                            Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IDevice> deviceList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "a.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR a.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND a.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND a.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String versionTypeCondition = " AND (";
//            for (String versionType : versionTypes) {
//                if ("无".equals(versionType)) {
//                    versionTypeCondition += "d.version_name IS NULL OR ";
//                } else {
//                    versionTypeCondition += "d.version_name = '" + versionType + "' OR ";
//                }
//            }
//            versionTypeCondition = versionTypeCondition.substring(0, versionTypeCondition.length() - 4) + ")";
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String sql = "SELECT a.apper_name,a.tel,s.district_name,a.street_name,a.id_card,a.sex,a.regulate_level," +
//                    "a.regulate_date_start,a.regulate_date_end,d.device_type,d.version_name FROM apper AS a,apper_device AS d," +
//                    "street AS s WHERE d.apper_id IS NOT NULL and d.apper_id = a.id AND a.street_id = s.id"
//                    + statusCondition + apperNamesCondition + streetCondition + versionTypeCondition +
//                    " ORDER BY CONVERT(s.district_name USING gbk),CONVERT(a.street_name USING gbk),CONVERT(a.apper_name USING gbk)";
//            System.out.println(sql);
//            IDevice device = (IDevice) Class.forName(apperClassPath + ".ApperDevice").newInstance();
//            deviceList = preparedSql.select(sql, device, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(deviceList, error);
//    }
//
//    public static Result<Version> getSystemVersion() {
//        String versionClassPath = "com.heda.zhjiaofx.model.system.Version";
//        lazyInitDB(versionClassPath);
//        List<Version> versionList = null;
//        String error = null;
//        try {
//            String sql = "SELECT * FROM version ORDER BY id DESC LIMIT 1";
//            System.out.println(sql);
//            Version version = (Version) Class.forName(versionClassPath).newInstance();
//            versionList = preparedSql.select(sql, version, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(versionList == null || versionList.size() == 0 ? null : versionList.get(0), error);
//    }
//
//    public static Result<List<IApperLocationLog>> getApperLocationRecords(String apperClassPath,
//                                                                          String apperName,
//                                                                          Integer districtId,
//                                                                          Integer streetId,
//                                                                          String startDate,
//                                                                          String endDate,
//                                                                          Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IApper> apperList = null;
//        List<IApperLocationLog> locationLogList = null;
//        Integer count = null;
//        String error = null;
//        try {
//            String sql = "SELECT id FROM apper WHERE apper_name = '" + apperName + "' AND street_id = " + streetId +
//                    (status == null ? "" : (" AND `status` =" + (status ? 1 : 0))) + " ORDER BY create_time DESC LIMIT 1";
//            System.out.println(sql);
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//            apperList = preparedSql.select(sql, apper, (Object[]) null);
//            if (apperList != null && apperList.size() > 0) {
//                sql = "SELECT location_json,last_location_time,is_cross_border FROM apper_location_log WHERE apper_id = " + apperList.get(0).getId();
//                System.out.println(sql);
//                IApperLocationLog locationLog = (IApperLocationLog) Class.forName(apperClassPath + ".ApperLocationLog").newInstance();
//                locationLogList = preparedSql.select(sql, locationLog, (Object[]) null);
//            } else {
//                error = "该司法所不存在此用户，请确认姓名是否正确";
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(locationLogList, error);
//    }
//
//    public static Result<List<IApper>> getNotificationUnreadCount(String apperClassPath,
//                                                                  String[] apperNames,
//                                                                  Integer districtId,
//                                                                  Integer streetId,
//                                                                  boolean allowEmpty,
//                                                                  Boolean status) {
//        lazyInitDB(apperClassPath);
//        List<IApper> apperList = null;
//        String error = null;
//        try {
//            String apperNamesCondition = "";
//            for (String apperName : apperNames) {
//                if (apperName != null && !apperName.trim().isEmpty()) {
//                    if (!apperNamesCondition.startsWith(" AND (")) {
//                        apperNamesCondition += " AND (";
//                        apperNamesCondition += "a.apper_name LIKE '%" + apperName + "%'";
//                    } else {
//                        apperNamesCondition += " OR a.apper_name LIKE '%" + apperName + "%'";
//                    }
//                }
//            }
//            if (!apperNamesCondition.isEmpty()) {
//                apperNamesCondition += ")";
//            }
//
//            String streetCondition = "";
//            if (streetId != -1) {
//                streetCondition = " AND a.street_id =" + streetId;
//            } else if (districtId != -1) {
//                streetCondition = " AND a.street_id IN(SELECT id FROM street WHERE district_id = " + districtId + ")";
//            }
//
//            String statusCondition = "";
//            if (status != null) {
//                statusCondition = " AND a.`status` =" + (status ? 1 : 0);
//            }
//
//            String sql = "SELECT a.id, a.apper_name,s.district_name,a.street_name,a.tel,a.regulate_date_start,a.regulate_date_end," +
//                    "(SELECT count(id) FROM notice_send_detail WHERE apper_id=a.id AND `status`=0) as unread_notice" +
//                    " FROM apper AS a,street as s WHERE a.street_id = s.id" + statusCondition + apperNamesCondition +
//                    streetCondition + " ORDER BY s.district_name,a.street_name,a.apper_name";
//            System.out.println(sql);
//            IApper apper = (IApper) Class.forName(apperClassPath + ".Apper").newInstance();
//
//            apperList = preparedSql.select(sql, apper, (Object[]) null);
//            if (apperList != null) {
//                for (int i = apperList.size() - 1; i >= 0; i--) {
//                    if (apperList.get(i).getUnreadNotice() == 0 && !allowEmpty) {
//                        apperList.remove(i);
//                    }
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperList, error);
//    }
//
//    public static Result<Boolean> clearUnreadNotifications(String apperClassPath, Integer apperId, String operator) {
//        lazyInitDB(apperClassPath);
//        Boolean result = null;
//        String error = null;
//        try {
//            INoticeSendDetail noticeSendDetail = (INoticeSendDetail) Class.forName(apperClassPath + ".NoticeSendDetail").newInstance();
//            noticeSendDetail.setApperId(apperId);
//            noticeSendDetail.setStatus(0);
//            Condition condition = new ConditionImpl();
//            condition.set("status", 1);
//            condition.set("modifier", operator);
//            int update = suidRich.update(noticeSendDetail, condition);
//            if (update > 0) {
//                result = true;
//            } else {
//                error = "未读通知不存在";
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<IApperRiskAssess>> getExportRiskAssessList(String apperClassPath,
//                                                                         String apperIds) {
//        lazyInitDB(apperClassPath);
//        List<IApperRiskAssess> apperRiskAssessList = null;
//        String error = null;
//        try {
//            String sql = "SELECT * FROM apper_risk_assess WHERE apper_id IN(" + apperIds + ")";
//            System.out.println(sql);
//            IApperRiskAssess apperRiskAssess = (IApperRiskAssess) Class.forName(apperClassPath + ".ApperRiskAssess").newInstance();
//            apperRiskAssessList = preparedSql.select(sql, apperRiskAssess, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperRiskAssessList, error);
//    }
//
//    public static Result<List<IApperRiskAssess>> getApperRiskAssessList(String apperClassPath, Integer apperId) {
//        lazyInitDB(apperClassPath);
//        List<IApperRiskAssess> apperRiskAssessList = null;
//        String error = null;
//        try {
//            String sql = "SELECT * FROM apper_risk_assess WHERE apper_id=" + apperId + " ORDER BY stage,closed_time";
//            System.out.println(sql);
//            IApperRiskAssess apperRiskAssess = (IApperRiskAssess) Class.forName(apperClassPath + ".ApperRiskAssess").newInstance();
//            apperRiskAssessList = preparedSql.select(sql, apperRiskAssess, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(apperRiskAssessList, error);
//    }
//
//    public static Result<Boolean> deleteApperRiskAssess(String apperClassPath, Integer assessId) {
//        lazyInitDB(apperClassPath);
//        Boolean result = null;
//        String error = null;
//        try {
//            Class clazz = Class.forName(apperClassPath + ".ApperRiskAssess");
//            int delete = suidRich.deleteById(clazz, assessId);
//            if (delete == 1) result = true;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<IApperRiskAssess> updateRiskAssess(String apperClassPath,
//                                                            IApperRiskAssess riskAssess,
//                                                            Timestamp newClosedTime,
//                                                            String operator) {
//        lazyInitDB(apperClassPath);
//        IApperRiskAssess result = null;
//        String error = null;
//        try {
//            IApperRiskAssess assess = (IApperRiskAssess) Class.forName(apperClassPath + ".ApperRiskAssess").newInstance();
//            assess.setId(riskAssess.getId());
//            assess.setClosedTime(newClosedTime);
//            assess.setModifier(operator);
//            int update = suidRich.updateById(assess, null);
//            if (update == 1) {
//                riskAssess.setClosedTime(newClosedTime);
//                riskAssess.setModifier(operator);
//                result = riskAssess;
//            }
//
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<IApperRiskAssess> insertRiskAssess(String user,
//                                                            String apperClassPath,
//                                                            IApper apper,
//                                                            Integer stage,
//                                                            Timestamp closedTime) {
//        lazyInitDB(apperClassPath);
//        IApperRiskAssess result = null;
//        String error = null;
//        try {
//            IApperRiskAssess riskAssess = (IApperRiskAssess) Class.forName(apperClassPath + ".ApperRiskAssess").newInstance();
//            riskAssess.setStage(stage);
//            riskAssess.setStreetId(apper.getStreetId());
//            riskAssess.setStreetName(apper.getStreetName());
//            riskAssess.setDistrictName(apper.getDistrictName());
//            riskAssess.setApperId(apper.getId());
//            riskAssess.setApperName(apper.getApperName());
//            riskAssess.setSex(apper.getSex());
//            riskAssess.setAge(apper.getAge());
//            riskAssess.setIdCard(apper.getIdCard());
//            riskAssess.setTel(apper.getTel());
//            riskAssess.setNation(apper.getNation());
//            riskAssess.setNativePlace(apper.getNativePlace());
//            riskAssess.setCauseOfAction(apper.getCharge());
//            riskAssess.setRegulateLevel(apper.getRegulateLevel());
//            riskAssess.setRegulateType(apper.getRegulateType());
//            riskAssess.setRegulateDateStart(apper.getRegulateDateStart());
//            riskAssess.setRegulateDateEnd(apper.getRegulateDateEnd());
//            riskAssess.setAssessStatus(0);
//            riskAssess.setClosedTime(closedTime);
//            riskAssess.setCreator(user);
//            riskAssess.setCreateTime(new Timestamp(new Date().getTime()));
//            int insert = (int) suidRich.insertAndReturnId(riskAssess);
//            if (insert > 0) {
//                riskAssess.setId(insert);
//                result = riskAssess;
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<IApperSignLog> replaceSignLogImage(String apperClassPath, IApperSignLog signLog, String operator) {
//        lazyInitDB(apperClassPath);
//        IApperSignLog result = null;
//        String error = null;
//        try {
//            IApperSignLog apperSignLog = (IApperSignLog) Class.forName(apperClassPath + ".ApperSignLog").newInstance();
//            apperSignLog.setId(signLog.getId());
//            apperSignLog.setActImgUrl(signLog.getRegulateBasePhoto());
//            apperSignLog.setModifier(operator);
//            int update = suidRich.updateById(apperSignLog, null);
//            if (update == 1) {
//                signLog.setActImgUrl(signLog.getRegulateBasePhoto().replaceFirst("http:", "https:"));
//                signLog.setModifier(operator);
//                result = signLog;
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<SyncDeviceJson>> getSyncDeviceFailedApperList(String apperClassPath,
//                                                                            String redisServer,
//                                                                            Integer redisPort,
//                                                                            String redisPasswd) {
//        lazyInitDB(apperClassPath);
//        List<SyncDeviceJson> result = new ArrayList<>();
//        List<IDisruptorFailLog> failList = null;
//        String error = null;
//        try {
//            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            String sql = "SELECT * FROM disruptor_fail_log WHERE event_type = 5 and create_time >= '" + today + "' ORDER BY create_time asc";
//            System.out.println(sql);
//            IDisruptorFailLog disruptorFailLog = (IDisruptorFailLog) Class.forName(apperClassPath + ".DisruptorFailLog").newInstance();
//            failList = preparedSql.select(sql, disruptorFailLog, (Object[]) null);
//            if (failList != null && !failList.isEmpty()) {
//                HashMap<Integer, SyncDeviceJson> tempMap = new HashMap<>();
//                for (IDisruptorFailLog item : failList) {
//                    try {
//                        JsonObject syncObject = new JsonParser().parse(item.getDataJson()).getAsJsonObject();
//                        int userId = syncObject.get("userId").getAsInt();
//                        String userName = syncObject.get("userName").getAsString();
//                        String batchNum = syncObject.get("batchNum").getAsString();
//                        String deviceId = batchNum.substring(batchNum.lastIndexOf(":") + 1);
//                        SyncDeviceJson syncDeviceJson = new SyncDeviceJson(userId, userName, deviceId, item.getErrorMsg());
//                        tempMap.put(userId, syncDeviceJson);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (!tempMap.isEmpty()) {
//                    try {
//                        Jedis jedis = new Jedis(redisServer, redisPort == null ? 6379 : redisPort);
//                        if (redisPasswd != null && !redisPasswd.isEmpty()) {
//                            jedis.auth(redisPasswd);
//                        }
//                        String redisData = jedis.get("apper_modify_device_id");
//                        if (redisData != null && !redisData.isEmpty()) {
//                            JsonArray deviceIdJsonArray = new JsonParser().parse(redisData).getAsJsonArray().get(1).getAsJsonArray();
//                            HashMap<Integer, String> deviceIdMap = new HashMap<>();
//                            for (JsonElement e : deviceIdJsonArray) {
//                                JsonObject item = e.getAsJsonArray().get(1).getAsJsonObject();
//                                deviceIdMap.put(item.get("apperId").getAsInt(), item.get("newDeviceId").getAsString());
//                            }
//                            tempMap.forEach((k, v) -> {
//                                String newDeviceId = deviceIdMap.get(k);
//                                if (newDeviceId != null && !newDeviceId.equals(v.getDeviceIdFromDevice())) {
//                                    v.setDeviceIdFromRedis(newDeviceId);
//                                    result.add(v);
//                                }
//                            });
//                        }
//                        jedis.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<List<TxVideo>> getTxVideoList(String apperClassPath, Integer streamId) {
//        lazyInitDB(apperClassPath);
//        List<TxVideo> result = new ArrayList<>();
//        String error = null;
//        try {
//            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            String sql = "SELECT * FROM tx_video WHERE stream_id = '" + streamId + "' ORDER BY create_time asc";
//            System.out.println(sql);
//            TxVideo txVideo = (TxVideo) Class.forName(apperClassPath + ".TxVideo").newInstance();
//            result = preparedSql.select(sql, txVideo, (Object[]) null);
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<Boolean> setVideoVisitFileId(String apperClassPath,
//                                                      Integer visitRecordId,
//                                                      String fileId,
//                                                      String operator) {
//        lazyInitDB(apperClassPath);
//        boolean result = false;
//        String error = null;
//        try {
//            IVisitRecord record = (IVisitRecord) Class.forName(apperClassPath + ".VisitRecord").newInstance();
//            record.setId(visitRecordId);
//            record.setFileId(fileId);
//            record.setModifier(operator);
//            int update = suidRich.updateById(record, null);
//            if (update == 1) {
//                result = true;
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }
//
//    public static Result<Boolean> updateVideoVisitContent(String apperClassPath,
//                                                          Integer visitRecordId,
//                                                          List<String> newValues) {
//        lazyInitDB(apperClassPath);
//        boolean result = false;
//        String error = null;
//        try {
//            IVisitEvaluate evaluate = (IVisitEvaluate) Class.forName(apperClassPath + ".VisitEvaluate").newInstance();
//            evaluate.setRecordId(visitRecordId);
//            Condition condition = new ConditionImpl();
//            condition.set("work_unit", newValues.get(0));
//            condition.set("visit_reason", newValues.get(1));
//            condition.set("remark", newValues.get(2));
//            condition.set("place", newValues.get(3));
//            condition.set("personnel", newValues.get(4));
//            int update = suidRich.update(evaluate, condition);
//            if (update == 1) result = true;
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
//            error = e.getMessage();
//        }
//        return new Result<>(result, error);
//    }

    public static Result<Version> getSystemVersion() {
        String versionClassPath = "com.emillics.lemon.model.Version";
        List<Version> versionList = null;
        String error = null;
        try {
            String sql = "SELECT * FROM version ORDER BY id DESC LIMIT 1";
            System.out.println(sql);
            Version version = (Version) Class.forName(versionClassPath).newInstance();
            versionList = preparedSql.select(sql, version, (Object[]) null);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
        return new Result<>(versionList == null || versionList.size() == 0 ? null : versionList.get(0), error);
    }
}
