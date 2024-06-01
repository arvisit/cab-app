package by.arvisit.cabapp.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import reactor.core.observability.DefaultSignalListener;
import reactor.core.publisher.Mono;

@Profile({ "dev", "itest" })
@Component
@RequiredArgsConstructor
public class TracerGlobalFilter implements GlobalFilter {

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .tap(() -> new DefaultSignalListener<>() {
                    @Override
                    public void doOnComplete() throws Throwable {
                        String traceId = tracer.currentTraceContext().context().traceId();
                        exchange.getResponse().getHeaders().add("traceId", traceId);
                    }
                });
    }

}
