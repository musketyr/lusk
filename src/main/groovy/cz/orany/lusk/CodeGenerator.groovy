package cz.orany.lusk

import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.transform.CompileStatic

@CompileStatic
class CodeGenerator {

    private final SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
    private final Template beanTemplate;
    private final Template controllerTemplate;

    CodeGenerator(String beanTemplate, String controllerTemplate) {
        this.beanTemplate = templateEngine.createTemplate(beanTemplate)
        this.controllerTemplate = templateEngine.createTemplate(controllerTemplate)
    }

    String generateBean(String pkg, String beanName, Set<String> dependencies) {
        String dependenciesArgsList = dependencies.collect {
            "$it " + it[0].toLowerCase() + it[1..-1]
        }.join(', ')
        beanTemplate.make(pkg: pkg, beanName: beanName, dependencies: dependencies, dependenciesArgsList: dependenciesArgsList).toString()
    }

    String generateController(String pkg, String beanName) {
        controllerTemplate.make(pkg: pkg, beanName: beanName, controllerName: beanName - 'Service' + 'Controller', beanPropertyName: beanName[0].toLowerCase() + beanName[1..-1]).toString()
    }
}
