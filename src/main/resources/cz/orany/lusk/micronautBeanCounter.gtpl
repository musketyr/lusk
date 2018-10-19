package ${pkg};

import io.micronaut.context.ApplicationContext;
import io.micronaut.discovery.event.ServiceStartedEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class BeanCounter {

    private static final Logger LOG = LoggerFactory.getLogger(BeanCounter.class);

    private final ApplicationContext context;

    public BeanCounter(ApplicationContext context) {
        this.context = context;
    }

    @Async
    @EventListener
    void onStartup(ServiceStartedEvent ignored) {
        LOG.info("Loaded " + context.getAllBeanDefinitions().size() + " Micronaut beans");
    }

}
