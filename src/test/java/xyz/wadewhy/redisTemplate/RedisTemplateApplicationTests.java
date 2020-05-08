package xyz.wadewhy.redisTemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import xyz.wadewhy.redisTemplate.config.RedisTemplate;;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    // =========================Common=================================//

    // =========================String=================================//

    @Test
    public boolean set() {
        String key = "key1";
        Object value = "v1";
        int indexdb = 1;
        redisTemplate.indexdb.set(indexdb);
        return false;

    }
}
