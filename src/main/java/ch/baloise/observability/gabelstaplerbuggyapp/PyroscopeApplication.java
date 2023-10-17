package ch.baloise.observability.gabelstaplerbuggyapp;

import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.api.Logger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import io.pyroscope.javaagent.config.Config;
import io.pyroscope.javaagent.EventType;
import io.pyroscope.http.Format;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;

@SpringBootApplication
public class PyroscopeApplication {

	@Value("${pyroscope.url}")
	private String pyroscopeUrl;

	public static void main(String[] args) {
		SpringApplication.run(PyroscopeApplication.class, args);
	}

	@PostConstruct
	public void init() {
		PyroscopeAgent.start(
				new Config.Builder()
						.setApplicationName("spring-app")
						.setProfilingEvent(EventType.ITIMER)
						.setFormat(Format.JFR)
						.setServerAddress(pyroscopeUrl)
						// Optionally, if authentication is enabled, specify the API key.
						// .setAuthToken(System.getenv("PYROSCOPE_AUTH_TOKEN"))
						// Optionally, if you'd like to set allocation threshold to register events, in bytes. '0' registers all events
						.setProfilingAlloc("0")
						//.setSamplingDuration(Duration.)
						.setLogLevel(Logger.Level.DEBUG)
						.setProfilingLock("10ms")
						.setProfilingInterval(Duration.ofMillis(10))
						.setUploadInterval(Duration.ofSeconds(10))
						.build()
		);
	}

}
