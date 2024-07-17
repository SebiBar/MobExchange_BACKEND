package mobex.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
            info = @Info(
                    title = "MobExchange API",
                    version = "1.0",
                    description = "API for getting exchange rates and managing user authentication.",
                    license = @License(
                            name = "Apache 2.0",
                            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                    )
            ),
        servers =
            {
                @Server(url = "http://localhost:8080", description = "Local server"),
            }

)
public class OpenApiConfig {
}
