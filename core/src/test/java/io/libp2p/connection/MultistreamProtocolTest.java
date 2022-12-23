package io.libp2p.connection;

import io.libp2p.api.Node;
import io.libp2p.utils.SillyClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Protocol Negotiation")
public class MultistreamProtocolTest {

    @Test
    @DisplayName("Node MUST send string about support of multi stream protocol")
    void firstLine() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Node bob = Node.of(52000);
        bob.start();

        SillyClient client = SillyClient.of(bob.host(), bob.port(), executorService);
        String firstLine = client.readLine().get(5, TimeUnit.SECONDS);

        assertEquals("/multistream/1.0.0", firstLine);
    }

}
