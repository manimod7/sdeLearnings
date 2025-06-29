package lld.chatapplication;

public class MediaMessage extends Message {
    private String mediaUrl;
    private String mediaType;
    private String thumbnailUrl;
    private long fileSize;
    private int duration; // for audio/video
    
    public MediaMessage(String senderId, String chatRoomId, String mediaUrl, String mediaType) {
        super(senderId, chatRoomId, mediaUrl, MessageType.MEDIA);
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.fileSize = 0;
        this.duration = 0;
    }
    
    @Override
    public String getDisplayContent() {
        if (encrypted) {
            return "[Encrypted Media]";
        }
        
        return String.format("[%s] %s", mediaType.toUpperCase(), 
                           mediaUrl.substring(mediaUrl.lastIndexOf('/') + 1));
    }
    
    public String getMediaInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Media Type: ").append(mediaType);
        
        if (fileSize > 0) {
            info.append(", Size: ").append(formatFileSize(fileSize));
        }
        
        if (duration > 0) {
            info.append(", Duration: ").append(formatDuration(duration));
        }
        
        return info.toString();
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return (bytes / (1024 * 1024)) + " MB";
    }
    
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;\n        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
    
    public boolean isImage() {
        return mediaType != null && (mediaType.startsWith("image/") || 
                mediaType.equals("jpg") || mediaType.equals("png") || 
                mediaType.equals("gif") || mediaType.equals("webp"));
    }
    
    public boolean isVideo() {
        return mediaType != null && (mediaType.startsWith("video/") || 
                mediaType.equals("mp4") || mediaType.equals("avi") || 
                mediaType.equals("mov") || mediaType.equals("webm"));
    }
    
    public boolean isAudio() {
        return mediaType != null && (mediaType.startsWith("audio/") || 
                mediaType.equals("mp3") || mediaType.equals("wav") || 
                mediaType.equals("ogg") || mediaType.equals("m4a"));
    }
    
    // Getters and setters
    public String getMediaUrl() { return mediaUrl; }
    public String getMediaType() { return mediaType; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    @Override
    public String toString() {
        return String.format("[MEDIA] %s", super.toString());
    }
}