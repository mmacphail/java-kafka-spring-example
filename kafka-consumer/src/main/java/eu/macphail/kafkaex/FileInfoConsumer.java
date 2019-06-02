package eu.macphail.kafkaex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileInfoConsumer {

    @Autowired
    ElasticService elastic;

    @KafkaListener(topics = "${fileinfo.topic.name}", containerFactory = "fileInfoKafkaListenerContainerFactory")
    public void fileInfoListener(FileInfo fileInfo) {
        log.info("Received fileinfo : {}", fileInfo);
        elastic.sendFileInfo(fileInfo);
    }

}
