package io.libp2p.api;

import io.ipfs.multiaddr.MultiAddress;
import io.libp2p.api.impl.NodeImpl;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Node {

    /**
     * @deprecated recommend use method Node::of(MultiAddress a)
     * @param port it is port which Node will be listened
     * @return
     */
    @Deprecated
    static Node of(int port) {
        return new NodeImpl(port);
    }

    static Node of(MultiAddress address) {
        return new NodeImpl(address.getPort());
    }

    void start();

    void stop();

    String host();

    int port();

    Status status();

    Map<String, Peer> peers();

    void connect(String host, int port);

    public static enum Status {
        STOPPED,
        RUNNING
    }
}
