package io.libp2p;

import io.libp2p.api.Node;
import io.libp2p.api.Peer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("It is testing nodes")
public class NodeTest {

    @Test
    @DisplayName("Node is MUST correctly status before and after call start and stop methods")
    void startAndStop() {
        Node node = Node.of(51000);
        assertEquals(Node.Status.STOPPED, node.status());

        node.start();
        assertEquals(Node.Status.RUNNING, node.status());

        node.stop();
        assertEquals(Node.Status.STOPPED, node.status());
    }

    @Test
    @DisplayName("Node is MUST know about peers, that have joined")
    void peers() {
        Node joy = Node.of(51001);
        joy.start();

        Node alis = Node.of(51002);
        alis.start();

        alis.connect(joy.host(), joy.port());

        List<Peer> peersOfJoy = joy.peers();

        assertEquals(1, peersOfJoy.size());
        assertEquals(alis.host(), peersOfJoy.get(0).host());
        assertEquals(alis.outputPort(), peersOfJoy.get(0).port());
    }
}
