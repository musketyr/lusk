package cz.orany.lusk

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class LuskSpec extends Specification {

    @Rule TemporaryFolder tmp

    void 'generate sources'() {
        given:
            File folder = tmp.newFolder()

            File buildFile = new File(folder, 'build.gradle')
            buildFile.text = '// build file'

            Lusk lusk = new Lusk(folder, framework as Lusk.Framework, 'cloud.winterboots.hello')

            int count = 1000
        when:
            lusk.generate(count)
            File srcFolder = new File(folder, "src/main/java/cloud/winterboots/hello")
            File testFolder = new File(folder, "src/test/groovy/cloud/winterboots/hello")
        then:
            srcFolder.listFiles().findAll { it.name.endsWith('Service.java') }.size() == count
            srcFolder.listFiles().findAll { it.name.endsWith('Controller.java') }.size() == count
            new File(testFolder, 'HttpSpec.groovy').exists()
            buildFile.text.contains('gru')
        where:
            framework << Lusk.Framework.values()
    }
}
