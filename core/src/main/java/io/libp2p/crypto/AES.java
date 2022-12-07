package io.libp2p.crypto;

public interface AES {
    byte[] encrypt(byte[] data);
    byte[] decrypt(byte[] data);

}
