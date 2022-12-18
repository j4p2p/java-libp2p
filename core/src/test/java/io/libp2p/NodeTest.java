package io.libp2p;

import io.libp2p.api.Node;
import io.libp2p.api.Peer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

        List<Peer> peersOfJoy = joy.peers();

        assertEquals(1, peersOfJoy.size());
        assertEquals(alis.host(), peersOfJoy.get(0).host());
        assertEquals(alis.port(), peersOfJoy.get(0).port());

        assertEquals(joy.host(), alis.peers().get(0).host());
        assertNotEquals(joy.port(), alis.peers().get(0).port());

        joy.stop();
        alis.stop();
    }

    @Test
    @DisplayName("Node is MUST know about all peers, that have joined. Node MUST have more than one peer")
    void threePeers() {
        Node joy = Node.of(51003);
        joy.start();

        Node alis = Node.of(51004);
        alis.start();
        alis.connect(joy.host(), joy.port());

        Node bob = Node.of(51005);
        bob.start();
        bob.connect(alis.host(), alis.port());

        joy.connect(bob.host(), bob.port());

        assertEquals(2, joy.peers().size());
        assertEquals(2, alis.peers().size());
        assertEquals(2, bob.peers().size());

        joy.stop();
        alis.stop();
        bob.stop();
    }

    @Test
    @DisplayName("Node does NOT MUST add peer, if it was not connecting")
    void connectToDisabledPeer() {
        Node joy = Node.of(51006);

        Node alis = Node.of(51007);
        alis.start();
        try {
            alis.connect(joy.host(), joy.port());
        } catch (Exception ex) {
            //ignore
        }

        assertEquals(0, alis.peers().size());
        assertEquals(0, joy.peers().size());

        alis.stop();
    }

    @Test
    @DisplayName("Node MUST to store unique peers only")
    void uniquePeers() {
        Node joy = Node.of(51008);
        Node alis = Node.of(51009);

        joy.start();
        alis.start();
        try {
            joy.connect(alis.host(), alis.port());
            alis.connect(joy.host(), joy.port());
        } catch (Exception ex) {
            //ignore
        }

        joy.stop();
        joy.start();

        try {
            joy.connect(alis.host(), alis.port());
        } catch (Exception ex) {
            //ignore
        }

        assertEquals(1, alis.peers().size());
        assertEquals(1, joy.peers().size());

        joy.stop();
        alis.stop();
    }
}
