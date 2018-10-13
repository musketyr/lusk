package cz.orany.lusk

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class LuskSpec extends Specification {

    @Rule TemporaryFolder tmp

    void 'generate sources'() {
        File folder = tmp.newFolder()
        given:
            Lusk lusk = new Lusk(folder, framework, 'cloud.winterboots.hello')
            int count = 1000
        when:
            lusk.generate(count)
            File packageFolder = new File(folder, "cloud/winterboots/hello")
        then:
            packageFolder.listFiles().findAll { it.name.endsWith('Service.java') }.size() == count
            packageFolder.listFiles().findAll { it.name.endsWith('Controller.java') }.size() == count
	where:
	    framework << Lusk.Framework.values()
    }
}
