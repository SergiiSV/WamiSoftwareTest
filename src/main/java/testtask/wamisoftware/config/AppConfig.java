package testtask.wamisoftware.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "testtask.wamisoftware"
})
public class AppConfig {
}
