package $pkg;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;

@Controller("/$beanPropertyName")
public class $controllerName {

    private final $beanName $beanPropertyName;

    private $controllerName($beanName $beanPropertyName) {
        this.$beanPropertyName = $beanPropertyName;
    }

    @Get("/")
    public String index() {
        return ${beanPropertyName}.getGreeting();
    }
}
