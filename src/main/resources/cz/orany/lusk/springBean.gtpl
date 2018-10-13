package ${pkg};

import org.springframework.stereotype.Service;

@Service
public class ${beanName} {

   public ${beanName}(${dependenciesArgsList}) { }

   public String getGreeting() { return "Hello $beanName"; }

}
