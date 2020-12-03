package cn.cst.pulsar;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

@Slf4j
public class PulsarProducter {

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarUtil.getPulsarClient();
        Producer<byte[]> producer = client.newProducer()
                .topic("non-persistent://public/default/my-topic-name")
                .create();
        for (int i = 0; i < 100; i++) {
            producer.send(String.format("My message %s", i).getBytes());
        }


    }
}
