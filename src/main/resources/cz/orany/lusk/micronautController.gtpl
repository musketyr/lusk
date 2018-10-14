package $pkg;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.HttpStatus;

@Controller("/${beanPropertyName - 'Service'}")
public class $controllerName {

    private final $beanName $beanPropertyName;

    public $controllerName($beanName $beanPropertyName) {
        this.$beanPropertyName = $beanPropertyName;
    }

    @Get("/")
    public String index() {
        return ${beanPropertyName}.getGreeting();
    }
}
