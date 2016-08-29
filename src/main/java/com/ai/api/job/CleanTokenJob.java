package com.ai.api.job;

import com.ai.api.dao.impl.TokenJWTDaoImpl;
import com.ai.api.service.AuthenticationService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.StringUtils;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.job
 * Creation Date   : 2016/8/29 12:29
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class CleanTokenJob extends QuartzJobBean {
    private static Logger TOKEN_JOB_LOGGER = Logger.getLogger("TOKEN_JOB_LOGGER");

//    @Value("${enable.token.job}")
    private String enableTokenJob="true";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TOKEN_JOB_LOGGER.info("Token job START");
        try {
            if (enableTokenJob.equalsIgnoreCase("true")) {
                TOKEN_JOB_LOGGER.info("Start ...");
                List<String> tokenList = RedisUtil.hvals("publicAPIToken");
                for (String token : tokenList) {
                    if (StringUtils.isNotBlank(token)) {
                        TOKEN_JOB_LOGGER.info("--------------------[" + token + "]");
                        String sessionId = JSON.parseObject(token).getString("id");
                        TOKEN_JOB_LOGGER.info("-----sessionId------[" + sessionId + "]");
                        Date validDate = new Date(JSON.parseObject(token).getLong("validBefore") * 1000);
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DATE, -30);
                        if (validDate.before(calendar.getTime())) {
                            TOKEN_JOB_LOGGER.info("[" + sessionId + "] EXPIRED!!");
                            Long count = RedisUtil.hdel("publicAPIToken",sessionId);
                            if (count > 0) {
                                TOKEN_JOB_LOGGER.info("TokenSession removed!!! [" + sessionId + "]");
                            } else {
                                TOKEN_JOB_LOGGER.info("Fail to remove [" + sessionId + "]");
                            }
                        }
                    }
                }
            } else {
                TOKEN_JOB_LOGGER.info("Token job was disabled.");
            }
            TOKEN_JOB_LOGGER.info("Token job DONE");
        }catch (Exception e){
            TOKEN_JOB_LOGGER.error("Error occurred！! ",e);
        }
    }
}
