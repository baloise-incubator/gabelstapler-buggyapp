package ch.baloise.observability.gabelstaplerbuggyapp;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporterBuilder;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.otel.pyroscope.PyroscopeOtelConfiguration;
import io.otel.pyroscope.PyroscopeOtelSpanProcessor;
import io.otel.pyroscope.shadow.http.Format;
import io.otel.pyroscope.shadow.javaagent.EventType;
import io.otel.pyroscope.shadow.javaagent.PyroscopeAgent;
import io.otel.pyroscope.shadow.javaagent.api.Logger;
import io.otel.pyroscope.shadow.javaagent.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.tracing.otlp.OtlpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties(OtlpProperties.class)
public class OtlpConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${pyroscope.url}")
    private String pyroscopeUrl;

    @Value("${pyroscope.frontend-url}")
    private String pyroscopeFrontendUrl;

    @Bean
    Config pyroscopeConfig() {
        return new Config.Builder()
                .setApplicationName(applicationName)
                .setProfilingEvent(EventType.ITIMER)
                .setFormat(Format.JFR)
                .setLogLevel(Logger.Level.DEBUG)
                .setServerAddress(pyroscopeUrl)
                .build();
    }

    @Bean
    SpanProcessor pyroscopeSpanProcessor(Config pyroscopeConfig) {
        PyroscopeAgent.start(
                new PyroscopeAgent.Options.Builder(pyroscopeConfig)
                        .build()
        );

        PyroscopeOtelConfiguration pyroscopeOtelConfig = new PyroscopeOtelConfiguration.Builder()
                .setAppName(pyroscopeConfig.applicationName + "." + pyroscopeConfig.profilingEvent.id)
                .setPyroscopeEndpoint(pyroscopeFrontendUrl)
                .setAddProfileURL(true)
                .setAddSpanName(true)
                .setAddProfileBaselineURLs(true)
                .build();
        return new PyroscopeOtelSpanProcessor(pyroscopeOtelConfig);
    }

    // OtlpAutoConfiguration use HTTP by default, we update it to use  GRPC
    // https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/tracing/otlp/OtlpAutoConfiguration.java
    @Bean
    public OtlpGrpcSpanExporter otlpExporter(final OtlpProperties properties) {

        final OtlpGrpcSpanExporterBuilder builder =
                OtlpGrpcSpanExporter.builder()
                        .setEndpoint(properties.getEndpoint())
                        .setTimeout(properties.getTimeout())
                        .setCompression(String.valueOf(properties.getCompression()).toLowerCase());

        for (final Map.Entry<String, String> header : properties.getHeaders().entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }

        return builder.build();
    }

}
