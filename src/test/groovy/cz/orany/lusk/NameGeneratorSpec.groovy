package cz.orany.lusk

import spock.lang.Specification

class NameGeneratorSpec extends Specification {

    void 'generate bean names'() {
        given:
            int count = 10
        when:
            Set<String> names = NameGenerator.generateBeanNames(count)
        then:
            names.size() == count
            names.unique().size() == count
            names.every { it.endsWith('Service') }
    }
}
