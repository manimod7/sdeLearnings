package lld.colorlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator class for creating color palettes based on color theory
 * Implements various palette generation algorithms
 */
public class PaletteGenerator {
    
    /**
     * Generate color palette based on type and base color
     */
    public static List<Color> generatePalette(Color baseColor, PaletteType type) {
        List<Color> palette = new ArrayList<>();
        
        switch (type) {
            case MONOCHROMATIC:
                palette = generateMonochromatic(baseColor);
                break;
            case ANALOGOUS:
                palette = generateAnalogous(baseColor);
                break;
            case COMPLEMENTARY:
                palette = generateComplementary(baseColor);
                break;
            case TRIADIC:
                palette = generateTriadic(baseColor);
                break;
            case TETRADIC:
                palette = generateTetradic(baseColor);
                break;
            case SPLIT_COMPLEMENTARY:
                palette = generateSplitComplementary(baseColor);
                break;
            case WARM:
                palette = generateWarmPalette(baseColor);
                break;
            case COOL:
                palette = generateCoolPalette(baseColor);
                break;
            case PASTEL:
                palette = generatePastelPalette(baseColor);
                break;
            case VIBRANT:
                palette = generateVibrantPalette(baseColor);
                break;
            default:
                palette.add(baseColor);
        }
        
        return palette;
    }
    
    /**
     * Generate monochromatic palette (different shades and tints)
     */
    private static List<Color> generateMonochromatic(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        
        // Add base color
        palette.add(baseColor);
        
        // Add darker shades
        palette.add(baseColor.darken(0.2));
        palette.add(baseColor.darken(0.4));
        
        // Add lighter tints
        palette.add(baseColor.lighten(0.2));
        palette.add(baseColor.lighten(0.4));
        
        return palette;
    }
    
    /**
     * Generate analogous palette (adjacent colors on color wheel)
     */
    private static List<Color> generateAnalogous(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        palette.add(baseColor);
        
        // Generate colors 30 degrees apart
        for (int i = 1; i <= 2; i++) {
            double newHue = (hsv[0] + (30 * i)) % 360;
            int[] rgb = ColorUtils.hsvToRgb(newHue, hsv[1], hsv[2]);
            palette.add(new Color("Analogous " + i, rgb[0], rgb[1], rgb[2]));
        }
        
        for (int i = 1; i <= 2; i++) {
            double newHue = (hsv[0] - (30 * i) + 360) % 360;
            int[] rgb = ColorUtils.hsvToRgb(newHue, hsv[1], hsv[2]);
            palette.add(new Color("Analogous -" + i, rgb[0], rgb[1], rgb[2]));
        }
        
        return palette;
    }
    
    /**
     * Generate complementary palette (opposite colors)
     */
    private static List<Color> generateComplementary(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        palette.add(baseColor);
        
        // Add complementary color (180 degrees opposite)
        double complementaryHue = (hsv[0] + 180) % 360;
        int[] rgb = ColorUtils.hsvToRgb(complementaryHue, hsv[1], hsv[2]);
        palette.add(new Color("Complementary", rgb[0], rgb[1], rgb[2]));
        
        // Add variations
        palette.add(baseColor.lighten(0.2));
        palette.add(baseColor.darken(0.2));
        
        int[] lightRgb = ColorUtils.hsvToRgb(complementaryHue, hsv[1] * 0.8, Math.min(hsv[2] * 1.2, 1.0));
        palette.add(new Color("Light Complementary", lightRgb[0], lightRgb[1], lightRgb[2]));
        
        return palette;
    }
    
    /**
     * Generate triadic palette (120 degrees apart)
     */
    private static List<Color> generateTriadic(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        palette.add(baseColor);
        
        // Add colors 120 degrees apart
        for (int i = 1; i <= 2; i++) {
            double newHue = (hsv[0] + (120 * i)) % 360;
            int[] rgb = ColorUtils.hsvToRgb(newHue, hsv[1], hsv[2]);
            palette.add(new Color("Triadic " + i, rgb[0], rgb[1], rgb[2]));
        }
        
        return palette;
    }
    
    /**
     * Generate tetradic palette (rectangle on color wheel)
     */
    private static List<Color> generateTetradic(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        palette.add(baseColor);
        
        // Add colors 90 degrees apart
        for (int i = 1; i <= 3; i++) {
            double newHue = (hsv[0] + (90 * i)) % 360;
            int[] rgb = ColorUtils.hsvToRgb(newHue, hsv[1], hsv[2]);
            palette.add(new Color("Tetradic " + i, rgb[0], rgb[1], rgb[2]));
        }
        
        return palette;
    }
    
    /**
     * Generate split complementary palette
     */
    private static List<Color> generateSplitComplementary(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        palette.add(baseColor);
        
        // Add colors adjacent to complement (150 and 210 degrees)
        double complementaryHue = (hsv[0] + 180) % 360;
        
        double hue1 = (complementaryHue - 30 + 360) % 360;
        double hue2 = (complementaryHue + 30) % 360;
        
        int[] rgb1 = ColorUtils.hsvToRgb(hue1, hsv[1], hsv[2]);
        int[] rgb2 = ColorUtils.hsvToRgb(hue2, hsv[1], hsv[2]);
        
        palette.add(new Color("Split Complement 1", rgb1[0], rgb1[1], rgb1[2]));
        palette.add(new Color("Split Complement 2", rgb2[0], rgb2[1], rgb2[2]));
        
        return palette;
    }
    
    /**
     * Generate warm color palette
     */
    private static List<Color> generateWarmPalette(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        
        // Warm colors: reds, oranges, yellows
        palette.add(new Color("Warm Red", 255, 99, 71));
        palette.add(new Color("Warm Orange", 255, 165, 0));
        palette.add(new Color("Warm Yellow", 255, 215, 0));
        palette.add(new Color("Warm Pink", 255, 182, 193));
        palette.add(baseColor);
        
        return palette;
    }
    
    /**
     * Generate cool color palette
     */
    private static List<Color> generateCoolPalette(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        
        // Cool colors: blues, greens, purples
        palette.add(new Color("Cool Blue", 70, 130, 180));
        palette.add(new Color("Cool Green", 32, 178, 170));
        palette.add(new Color("Cool Purple", 138, 43, 226));
        palette.add(new Color("Cool Cyan", 0, 191, 255));
        palette.add(baseColor);
        
        return palette;
    }
    
    /**
     * Generate pastel color palette
     */
    private static List<Color> generatePastelPalette(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        // Create pastel versions by reducing saturation and increasing value
        for (int i = 0; i < 5; i++) {
            double newHue = (hsv[0] + (72 * i)) % 360; // 72 degrees apart
            double newSaturation = Math.min(hsv[1] * 0.3, 0.3); // Low saturation
            double newValue = Math.max(hsv[2], 0.8); // High value
            
            int[] rgb = ColorUtils.hsvToRgb(newHue, newSaturation, newValue);
            palette.add(new Color("Pastel " + i, rgb[0], rgb[1], rgb[2]));
        }
        
        return palette;
    }
    
    /**
     * Generate vibrant color palette
     */
    private static List<Color> generateVibrantPalette(Color baseColor) {
        List<Color> palette = new ArrayList<>();
        double[] hsv = ColorUtils.rgbToHsv(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue());
        
        // Create vibrant versions by maximizing saturation
        for (int i = 0; i < 5; i++) {
            double newHue = (hsv[0] + (72 * i)) % 360; // 72 degrees apart
            double newSaturation = 1.0; // Maximum saturation
            double newValue = Math.max(hsv[2], 0.8); // High value
            
            int[] rgb = ColorUtils.hsvToRgb(newHue, newSaturation, newValue);
            palette.add(new Color("Vibrant " + i, rgb[0], rgb[1], rgb[2]));
        }
        
        return palette;
    }
}