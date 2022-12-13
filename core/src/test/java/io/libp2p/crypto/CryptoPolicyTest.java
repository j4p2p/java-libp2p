package io.libp2p.crypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Your JVM is MUST")
public class CryptoPolicyTest {

    @Test
    @DisplayName("unlimited a length key for AES")
    void unlimitedKeyLength() throws NoSuchAlgorithmException {
        int maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
        assertEquals(2147483647, maxKeySize);
    }
}
