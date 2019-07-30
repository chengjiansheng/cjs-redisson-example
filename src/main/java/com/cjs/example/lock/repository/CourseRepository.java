package com.cjs.example.lock.repository;

import com.cjs.example.lock.model.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
public interface CourseRepository extends JpaSpecificationExecutor<CourseModel>, JpaRepository<CourseModel, Integer> {
}
