package cz.orany.lusk

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.util.environment.RestoreSystemProperties

@RestoreSystemProperties
class LuskAppSpec extends Specification {

    @Rule TemporaryFolder tmp

    void 'minimal input'() {
        given:
            File dir = tmp.newFolder()
            String pkg = 'org.example.pkg'
            Lusk.Framework framework = Lusk.Framework.micronaut

            System.setProperty('user.dir', dir.canonicalPath)

        when:
            LuskApp.main(
                '-p', pkg,
                '-f', framework.toString()
            )
        then:
            new File(dir, 'src/test/groovy/org/example/pkg/HttpSpec.groovy').exists()
    }


    void 'no input and no way to guess'() {
        given:
            File dir = tmp.newFolder()
            System.setProperty('user.dir', dir.canonicalPath)

        when:
            LuskApp.main()
        then:
            !new File(dir, 'src/test/groovy/org/example/pkg/HttpSpec.groovy').exists()
    }

}
