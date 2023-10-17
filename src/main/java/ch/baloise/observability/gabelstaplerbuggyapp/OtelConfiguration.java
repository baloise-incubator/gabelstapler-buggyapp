package ch.baloise.observability.gabelstaplerbuggyapp;

import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OtelConfiguration {
    @Bean
    SpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpHttpSpanExporter
                .builder()
                .setEndpoint(url)
                .build();
    }
}
