package ch.baloise.observability.gabelstaplerbuggyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

import java.time.Duration;

@SpringBootApplication
public class GabelStaplerBuggyApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(GabelStaplerBuggyApplication.class, args);
	}

//	@PostConstruct
//	public void init() {
//		PyroscopeAgent.start(
//				new Config.Builder()
//						.setApplicationName(applicationName)
//						.setProfilingEvent(EventType.ITIMER)
//						.setFormat(Format.JFR)
//						.setServerAddress(pyroscopeUrl)
//						// Optionally, if authentication is enabled, specify the API key.
//						// .setAuthToken(System.getenv("PYROSCOPE_AUTH_TOKEN"))
//						// Optionally, if you'd like to set allocation threshold to register events, in bytes. '0' registers all events
//						.setProfilingAlloc("0")
//						//.setSamplingDuration(Duration.)
//						.setLogLevel(Logger.Level.DEBUG)
//						.setProfilingLock("10ms")
//						.setProfilingInterval(Duration.ofMillis(10))
//						.setUploadInterval(Duration.ofSeconds(10))
//						.build()
//		);
//	}

}
