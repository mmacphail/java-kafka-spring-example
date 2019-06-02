package eu.macphail.kafkaex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@EnableKafka
@Slf4j
@SpringBootApplication
public class KafkaProducerApplication implements CommandLineRunner {

    @Autowired
    FileInfoProducer producer;

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("Expected argument: path to analyze");
        }

        log.info("Searching for files in {}", String.join(",", args));

        List<ListenableFuture<SendResult<String, FileInfo>>> operations = Files.walk(Paths.get(args[0]))
                .peek(this::logPathVisited)
                .map(FileInfo::readFromPath)
                .map(producer::sendFileInfoMessage)
                .peek(this::addLogCallback)
                .collect(Collectors.toList());

        for(ListenableFuture<SendResult<String, FileInfo>> f : operations) {
            f.get();
        }
    }

    private void logPathVisited(Path path) {
        log.info("Visited path {}", path);
    }

    private void addLogCallback(ListenableFuture<SendResult<String, FileInfo>> f) {
        f.addCallback(
                result -> log.info("Sent fileinfo {}", result.getProducerRecord().key()),
                ex -> log.error("Couldn't send fileinfo", ex));
    }
}
