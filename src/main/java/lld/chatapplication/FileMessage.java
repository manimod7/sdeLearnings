package lld.chatapplication;

public class FileMessage extends Message {
    private String fileName;
    private long fileSize;
    private String fileType;
    private String fileUrl;
    private String checksum;
    
    public FileMessage(String senderId, String chatRoomId, String fileName, String fileUrl) {
        super(senderId, chatRoomId, fileName, MessageType.FILE);
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = 0;
        this.fileType = extractFileType(fileName);
    }
    
    @Override
    public String getDisplayContent() {
        if (encrypted) {
            return "[Encrypted File]";
        }
        
        return String.format("📎 %s (%s)", fileName, formatFileSize(fileSize));
    }
    
    public String getFileInfo() {
        StringBuilder info = new StringBuilder();
        info.append("File: ").append(fileName);
        info.append(", Type: ").append(fileType);
        info.append(", Size: ").append(formatFileSize(fileSize));
        
        if (checksum != null) {
            info.append(", Checksum: ").append(checksum.substring(0, 8)).append("...");
        }
        
        return info.toString();
    }
    
    private String extractFileType(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "unknown";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    public boolean isDocument() {
        return fileType.matches("(pdf|doc|docx|xls|xlsx|ppt|pptx|txt|rtf)");
    }
    
    public boolean isArchive() {
        return fileType.matches("(zip|rar|7z|tar|gz|bz2)");
    }
    
    public boolean isExecutable() {
        return fileType.matches("(exe|msi|app|deb|rpm|dmg)");
    }
    
    public boolean isAllowedFileType() {
        // Security check - block potentially dangerous file types
        String[] blockedTypes = {"exe", "bat", "cmd", "scr", "com", "pif", "js", "jar"};
        for (String blocked : blockedTypes) {
            if (fileType.equals(blocked)) {
                return false;
            }
        }
        return true;
    }
    
    public void validateFileSize(long maxSizeBytes) throws IllegalArgumentException {
        if (fileSize > maxSizeBytes) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }
    }
    
    // Getters and setters
    public String getFileName() { return fileName; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getFileType() { return fileType; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    
    @Override
    public String toString() {
        return String.format("[FILE] %s", super.toString());
    }
}