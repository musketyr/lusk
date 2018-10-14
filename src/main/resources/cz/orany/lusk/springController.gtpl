package $pkg;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class $controllerName {

    private final $beanName $beanPropertyName;

    public $controllerName($beanName $beanPropertyName) {
        this.$beanPropertyName = $beanPropertyName;
    }

    @RequestMapping("/${beanPropertyName - 'Service'}")
    public String index() {
        return ${beanPropertyName}.getGreeting();
    }

}
