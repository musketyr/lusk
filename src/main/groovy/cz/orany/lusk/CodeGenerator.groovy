package cz.orany.lusk

import groovy.text.SimpleTemplateEngine
import groovy.text.Template
import groovy.transform.CompileStatic

@CompileStatic
class CodeGenerator {

    private final SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
    private final Template beanTemplate
    private final Template controllerTemplate
    private final Template specTemplate

    CodeGenerator(String beanTemplate, String controllerTemplate, String specTemplate) {
        this.beanTemplate = templateEngine.createTemplate(beanTemplate)
        this.controllerTemplate = templateEngine.createTemplate(controllerTemplate)
        this.specTemplate = templateEngine.createTemplate(specTemplate)
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

    String generateSpec(String pkg, Set<String> names) {
        specTemplate.make(pkg: pkg, serviceNames: names, simpleNames: names.collect {
            String bare = it - 'Service'
            return bare[0].toLowerCase() + bare[1..-1]
        }).toString()
    }
}
