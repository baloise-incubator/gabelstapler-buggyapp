package ch.baloise.observability.gabelstaplerbuggyapp;

import io.pyroscope.labels.LabelsSet;
import io.pyroscope.labels.Pyroscope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class ExampleController {

    static HashMap<Object, Object> myMap = new HashMap<>();

    @GetMapping("/example")
    String example() throws Exception {
        return Pyroscope.LabelsWrapper.run(new LabelsSet("controller", "ExampleController.example"), () -> {
            log.info("ExampleController.example endpoint called");
            return "normal";
        });
    }

    @GetMapping("/example/long")
    String exampleLong() throws Exception {
        return Pyroscope.LabelsWrapper.run(new LabelsSet("controller", "ExampleController.exampleLong"), () -> {
            log.info("ExampleController.exampleLong endpoint called");
            Thread.sleep(15000);
            return "bubu long";
        });
    }

    @GetMapping("/example/cpu")
    String exampleCpu() throws Exception {
        return Pyroscope.LabelsWrapper.run(new LabelsSet("controller", "ExampleController.exampleCpu"), () -> {
            log.info("ExampleController.exampleCpu endpoint called");
            var thread1 = new CPUSpikerThread();
            thread1.start();
            var thread2 = new CPUSpikerThread();
            thread2.start();
            var thread3 = new CPUSpikerThread();
            thread3.start();
            var thread4 = new CPUSpikerThread();
            thread4.start();
            var thread5 = new CPUSpikerThread();
            thread5.start();
            Thread.sleep(15000);
            thread1.stop();
            thread2.stop();
            thread3.stop();
            thread4.stop();
            thread5.stop();
            return "bubu cpu";
        });
    }

    @GetMapping("/example/memory")
    String exampleMemory() throws Exception {
        return Pyroscope.LabelsWrapper.run(new LabelsSet("controller", "ExampleController.exampleMemory"), () -> {
            log.info("ExampleController.exampleMemory endpoint called");
            var counter = 0;
            while (counter < 1000000) {
                myMap.put("key" + counter, "Large stringgggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + "ggggggggggggggggggggggggggggggggggggggggggggggggggggg"
                        + counter);
                log.info("inserted key in map key" + counter);
                ++counter;
            }
            return "bubu memory";
        });
    }

    public static class CPUSpikerThread extends Thread {

        @Override
        public void run() {
            while (true) {
                // Just looping infinitely
            }
        }
    }

}
