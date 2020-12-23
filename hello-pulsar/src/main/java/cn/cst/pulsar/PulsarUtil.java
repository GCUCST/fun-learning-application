package cn.cst.pulsar;

import lombok.experimental.UtilityClass;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

@UtilityClass
class PulsarUtil {

  PulsarClient getPulsarClient() throws PulsarClientException {
    PulsarClient client = PulsarClient.builder().serviceUrl("pulsar://localhost:6650").build();
    return client;
  }
}
