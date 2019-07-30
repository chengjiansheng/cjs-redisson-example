package com.cjs.example.lock.service;

import com.cjs.example.lock.model.CourseModel;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
public interface CourseService {

    CourseModel getById(Integer courseId);

    void upload(Integer userId, Integer courseId, Integer studyProcess);
}
