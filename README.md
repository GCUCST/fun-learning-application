# 读我
#### fun-learning-application
#### 本项目用于学习，部分代码来源各大网站，侵删

This repository summaries some tools'usage with a small demo and text detail.

#### commands
#### find Effective pom
mvn help:effective-pom

#### -P<profile_id> to active profile
mvn test -Ptest 

### use archetype to generate a project
mvn archetype:generate 

### use to generate document
mvn site 


##
<build>
  <pluginManagement>
      <plugins>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-site-plugin</artifactId>
            <version>3.3</version>
          </plugin>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-project-info-reports-plugin</artifactId>
            <version>2.7</version>
          </plugin>
      </plugins>
      </pluginManagement>
  </build>
  ##

#https://www.runoob.com/maven/maven-build-test-project.html

#如果出现 执行 goal fmt失败
可以修复：mvn com.coveo:fmt-maven-plugin:format