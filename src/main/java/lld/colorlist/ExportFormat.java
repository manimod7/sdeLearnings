package lld.colorlist;

/**
 * Enum representing different export formats for color lists
 */
public enum ExportFormat {
    JSON,       // JSON format for web applications
    CSV,        // Comma-separated values for spreadsheets
    XML,        // XML format for structured data
    HEX_LIST,   // Simple list of hex codes
    RGB_LIST,   // Simple list of RGB values
    ADOBE_ASE,  // Adobe Swatch Exchange format
    GIMP_GPL,   // GIMP palette format
    CSS,        // CSS variables format
    SCSS        // SCSS variables format
}