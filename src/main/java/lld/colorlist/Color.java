package lld.colorlist;

import java.util.Objects;

/**
 * Represents a color with RGB values and metadata
 * Immutable design for thread safety
 */
public class Color {
    private final String name;
    private final int red;
    private final int green;
    private final int blue;
    private final String hexCode;
    
    public Color(String name, int red, int green, int blue) {
        validateRGB(red, green, blue);
        this.name = name != null ? name.trim() : generateDefaultName(red, green, blue);
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hexCode = String.format("#%02X%02X%02X", red, green, blue);
    }
    
    public Color(String name, String hexCode) {
        if (hexCode == null || !isValidHexCode(hexCode)) {
            throw new IllegalArgumentException("Invalid hex code: " + hexCode);
        }
        
        this.name = name != null ? name.trim() : generateDefaultName(hexCode);
        this.hexCode = hexCode.toUpperCase();
        
        // Parse hex to RGB
        int hex = Integer.parseInt(hexCode.substring(1), 16);
        this.red = (hex >> 16) & 0xFF;
        this.green = (hex >> 8) & 0xFF;
        this.blue = hex & 0xFF;
    }
    
    // Factory methods for common colors
    public static Color fromRGB(String name, int red, int green, int blue) {
        return new Color(name, red, green, blue);
    }
    
    public static Color fromHex(String name, String hexCode) {
        return new Color(name, hexCode);
    }
    
    public static Color red() {
        return new Color("Red", 255, 0, 0);
    }
    
    public static Color green() {
        return new Color("Green", 0, 255, 0);
    }
    
    public static Color blue() {
        return new Color("Blue", 0, 0, 255);
    }
    
    public static Color white() {
        return new Color("White", 255, 255, 255);
    }
    
    public static Color black() {
        return new Color("Black", 0, 0, 0);
    }
    
    private void validateRGB(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255");
        }
    }
    
    private boolean isValidHexCode(String hexCode) {
        return hexCode.matches("^#[0-9A-Fa-f]{6}$");
    }
    
    private String generateDefaultName(int red, int green, int blue) {
        return String.format("RGB(%d,%d,%d)", red, green, blue);
    }
    
    private String generateDefaultName(String hexCode) {
        return "Color " + hexCode;
    }
    
    /**
     * Calculate luminance for accessibility checks
     */
    public double getLuminance() {
        double r = red / 255.0;
        double g = green / 255.0;
        double b = blue / 255.0;
        
        // Apply gamma correction
        r = r <= 0.03928 ? r / 12.92 : Math.pow((r + 0.055) / 1.055, 2.4);
        g = g <= 0.03928 ? g / 12.92 : Math.pow((g + 0.055) / 1.055, 2.4);
        b = b <= 0.03928 ? b / 12.92 : Math.pow((b + 0.055) / 1.055, 2.4);
        
        return 0.2126 * r + 0.7152 * g + 0.0722 * b;
    }
    
    /**
     * Calculate contrast ratio with another color
     */
    public double getContrastRatio(Color other) {
        double l1 = this.getLuminance();
        double l2 = other.getLuminance();
        
        double lighter = Math.max(l1, l2);
        double darker = Math.min(l1, l2);
        
        return (lighter + 0.05) / (darker + 0.05);
    }
    
    /**
     * Check if color meets accessibility standards
     */
    public boolean isAccessibleWith(Color other) {
        return getContrastRatio(other) >= 4.5; // WCAG AA standard
    }
    
    /**
     * Generate complementary color
     */
    public Color getComplementaryColor() {
        return new Color("Complementary of " + name, 255 - red, 255 - green, 255 - blue);
    }
    
    /**
     * Darken color by percentage
     */
    public Color darken(double percentage) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Percentage must be between 0 and 1");
        }
        
        int newRed = (int) (red * (1 - percentage));
        int newGreen = (int) (green * (1 - percentage));
        int newBlue = (int) (blue * (1 - percentage));
        
        return new Color(name + " (darkened)", newRed, newGreen, newBlue);
    }
    
    /**
     * Lighten color by percentage
     */
    public Color lighten(double percentage) {
        if (percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("Percentage must be between 0 and 1");
        }
        
        int newRed = (int) (red + (255 - red) * percentage);
        int newGreen = (int) (green + (255 - green) * percentage);
        int newBlue = (int) (blue + (255 - blue) * percentage);
        
        return new Color(name + " (lightened)", newRed, newGreen, newBlue);
    }
    
    // Getters
    public String getName() { return name; }
    public int getRed() { return red; }
    public int getGreen() { return green; }
    public int getBlue() { return blue; }
    public String getHexCode() { return hexCode; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Color color = (Color) obj;
        return red == color.red && green == color.green && blue == color.blue;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", name, hexCode);
    }
}