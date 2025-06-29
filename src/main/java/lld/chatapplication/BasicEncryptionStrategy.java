package lld.chatapplication;

import java.util.Base64;

public class BasicEncryptionStrategy implements EncryptionStrategy {
    
    @Override
    public String encrypt(String plaintext, String key) {
        if (plaintext == null || key == null) {
            throw new IllegalArgumentException("Plaintext and key cannot be null");
        }
        
        // Simple XOR encryption for demonstration
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] keyBytes = key.getBytes();
        byte[] encrypted = new byte[plaintextBytes.length];
        
        for (int i = 0; i < plaintextBytes.length; i++) {
            encrypted[i] = (byte) (plaintextBytes[i] ^ keyBytes[i % keyBytes.length]);
        }
        
        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    @Override
    public String decrypt(String ciphertext, String key) {
        if (ciphertext == null || key == null) {
            throw new IllegalArgumentException("Ciphertext and key cannot be null");
        }
        
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
            byte[] keyBytes = key.getBytes();
            byte[] decrypted = new byte[encryptedBytes.length];
            
            for (int i = 0; i < encryptedBytes.length; i++) {
                decrypted[i] = (byte) (encryptedBytes[i] ^ keyBytes[i % keyBytes.length]);
            }
            
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt message", e);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "XOR with Base64";
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
}