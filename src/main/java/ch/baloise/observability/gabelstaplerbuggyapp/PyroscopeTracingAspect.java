package ch.baloise.observability.gabelstaplerbuggyapp;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.pyroscope.labels.LabelsSet;
import io.pyroscope.labels.Pyroscope;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
@EnableAspectJAutoProxy
@Slf4j
public class PyroscopeTracingAspect {

    @Autowired
    private Tracer tracer;

    @Around("@annotation(org.springframework.web.bind.annotation.RestController)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("before controller call => class: " + joinPoint.getSignature().getDeclaringType().getSimpleName() + ", method: " + joinPoint.getSignature().getName());
        String traceId = "unknown";
        String spanId = "unknown";
        Span span = tracer.currentSpan();
        if (span != null) {
            traceId = span.context().traceId();
            spanId = span.context().spanId();
        }
        return Pyroscope.LabelsWrapper.run(new LabelsSet("trace-id", traceId, "span-id", spanId), () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

}
