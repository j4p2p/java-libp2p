package io.libp2p.api;

import io.libp2p.Host;

import java.util.List;

public interface Node {

    static Node of(int port) {
        return Host.of(port);
    }
    void start();
    void stop();
    String host();
    int port();
    int outputPort();
    Status status();

    List<Peer> peers();

    void connect(String host, int port);

    public static enum Status {
        STOPPED,
        RUNNING
    }
}
