package io.libp2p.api;

import io.libp2p.api.impl.NodeImpl;

import java.util.List;

public interface Node {

    static Node of(int port) {
        return new NodeImpl(port);
    }

    void start();

    void stop();

    String host();

    int port();

    Status status();

    List<Peer> peers();

    void connect(String host, int port);

    public static enum Status {
        STOPPED,
        RUNNING
    }
}
