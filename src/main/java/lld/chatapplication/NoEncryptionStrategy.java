package lld.chatapplication;

public class NoEncryptionStrategy implements EncryptionStrategy {
    
    @Override
    public String encrypt(String plaintext, String key) {
        return plaintext; // No encryption
    }
    
    @Override
    public String decrypt(String ciphertext, String key) {
        return ciphertext; // No decryption needed
    }
    
    @Override
    public String getAlgorithmName() {
        return "No Encryption";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}