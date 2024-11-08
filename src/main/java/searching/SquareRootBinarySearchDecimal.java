package searching;

import java.text.DecimalFormat;

public class SquareRootBinarySearchDecimal {
    /**
     * Computes the square root of a non-negative number x up to 4 decimal places using binary search.
     *
     * @param x The non-negative number whose square root is to be computed.
     * @return The square root of x truncated to 4 decimal places.
     */
    public static double sqrt(double x) {
        // Handle edge cases
        if (x < 0) {
            throw new IllegalArgumentException("Cannot compute square root of negative number.");
        }
        if (x == 0 || x == 1) {
            return x;
        }

        double left, right;
        // Determine the search range based on the value of x
        if (x < 1) {
            left = x;
            right = 1;
        } else {
            left = 0;
            right = x;
        }

        double mid = 0;
        double epsilon = 1e-5; // Precision up to 4 decimal places

        while ((right - left) > epsilon) {
            mid = left + (right - left) / 2;
            double midSquared = mid * mid;

            if (midSquared == x) {
                break; // Exact square root found
            } else if (midSquared < x) {
                left = mid;
            } else {
                right = mid;
            }
        }

        // Truncate the result to 4 decimal places
        return truncateToDecimalPlaces(mid, 4);
    }

    /**
     * Truncates a double value to a specified number of decimal places without rounding.
     *
     * @param value         The double value to truncate.
     * @param decimalPlaces The number of decimal places to retain.
     * @return The truncated double value.
     */
    private static double truncateToDecimalPlaces(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.floor(value * factor) / factor;
    }

    /**
     * Formats a double value to a specified number of decimal places with rounding.
     *
     * @param value         The double value to format.
     * @param decimalPlaces The number of decimal places to retain.
     * @return The formatted string representing the double value.
     */
    private static String formatDouble(double value, int decimalPlaces) {
        StringBuilder pattern = new StringBuilder("0.");
        for(int i = 0; i < decimalPlaces; i++) {
            pattern.append("0");
        }
        DecimalFormat df = new DecimalFormat(pattern.toString());
        return df.format(value);
    }

    // Example usage and test cases
    public static void main(String[] args) {
        double[] testCases = {0.25, 0.04, 2};
        for (double x : testCases) {
            try {
                double sqrtValue = sqrt(x);
                String formattedSqrt = formatDouble(sqrtValue, 4);
                System.out.println("Square root of " + x + " is: " + formattedSqrt);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

