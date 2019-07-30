package com.cjs.example.lock.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
@Data
@Entity
@Table(name = "t_course")
public class CourseModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_type")
    private Integer courseType;

    @Column(name = "start_time")
    private Date startTime;
}
