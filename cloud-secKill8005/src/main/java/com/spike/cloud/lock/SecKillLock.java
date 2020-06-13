package com.spike.cloud.lock;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author spike
 * @Date: 2020-06-08 01:25
 */
@Slf4j
@Component
public class SecKillLock {
    @Autowired
    private RedisTemplate redisTemplate;
    private DefaultRedisScript<String> unlockScript;
    private DefaultRedisScript<String> lockScript;
    private static final String lockText = "if redis.call('set',KEYS[1],KEYS[2],'NX','PX',KEYS[3]) then \n"+
            "    return '1'\n"+
            "else\n"+
            "    return '0'\n"+
            "end";
    private static final String unlockText = "if redis.call('get',KEYS[1]) == KEYS[2] then\n" +
            "    return tostring(redis.call('del',KEYS[1]))\n" +
            "else\n" +
            "    return '0'\n" +
            "end\n";

    @PostConstruct
    public void init() {
        lockScript = new DefaultRedisScript<>();
        lockScript.setResultType(String.class);
        unlockScript = new DefaultRedisScript<>();
        unlockScript.setResultType(String.class);

        // load lua script
        lockScript.setScriptText(lockText);
        unlockScript.setScriptText(unlockText);
    }

    public boolean lock(String key, String value, String expireTime) {
        try {
            List<String> keyList = new ArrayList<>();
            keyList.add(key);
            keyList.add(value);
            keyList.add(expireTime);
            // lock
            String res = String.valueOf(redisTemplate.execute(lockScript, keyList, keyList));
            if (res != null && res.equals("1")) {
                return true;
            } else {
//                log.info("lock fail!");
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean unlock(String key, String value) {
        List<String> keyList = new ArrayList<>();
        keyList.add(key);
        keyList.add(value);
        // unlock
        String res = String.valueOf(redisTemplate.execute(unlockScript, keyList, keyList));
//        log.info("unlock result: " + res);
        if (res != null && res.equals("1")) {
//            log.info("unlock!");
            return true;
        } else {
//            log.info("unlock fail!");
            return false;
        }
    }
}
