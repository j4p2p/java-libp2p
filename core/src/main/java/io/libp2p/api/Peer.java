package io.libp2p.api;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface Peer {

    static Peer of(String host, int port) {
        throw new NotImplementedException();
    }

    String host();
    int port();
}
