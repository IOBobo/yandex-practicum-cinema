package ru.home;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Locale;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.weight;
import static org.springframework.web.servlet.function.RequestPredicates.path;


//PORT: 8000
//MONOLITH_URL: http://monolith:8080
//        #монолит
//MOVIES_SERVICE_URL: http://movies-service:8081 #сервис movies
//EVENTS_SERVICE_URL: http://events-service:8082
//GRADUAL_MIGRATION: "true" # вкл/выкл простого фиче-флага
//MOVIES_MIGRATION_PERCENT: "50" # процент миграции
@Configuration
public class ProxyConfiguration {

    private static final String MOVIES_PATHS = "/api/movies/**";
    private static final String WEIGHT_GROUP = "movies-traffic";

    @Bean
    public RouterFunction<ServerResponse> proxyRoutes(ProxyProperties proxy) {
        System.out.println();
        if(proxy.isMigrationOn()) {
            return migrationRoutes(proxy);
        }
        return monolithUpstreamRoute(proxy);
    }

    private RouterFunction<ServerResponse> monolithUpstreamRoute(ProxyProperties proxy) {
        return route("proxy-all-to-monolith")
                .route(
                        path(MOVIES_PATHS),
                        http(proxy.getUpstreamA().getUri())
                )
                .build();
    }

    private RouterFunction<ServerResponse> migrationRoutes(ProxyProperties proxy) {

        return route("proxy-all-weighted-migration")
                .route(
                        path(MOVIES_PATHS).and(
                                weight(WEIGHT_GROUP, 100 - proxy.getWeight())
                        ),
                        http(proxy.getUpstreamA().getUri())
                )
                .route(
                        path(MOVIES_PATHS).and(
                                weight(WEIGHT_GROUP, proxy.getWeight())
                        ),
                        http(proxy.getUpstreamB().getUri())
                )
                .build();
    }
}