package io.libp2p.api.impl;

import io.libp2p.api.Peer;

public class PeerImpl implements Peer {
    private final String host;
    private final int port;

    public PeerImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }
}
