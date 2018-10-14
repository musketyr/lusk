package $pkg

import com.agorapulse.gru.Gru
import com.agorapulse.gru.http.Http
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class HttpSpec extends Specification {

    /* TODO: include following into your build.gradle script
        repositories {
            maven { url "https://jcenter.bintray.com" }
        }

        dependencies {
            testCompile "com.agorapulse:gru-http:0.6.6"
        }
     */

    @Rule Gru<Http> gru = Gru.equip(Http.steal(this)).prepare('http://localhost:8080')

    void 'test #name'() {
        given:
            String theUrl = '/' + name
            String theMessage = 'Hello ' + serviceName
        expect:
            gru.test {
                get theUrl
                expect {
                    text inline(theMessage)
                }
            }
        where:
            name << [${simpleNames.collect{ '"' + it + '"'}.join(', ')}]
            serviceName << [${serviceNames.collect{ '"' + it + '"'}.join(', ')}]
    }
}
