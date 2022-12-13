package io.libp2p.api.crypto;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface PeerId {
    Type type();
    byte[] privateKey();
    byte[] publicKey();

    static PeerId of(Type type, byte[] privateK, byte[] publicK) {
        throw new NotImplementedException();
    }

    public static enum Type {
        RSA,
        ED25519,
        SECP256K1
    }
}
