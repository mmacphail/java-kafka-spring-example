package eu.macphail.kafkaex;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@Service
public class ElasticService {

    @Value(value = "${es.scheme}")
    private String elasticSearchScheme;

    @Value(value = "${es.host}")
    private String elasticSearchHostname;

    @Value(value = "${es.port}")
    private int elasticSearchPort;

    @Autowired
    private ObjectMapper json;

    private RestHighLevelClient client;

    @PostConstruct
    public void init() {
        this.client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticSearchHostname, elasticSearchPort, elasticSearchScheme)));
    }

    @PreDestroy
    public void cleanup() throws IOException {
        client.close();
    }

    public void sendFileInfo(FileInfo fileInfo) {
        try {
            IndexRequest request = new IndexRequest("fileinfo")
                    .id(fileInfo.getFilePath())
                    .source(json.writer().writeValueAsString(fileInfo), XContentType.JSON)
                    .timeout(TimeValue.timeValueSeconds(1));
            client.index(request, RequestOptions.DEFAULT);
            log.info("FileInfo {} sent to elasticsearch", fileInfo.getFilePath());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
