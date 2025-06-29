package lld.colorlist;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for exporting color lists to various formats
 * Supports multiple export formats for different use cases
 */
public class ColorExporter {
    
    public static String export(List<Color> colors, ExportFormat format) {
        switch (format) {
            case JSON:
                return exportToJson(colors);
            case CSV:
                return exportToCsv(colors);
            case XML:
                return exportToXml(colors);
            case HEX_LIST:
                return exportToHexList(colors);
            case RGB_LIST:
                return exportToRgbList(colors);
            case ADOBE_ASE:
                return exportToAdobeAse(colors);
            case GIMP_GPL:
                return exportToGimpGpl(colors);
            case CSS:
                return exportToCss(colors);
            case SCSS:
                return exportToScss(colors);
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
    
    /**
     * Export to JSON format
     */
    private static String exportToJson(List<Color> colors) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"colors\": [\n");
        
        for (int i = 0; i < colors.size(); i++) {
            Color color = colors.get(i);
            json.append("    {\n");
            json.append(String.format("      \"name\": \"%s\",\n", color.getName()));
            json.append(String.format("      \"hex\": \"%s\",\n", color.getHexCode()));
            json.append(String.format("      \"rgb\": {\n"));
            json.append(String.format("        \"r\": %d,\n", color.getRed()));
            json.append(String.format("        \"g\": %d,\n", color.getGreen()));
            json.append(String.format("        \"b\": %d\n", color.getBlue()));
            json.append("      }\n");
            json.append("    }");
            
            if (i < colors.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}");
        
        return json.toString();
    }
    
    /**
     * Export to CSV format
     */
    private static String exportToCsv(List<Color> colors) {
        StringBuilder csv = new StringBuilder();
        csv.append("Name,Hex,Red,Green,Blue,Luminance\n");
        
        for (Color color : colors) {
            csv.append(String.format("\"%s\",%s,%d,%d,%d,%.3f\n",
                color.getName(),
                color.getHexCode(),
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getLuminance()
            ));
        }
        
        return csv.toString();
    }
    
    /**
     * Export to XML format
     */
    private static String exportToXml(List<Color> colors) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<colors>\n");
        
        for (Color color : colors) {
            xml.append("  <color>\n");
            xml.append(String.format("    <name>%s</name>\n", escapeXml(color.getName())));
            xml.append(String.format("    <hex>%s</hex>\n", color.getHexCode()));
            xml.append("    <rgb>\n");
            xml.append(String.format("      <r>%d</r>\n", color.getRed()));
            xml.append(String.format("      <g>%d</g>\n", color.getGreen()));
            xml.append(String.format("      <b>%d</b>\n", color.getBlue()));
            xml.append("    </rgb>\n");
            xml.append(String.format("    <luminance>%.3f</luminance>\n", color.getLuminance()));
            xml.append("  </color>\n");
        }
        
        xml.append("</colors>");
        return xml.toString();
    }
    
    /**
     * Export to simple hex list
     */
    private static String exportToHexList(List<Color> colors) {
        return colors.stream()
                    .map(Color::getHexCode)
                    .collect(Collectors.joining("\n"));
    }
    
    /**
     * Export to simple RGB list
     */
    private static String exportToRgbList(List<Color> colors) {
        return colors.stream()
                    .map(color -> String.format("rgb(%d, %d, %d)", 
                                               color.getRed(), color.getGreen(), color.getBlue()))
                    .collect(Collectors.joining("\n"));
    }
    
    /**
     * Export to Adobe Swatch Exchange format (simplified)
     */
    private static String exportToAdobeAse(List<Color> colors) {
        StringBuilder ase = new StringBuilder();
        ase.append("ASEF\n");
        ase.append("1.0\n");
        ase.append(colors.size()).append("\n");
        
        for (Color color : colors) {
            ase.append(String.format("%s %s RGB %d %d %d\n",
                color.getName().replaceAll("\\s+", "_"),
                color.getHexCode(),
                color.getRed(),
                color.getGreen(),
                color.getBlue()
            ));
        }
        
        return ase.toString();
    }
    
    /**
     * Export to GIMP Palette format
     */
    private static String exportToGimpGpl(List<Color> colors) {
        StringBuilder gpl = new StringBuilder();
        gpl.append("GIMP Palette\n");
        gpl.append("Name: Exported Color List\n");
        gpl.append("Columns: 4\n");
        gpl.append("#\n");
        
        for (Color color : colors) {
            gpl.append(String.format("%3d %3d %3d %s\n",
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getName()
            ));
        }
        
        return gpl.toString();
    }
    
    /**
     * Export to CSS variables format
     */
    private static String exportToCss(List<Color> colors) {
        StringBuilder css = new StringBuilder();
        css.append(":root {\n");
        
        for (int i = 0; i < colors.size(); i++) {
            Color color = colors.get(i);
            String variableName = sanitizeCssVariableName(color.getName());
            
            css.append(String.format("  --color-%s: %s;\n", variableName, color.getHexCode()));
            css.append(String.format("  --color-%s-rgb: %d, %d, %d;\n", 
                variableName, color.getRed(), color.getGreen(), color.getBlue()));
        }
        
        css.append("}");
        return css.toString();
    }
    
    /**
     * Export to SCSS variables format
     */
    private static String exportToScss(List<Color> colors) {
        StringBuilder scss = new StringBuilder();
        scss.append("// Color Variables\n");
        
        for (Color color : colors) {
            String variableName = sanitizeCssVariableName(color.getName());
            scss.append(String.format("$color-%s: %s;\n", variableName, color.getHexCode()));
        }
        
        scss.append("\n// Color Map\n");
        scss.append("$colors: (\n");
        
        for (int i = 0; i < colors.size(); i++) {
            Color color = colors.get(i);
            String variableName = sanitizeCssVariableName(color.getName());
            scss.append(String.format("  '%s': %s", variableName, color.getHexCode()));
            
            if (i < colors.size() - 1) {
                scss.append(",");
            }
            scss.append("\n");
        }
        
        scss.append(");");
        return scss.toString();
    }
    
    /**
     * Sanitize color name for CSS variable usage
     */
    private static String sanitizeCssVariableName(String name) {
        return name.toLowerCase()
                  .replaceAll("[^a-z0-9]", "-")
                  .replaceAll("-+", "-")
                  .replaceAll("^-|-$", "");
    }
    
    /**
     * Escape XML special characters
     */
    private static String escapeXml(String text) {
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
}