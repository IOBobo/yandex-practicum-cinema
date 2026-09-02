package ru.home;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import static ru.home.App.MOVIES_PATHS;
import static ru.home.App.WEIGHT_GROUP;


//PORT: 8000
//MONOLITH_URL: http://monolith:8080
//        #монолит
//MOVIES_SERVICE_URL: http://movies-service:8081 #сервис movies
//EVENTS_SERVICE_URL: http://events-service:8082
//GRADUAL_MIGRATION: "true" # вкл/выкл простого фиче-флага
//MOVIES_MIGRATION_PERCENT: "50" # процент миграции
@Configuration
@ConditionalOnProperty(
        name = "proxy.migration-on",
        havingValue = "true",
        matchIfMissing = true
)
public class ProxyConfiguration {


    @Bean
    RouterFunction<ServerResponse> monolithMoviesRoute(ProxyProperties proxy) {
        int moviesServiceWeight = proxy.getWeight();
        int monolithWeight = 100 - moviesServiceWeight;

        return route("proxy-weight-monolith")
                .route(
                        path(MOVIES_PATHS)
                                .and(weight(WEIGHT_GROUP, monolithWeight)),
                        http(proxy.getUpstreamA().getUri())
                )
                .build();
    }


    @Bean
    RouterFunction<ServerResponse> moviesServiceRoute(ProxyProperties proxy) {
        int moviesServiceWeight = proxy.getWeight();

        return route("proxy-weight-movies-service")
                .route(
                        path(MOVIES_PATHS)
                                .and(weight(WEIGHT_GROUP, moviesServiceWeight)),
                        http(proxy.getUpstreamB().getUri())
                )
                .build();
    }

}