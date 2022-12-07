package io.libp2p.api;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public interface Node {

    static Node of(int port) {
        throw new NotImplementedException();
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
