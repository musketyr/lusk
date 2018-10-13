package cz.orany.lusk

import spock.lang.Specification

class CodeGeneratorSpec extends Specification {

    void 'generate template'() {
        given:
            String beanTemplate = '''
            package ${pkg};
            
            import javax.inject.Singleton;
            
            @Singleton
            public class ${beanName} {
            
               public ${beanName}(${dependenciesArgsList}) { }
            
            }
            '''.stripIndent().trim()

            String controllerTemplate = '''
            package $pkg;

            import org.springframework.web.bind.annotation.RestController;
            import org.springframework.web.bind.annotation.RequestMapping;
            
            @RestController
            public class $controllerName {
            
                private final $beanName $beanPropertyName;
            
                public $controllerName($beanName $beanPropertyName) { }
            
                @RequestMapping("/$beanPropertyName")
                public String index() {
                    return "Greetings with $beanName!";
                }
            
            }
            '''.stripIndent().trim()
        when:
            // language=Java
            String expectedBeanCode = '''
            package com.example.beans;
            
            import javax.inject.Singleton;
            
            @Singleton
            public class MyService {
            
               public MyService(OtherService otherService, AnotherService anotherService) { }
            
            }
            '''.stripIndent().trim()

            // language=Java
            String expectedControllerCode = '''
            package com.example.beans;

            import org.springframework.web.bind.annotation.RestController;
            import org.springframework.web.bind.annotation.RequestMapping;
            
            @RestController
            public class MyController {
            
                private final MyService myService;
            
                public MyController(MyService myService) { }
            
                @RequestMapping("/myService")
                public String index() {
                    return "Greetings with MyService!";
                }
            
            }
            '''.stripIndent().trim()
            CodeGenerator codeGenerator = new CodeGenerator(beanTemplate, controllerTemplate)
        then:

            codeGenerator.generateBean('com.example.beans', 'MyService', ['OtherService', 'AnotherService'] as Set<String>) == expectedBeanCode
            codeGenerator.generateController('com.example.beans', 'MyService') == expectedControllerCode
    }

}
