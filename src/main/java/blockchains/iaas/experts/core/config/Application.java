package blockchains.iaas.experts.core.config;


import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class Application extends ResourceConfig {
    public Application() {
        packages("blockchains.iaas.experts.core");
        register(ObjectMapperProvider.class);
        register( JacksonFeature.class );

    }
}
