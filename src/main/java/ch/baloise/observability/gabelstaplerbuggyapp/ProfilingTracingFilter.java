package ch.baloise.observability.gabelstaplerbuggyapp;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.pyroscope.labels.LabelsSet;
import io.pyroscope.labels.Pyroscope;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProfilingTracingFilter implements WebFilter {

    @Autowired
    Tracer tracer;

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {
        return Pyroscope.LabelsWrapper.run(new LabelsSet("trace-id", traceId, "span-id", spanId), () -> {
            String traceId = "unknown";
            String spanId = "unknown";
            Span span = tracer.currentSpan();
            if (span != null) {
                traceId = span.context().traceId();
                spanId = span.context().spanId();
            }
            log.info("Filter called with trace-id %s and span-id %s".formatted(traceId, spanId));
            return webFilterChain.filter(serverWebExchange);
        });
    }

}
