package cz.orany.lusk

import spock.lang.Specification

class NameGeneratorSpec extends Specification {

    void 'generate bean names'() {
        given:
            NameGenerator generator = new NameGenerator()
        when:
            Set<String> names = generator.generateBeanNames(1000)
        then:
            names.size() == 1000
            names.unique().size() == 1000
            names.every { it.endsWith('Service') }
    }

    void 'generate factory names'() {
        given:
            NameGenerator generator = new NameGenerator()
        when:
            String name = generator.generateFactoryName()
        then:
            name
            name.endsWith('Factory')
            name != 'Factory'
    }
}
