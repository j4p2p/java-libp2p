package io.libp2p;

import io.libp2p.api.Node;
import io.libp2p.api.Peer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

        joy.connect(alis.host(), alis.port());

        assertEquals(1, joy.peers().size());
        assertTrue(joy.peers().containsKey(alis.host()));
        assertEquals(alis.host(), joy.peers().get(alis.host()).host());
        assertEquals(alis.port(), joy.peers().get(alis.host()).port());

        assertTrue(alis.peers().containsKey(joy.host()));
        assertEquals(joy.host(), alis.peers().get(joy.host()).host());
        assertNotEquals(joy.port(), alis.peers().get(joy.host()).port());
    }

    @Test
    @DisplayName("Node is MUST know about all peers, that have joined. Node must have at least one peer")
    void threePeers() {
        Node joy = Node.of(51004);
        joy.start();

        Node alis = Node.of(51005);
        alis.start();
        alis.connect(joy.host(), joy.port());

        Node bob = Node.of(51006);
        bob.start();
        bob.connect(alis.host(), alis.port());

        joy.connect(bob.host(), bob.port());

        assertEquals(1, joy.peers().size());
        assertEquals(1, alis.peers().size());
        assertEquals(1, bob.peers().size());
    }
}
