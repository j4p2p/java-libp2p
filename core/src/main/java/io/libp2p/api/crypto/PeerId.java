package io.libp2p.api.crypto;

public interface PeerId {
    Type type();
    byte[] privateKey();
    byte[] publicKey();

    static PeerId of(Type type, byte[] privateK, byte[] publicK) {
        throw new UnsupportedOperationException();
    }

    public static enum Type {
        RSA,
        ED25519,
        SECP256K1
    }
}
