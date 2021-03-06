package cn.cst.pulsar;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class PulsarConsumer {

  //    public static void main(String[] args) throws PulsarClientException {
  //        PulsarClient client = PulsarUtil.getPulsarClient();
  //        Consumer consumer = client.newConsumer()
  //                .topic("non-persistent://public/default/my-topic-name")
  //                .subscriptionName("consumer_one")
  //                .subscribe();
  //        while (true) {
  //            // Wait for a message
  //            Message msg = consumer.receive();
  //            try {
  //                // Do something with the message
  //                System.out.println("Message received: " + new String(msg.getData()));
  //                // Acknowledge the message so that it can be deleted by the message broker
  //                consumer.acknowledge(msg);
  //            } catch (Exception e) {
  //                // Message failed to process, redeliver later
  //                consumer.negativeAcknowledge(msg);
  //            }
  //        }
  //
  //    }
  public static void main(String[] args) throws PulsarClientException {
    PulsarClient client = PulsarUtil.getPulsarClient();
    Consumer consumer =
        client
            .newConsumer()
            .topic("non-persistent://public/default/my-topic-name")
            .subscriptionName("consumer_two")
            .subscribe();
    while (true) {
      // Wait for a message
      Message msg = consumer.receive();
      try {
        // Do something with the message
        System.out.println("Message received: " + new String(msg.getData()));
        // Acknowledge the message so that it can be deleted by the message broker
        consumer.acknowledge(msg);
      } catch (Exception e) {
        // Message failed to process, redeliver later
        consumer.negativeAcknowledge(msg);
      }
    }
  }
}
