package io.libp2p.crypto;

public interface HMAC {
    byte[] digest(byte[] data);
}
