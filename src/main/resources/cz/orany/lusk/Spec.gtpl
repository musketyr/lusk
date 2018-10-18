package $pkg

import com.agorapulse.gru.Gru
import com.agorapulse.gru.http.Http
import org.junit.Rule
import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
// for manual execution only, skip for CI
@Requires({ !System.getenv('CI') })
class HttpSpec extends Specification {

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
