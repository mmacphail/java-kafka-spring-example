package eu.macphail.kafkaex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
public class FileInfoProducer {

    @Value(value = "${fileinfo.topic.name}")
    private String topic;

    @Autowired
    KafkaTemplate<String, FileInfo> template;

    ListenableFuture<SendResult<String, FileInfo>> sendFileInfoMessage(FileInfo fileInfo) {
        log.info("Sending message to kafka " + fileInfo);
        return template.send(topic, fileInfo.getFilePath(), fileInfo);
    }

}
