package lld.colorlist;

/**
 * Enum representing different types of color palettes
 * Used for generating color schemes based on color theory
 */
public enum PaletteType {
    MONOCHROMATIC,  // Different shades/tints of the same color
    ANALOGOUS,      // Colors next to each other on color wheel
    COMPLEMENTARY,  // Opposite colors on color wheel
    TRIADIC,        // Three colors evenly spaced on color wheel
    TETRADIC,       // Four colors forming a rectangle on color wheel
    SPLIT_COMPLEMENTARY, // Base color + two colors adjacent to complement
    WARM,           // Warm color palette
    COOL,           // Cool color palette
    PASTEL,         // Light, soft colors
    VIBRANT         // Bright, saturated colors
}