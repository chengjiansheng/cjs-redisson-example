package com.cjs.example.lock.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
@Data
@Entity
@Table(name = "t_user_course_record")
public class CourseRecordModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "study_process")
    private Integer studyProcess;

}
