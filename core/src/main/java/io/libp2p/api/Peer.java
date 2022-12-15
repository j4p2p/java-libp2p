package io.libp2p.api;

import io.libp2p.api.impl.PeerImpl;

public interface Peer {

    static Peer of(String host, int port) {
        return new PeerImpl(host, port);
    }

    String host();

    int port();
}
