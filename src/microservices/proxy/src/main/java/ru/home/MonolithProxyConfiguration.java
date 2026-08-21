package ru.home;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static ru.home.App.MOVIES_PATHS;

@Configuration
@ConditionalOnProperty(
        name = "proxy.migration-on",
        havingValue = "false",
        matchIfMissing = true
)
public class MonolithProxyConfiguration {
    @Bean
    private RouterFunction<ServerResponse> monolithUpstreamRoute(ProxyProperties proxy) {
        return route("proxy-all-to-monolith")
                .route(
                        path(MOVIES_PATHS),
                        http(proxy.getUpstreamA().getUri())
                )
                .build();
    }
}
