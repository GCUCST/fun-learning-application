package cn.cst.myspringboot;

import cn.cst.myspringboot.dao.CourseMapper;
import cn.cst.myspringboot.pojo.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MySpringbootApplicationTests {


    @Autowired
    CourseMapper courseMapper;

    @Test
    void contextLoads() {
        for (int i = 0; i < 100; i++) {
            Course course = new Course();
            course.setCname("CST"+i);
            course.setUserId(100L+i);
            course.setCstatus("OK");
            courseMapper.insert(course);
        }

    }

}
