package cz.orany.lusk

import spock.lang.Specification

class CodeGeneratorSpec extends Specification {

    private static final String beanTemplate = '''
            package ${pkg};
            
            import javax.inject.Singleton;
            
            @Singleton
            public class ${beanName} {
            
               public ${beanName}(${dependenciesArgsList}) { }
            
            }
            '''.stripIndent().trim()

    private static final String controllerTemplate = '''
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

    private static final String beanCounterTemplate = '''
            package $pkg;
            
            import io.micronaut.context.ApplicationContext;
            import io.micronaut.discovery.event.ServiceStartedEvent;
            import io.micronaut.runtime.event.annotation.EventListener;
            import io.micronaut.scheduling.annotation.Async;
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;
            
            import javax.inject.Singleton;
            
            @Singleton
            public class BeanCounter {
            
                private static final Logger LOG = LoggerFactory.getLogger(BeanCounter.class);
            
                private final ApplicationContext context;
            
                public BeanCounter(ApplicationContext context) {
                    this.context = context;
                }
            
                @Async
                @EventListener
                void onStartup(ServiceStartedEvent ignored) {
                    LOG.info("Loaded " + context.getAllBeanDefinitions().size() + " Micronaut beans");
                }
            
            }
            '''.stripIndent().trim()

    private static final String specTemplate = '''
            package $pkg

            import com.agorapulse.gru.Gru
            import com.agorapulse.gru.http.Http
            import org.junit.Rule
            import spock.lang.Specification
            import spock.lang.Unroll
            
            @Unroll
            class HttpSpec extends Specification{
            
                @Rule Gru<Http> gru = Gru.equip(Http.steal(this)).prepare('http://localhost:8080')
            
                void 'test #name'() {
                    expect:
                        gru.test {
                            get "/\\$name"
                            expect {
                                text inline("Hello \\$serviceName")
                            }
                        }
                    where:
                        name << [${simpleNames.collect{ '"' + it + '"'}.join(', ')}]
                        serviceName << [${serviceNames.collect{ '"' + it + '"'}.join(', ')}]
                }
            }

            '''.stripIndent().trim()

    // language=Java
    private static final String expectedBeanCode = '''
            package com.example.beans;
            
            import javax.inject.Singleton;
            
            @Singleton
            public class MyService {
            
               public MyService(OtherService otherService, AnotherService anotherService) { }
            
            }
            '''.stripIndent().trim()

    // language=Java
    private static final String expectedControllerCode = '''
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

    // language=Java
    private static final String expectedBeanCounterTemplate = '''
            package com.example.beans;
            
            import io.micronaut.context.ApplicationContext;
            import io.micronaut.discovery.event.ServiceStartedEvent;
            import io.micronaut.runtime.event.annotation.EventListener;
            import io.micronaut.scheduling.annotation.Async;
            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;
            
            import javax.inject.Singleton;
            
            @Singleton
            public class BeanCounter {
            
                private static final Logger LOG = LoggerFactory.getLogger(BeanCounter.class);
            
                private final ApplicationContext context;
            
                public BeanCounter(ApplicationContext context) {
                    this.context = context;
                }
            
                @Async
                @EventListener
                void onStartup(ServiceStartedEvent ignored) {
                    LOG.info("Loaded " + context.getAllBeanDefinitions().size() + " Micronaut beans");
                }
            
            }
            '''.stripIndent().trim()

    // language=Groovy
    private static final String expectedSpecTemplate = '''
            package com.example.beans

            import com.agorapulse.gru.Gru
            import com.agorapulse.gru.http.Http
            import org.junit.Rule
            import spock.lang.Specification
            import spock.lang.Unroll
            
            @Unroll
            class HttpSpec extends Specification{
            
                @Rule Gru<Http> gru = Gru.equip(Http.steal(this)).prepare('http://localhost:8080')
            
                void 'test #name'() {
                    expect:
                        gru.test {
                            get "/$name"
                            expect {
                                text inline("Hello $serviceName")
                            }
                        }
                    where:
                        name << ["my", "other", "another"]
                        serviceName << ["MyService", "OtherService", "AnotherService"]
                }
            }

            '''.stripIndent().trim()

    void 'generate template'() {
        given:

        when:

            CodeGenerator codeGenerator = new CodeGenerator(beanTemplate, controllerTemplate, beanCounterTemplate, specTemplate)
        then:

            codeGenerator.generateBean('com.example.beans', 'MyService', ['OtherService', 'AnotherService'] as Set<String>) == expectedBeanCode
            codeGenerator.generateController('com.example.beans', 'MyService') == expectedControllerCode
            codeGenerator.generateBeanCounter('com.example.beans') == expectedBeanCounterTemplate
            codeGenerator.generateSpec('com.example.beans', ['MyService', 'OtherService', 'AnotherService'] as Set<String>) == expectedSpecTemplate
    }

}
