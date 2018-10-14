package cz.orany.lusk

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class LuskSpec extends Specification {

    @Rule TemporaryFolder tmp

    void 'generate sources'() {
        given:
            File folder = tmp.newFolder()
            Lusk lusk = new Lusk(folder, framework, 'cloud.winterboots.hello')
            int count = 1000
        when:
            lusk.generate(count)
            File srcFolder = new File(folder, "src/main/java/cloud/winterboots/hello")
            File testFolder = new File(folder, "src/test/groovy/cloud/winterboots/hello")
        then:
            srcFolder.listFiles().findAll { it.name.endsWith('Service.java') }.size() == count
            srcFolder.listFiles().findAll { it.name.endsWith('Controller.java') }.size() == count
            new File(testFolder, 'HttpSpec.groovy').exists()
        where:
            framework << Lusk.Framework.values()
    }
}
