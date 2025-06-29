package lld.colorlist;

/**
 * Utility class for color operations and calculations
 */
public class ColorUtils {
    
    /**
     * Calculate Euclidean distance between two colors in RGB space
     */
    public static double calculateColorDistance(Color color1, Color color2) {
        int deltaR = color1.getRed() - color2.getRed();
        int deltaG = color1.getGreen() - color2.getGreen();
        int deltaB = color1.getBlue() - color2.getBlue();
        
        return Math.sqrt(deltaR * deltaR + deltaG * deltaG + deltaB * deltaB);
    }
    
    /**
     * Calculate perceptual color distance (Delta E)
     * More accurate for human perception
     */
    public static double calculatePerceptualDistance(Color color1, Color color2) {
        // Convert RGB to LAB color space for better perceptual accuracy
        double[] lab1 = rgbToLab(color1.getRed(), color1.getGreen(), color1.getBlue());
        double[] lab2 = rgbToLab(color2.getRed(), color2.getGreen(), color2.getBlue());
        
        double deltaL = lab1[0] - lab2[0];
        double deltaA = lab1[1] - lab2[1];
        double deltaB = lab1[2] - lab2[2];
        
        return Math.sqrt(deltaL * deltaL + deltaA * deltaA + deltaB * deltaB);
    }
    
    /**
     * Get color family/category
     */
    public static String getColorFamily(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        
        // Calculate luminance for grayscale detection
        double luminance = color.getLuminance();
        
        // Check for grayscale
        if (Math.abs(r - g) < 30 && Math.abs(g - b) < 30 && Math.abs(r - b) < 30) {
            if (luminance < 0.2) return "Black";
            if (luminance > 0.8) return "White";
            return "Gray";
        }
        
        // Find dominant channel
        int max = Math.max(Math.max(r, g), b);
        int min = Math.min(Math.min(r, g), b);
        
        // Check for low saturation (grayish colors)
        if (max - min < 50) {
            return "Muted";
        }
        
        // Determine hue-based family
        if (r == max) {
            if (g > b) return "Orange/Yellow";
            return "Red/Pink";
        } else if (g == max) {
            if (r > b) return "Yellow/Green";
            return "Green";
        } else {
            if (r > g) return "Purple/Violet";
            return "Blue";
        }
    }
    
    /**
     * Check if color is warm or cool
     */
    public static boolean isWarmColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        
        // Simplified warm/cool detection
        return (r + g) > (b * 1.5);
    }
    
    /**
     * Convert RGB to HSV color space
     */
    public static double[] rgbToHsv(int r, int g, int b) {
        double rNorm = r / 255.0;
        double gNorm = g / 255.0;
        double bNorm = b / 255.0;
        
        double max = Math.max(Math.max(rNorm, gNorm), bNorm);
        double min = Math.min(Math.min(rNorm, gNorm), bNorm);
        double delta = max - min;
        
        double h = 0;
        if (delta != 0) {
            if (max == rNorm) {
                h = 60 * (((gNorm - bNorm) / delta) % 6);
            } else if (max == gNorm) {
                h = 60 * (((bNorm - rNorm) / delta) + 2);
            } else {
                h = 60 * (((rNorm - gNorm) / delta) + 4);
            }
        }
        
        double s = max == 0 ? 0 : delta / max;
        double v = max;
        
        return new double[]{h, s, v};
    }
    
    /**
     * Convert HSV to RGB color space
     */
    public static int[] hsvToRgb(double h, double s, double v) {
        double c = v * s;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = v - c;
        
        double r1, g1, b1;
        
        if (h >= 0 && h < 60) {
            r1 = c; g1 = x; b1 = 0;
        } else if (h >= 60 && h < 120) {
            r1 = x; g1 = c; b1 = 0;
        } else if (h >= 120 && h < 180) {
            r1 = 0; g1 = c; b1 = x;
        } else if (h >= 180 && h < 240) {
            r1 = 0; g1 = x; b1 = c;
        } else if (h >= 240 && h < 300) {
            r1 = x; g1 = 0; b1 = c;
        } else {
            r1 = c; g1 = 0; b1 = x;
        }
        
        int r = (int) Math.round((r1 + m) * 255);
        int g = (int) Math.round((g1 + m) * 255);
        int b = (int) Math.round((b1 + m) * 255);
        
        return new int[]{r, g, b};
    }
    
    /**
     * Convert RGB to LAB color space (simplified)
     */
    private static double[] rgbToLab(int r, int g, int b) {
        // Convert RGB to XYZ first
        double[] xyz = rgbToXyz(r, g, b);
        
        // Convert XYZ to LAB
        double x = xyz[0] / 95.047;
        double y = xyz[1] / 100.000;
        double z = xyz[2] / 108.883;
        
        x = x > 0.008856 ? Math.cbrt(x) : (7.787 * x) + (16.0 / 116.0);
        y = y > 0.008856 ? Math.cbrt(y) : (7.787 * y) + (16.0 / 116.0);
        z = z > 0.008856 ? Math.cbrt(z) : (7.787 * z) + (16.0 / 116.0);
        
        double l = (116 * y) - 16;
        double a = 500 * (x - y);
        double bLab = 200 * (y - z);
        
        return new double[]{l, a, bLab};
    }
    
    /**
     * Convert RGB to XYZ color space
     */
    private static double[] rgbToXyz(int r, int g, int b) {
        double rNorm = r / 255.0;
        double gNorm = g / 255.0;
        double bNorm = b / 255.0;
        
        // Apply gamma correction
        rNorm = rNorm > 0.04045 ? Math.pow((rNorm + 0.055) / 1.055, 2.4) : rNorm / 12.92;
        gNorm = gNorm > 0.04045 ? Math.pow((gNorm + 0.055) / 1.055, 2.4) : gNorm / 12.92;
        bNorm = bNorm > 0.04045 ? Math.pow((bNorm + 0.055) / 1.055, 2.4) : bNorm / 12.92;
        
        // Apply transformation matrix
        double x = rNorm * 0.4124 + gNorm * 0.3576 + bNorm * 0.1805;
        double y = rNorm * 0.2126 + gNorm * 0.7152 + bNorm * 0.0722;
        double z = rNorm * 0.0193 + gNorm * 0.1192 + bNorm * 0.9505;
        
        return new double[]{x * 100, y * 100, z * 100};
    }
    
    /**
     * Generate random color
     */
    public static Color generateRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        
        return new Color("Random Color", r, g, b);
    }
    
    /**
     * Mix two colors
     */
    public static Color mixColors(Color color1, Color color2, double ratio) {
        if (ratio < 0 || ratio > 1) {
            throw new IllegalArgumentException("Ratio must be between 0 and 1");
        }
        
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        
        return new Color("Mixed Color", r, g, b);
    }
}