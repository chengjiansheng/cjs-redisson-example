package com.cjs.example.lock.service.impl;

import com.alibaba.fastjson.JSON;
import com.cjs.example.lock.constant.RedisKeyPrefixConstant;
import com.cjs.example.lock.model.CourseModel;
import com.cjs.example.lock.model.CourseRecordModel;
import com.cjs.example.lock.repository.CourseRecordRepository;
import com.cjs.example.lock.repository.CourseRepository;
import com.cjs.example.lock.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseRecordRepository courseRecordRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public CourseModel getById(Integer courseId) {

        CourseModel courseModel = null;

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        String value = hashOperations.get(RedisKeyPrefixConstant.COURSE, String.valueOf(courseId));

        if (StringUtils.isBlank(value)) {
            String lockKey = RedisKeyPrefixConstant.LOCK_COURSE + courseId;
            RLock lock = redissonClient.getLock(lockKey);
            try {
                boolean res = lock.tryLock(10, TimeUnit.SECONDS);
                if (res) {
                    value = hashOperations.get(RedisKeyPrefixConstant.COURSE, String.valueOf(courseId));
                    if (StringUtils.isBlank(value)) {
                        log.info("从数据库中读取");
                        courseModel = courseRepository.findById(courseId).orElse(null);
                        hashOperations.put(RedisKeyPrefixConstant.COURSE, String.valueOf(courseId), JSON.toJSONString(courseModel));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else {
            log.info("从缓存中读取");
            courseModel = JSON.parseObject(value, CourseModel.class);
        }

        return courseModel;
    }

    @Override
    public void upload(Integer userId, Integer courseId, Integer studyProcess) {

        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        String cacheKey = RedisKeyPrefixConstant.COURSE_PROGRESS + ":" + userId;
        String cacheValue = hashOperations.get(cacheKey, String.valueOf(courseId));
        if (StringUtils.isNotBlank(cacheValue) && studyProcess <= Integer.valueOf(cacheValue)) {
            return;
        }

        String lockKey = "upload:" + userId + ":" + courseId;

        RLock lock = redissonClient.getLock(lockKey);

        try {
            lock.lock(10, TimeUnit.SECONDS);

            cacheValue = hashOperations.get(cacheKey, String.valueOf(courseId));
            if (StringUtils.isBlank(cacheValue) || studyProcess > Integer.valueOf(cacheValue)) {
                CourseRecordModel model = new CourseRecordModel();
                model.setUserId(userId);
                model.setCourseId(courseId);
                model.setStudyProcess(studyProcess);
                courseRecordRepository.save(model);
                hashOperations.put(cacheKey, String.valueOf(courseId), String.valueOf(studyProcess));
            }

        } catch (Exception ex) {
            log.error("获取所超时！", ex);
        } finally {
            lock.unlock();
        }

    }
}
