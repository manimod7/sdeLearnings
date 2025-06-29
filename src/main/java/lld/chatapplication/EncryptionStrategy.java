package lld.chatapplication;

public interface EncryptionStrategy {
    String encrypt(String plaintext, String key);
    String decrypt(String ciphertext, String key);
    String getAlgorithmName();
    boolean isAvailable();
}