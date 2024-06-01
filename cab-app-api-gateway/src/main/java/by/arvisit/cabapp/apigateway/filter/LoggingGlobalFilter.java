package by.arvisit.cabapp.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.observability.DefaultSignalListener;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .tap(() -> new DefaultSignalListener<>() {
                    @Override
                    public void doOnSubscription() throws Throwable {
                        log.info("Incoming {} request to: {}", exchange.getRequest().getMethod(),
                                exchange.getRequest().getPath());
                    }
                });
    }

}
