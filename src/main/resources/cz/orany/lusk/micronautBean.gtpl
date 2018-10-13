package ${pkg};

import javax.inject.Singleton;

@Singleton
public class ${beanName} {

    public ${beanName}(${dependenciesArgsList}) { }

    public String getGreeting() { return "Hello $beanName"; }
}
