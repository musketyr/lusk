package cz.orany.lusk

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class FrameworkDetectorSpec extends Specification {

    @Rule TemporaryFolder tmp

    void 'detect micronaut'() {
        given:
            File projectRoot = tmp.newFolder()
            File appClassDir = new File(projectRoot, 'src/main/java/com/example/mn')
            appClassDir.mkdirs()
            File classFile = new File(appClassDir, 'Application.java')

            // language=Java
            classFile.text = '''
                package com.example.mn;
                
                import io.micronaut.runtime.Micronaut;
                
                public class Application {
                
                    public static void main(String[] args) {
                        Micronaut.run(Application.class);
                    }
                }
            '''.stripIndent().trim()
        when:
            Hint hint = new FrameworkDetector(projectRoot, 'src/main/java').detect()
        then:
            hint.framework == Lusk.Framework.micronaut
            hint.pkg == 'com.example.mn'
    }

    void 'detect spring'() {
        given:
            File projectRoot = tmp.newFolder()
            File appClassDir = new File(projectRoot, 'src/main/java/com/example/mn')
            appClassDir.mkdirs()
            File classFile = new File(appClassDir, 'Application.java')

            // language=Java
            classFile.text = '''
                package com.example.sp;
                
                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;
                
                @SpringBootApplication
                public class Application {
                
                    public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                    }
                }
            '''.stripIndent().trim()
        when:
            Hint hint = new FrameworkDetector(projectRoot, 'src/main/java').detect()
        then:
            hint.framework == Lusk.Framework.spring
            hint.pkg == 'com.example.sp'
    }

    void 'fail to detect'() {
        given:
            File projectRoot = tmp.newFolder()
        when:
            Hint hint = new FrameworkDetector(projectRoot, 'src/main/java').detect()
        then:
            !hint
    }

}
