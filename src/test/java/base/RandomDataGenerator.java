package base;

import java.security.SecureRandom;

/**
 * Utility class for generating random test data.
 * Used for creating random usernames, emails, and passwords for testing.
 */
public class RandomDataGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final String[] DOMAINS = {
        "gmail.com", "yahoo.com", "outlook.com", "example.com",
        "testmail.com", "fakemail.org", "demomail.net"
    };
    
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";

    /**
     * Generates a random email address with a random-length prefix (5-15 chars) and random domain.
     * @return a random email address string
     */
    public static String generateRandomEmail() {
        int prefixLength = getRandomLength(5, 15);
        String randomPrefix = generateRandomString(prefixLength, LOWERCASE + DIGITS);
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];
        return randomPrefix + "@" + domain;
    }

    /**
     * Generates a random password with random length between 8 and 20 characters.
     * @return a random password string
     */
    public static String generateRandomPassword() {
        return generateRandomPasswordWithLength(getRandomLength(8, 20));
    }

    /**
     * Generates a random integer between min and max (inclusive).
     * @param min minimum value (inclusive)
     * @param max maximum value (inclusive)
     * @return a random integer in the specified range
     */
    private static int getRandomLength(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Generates a random password with specified length.
     * The password will contain a mix of uppercase, lowercase, digits, and special characters.
     * @param length the desired password length
     * @return a random password string
     */
    private static String generateRandomPasswordWithLength(int length) {
        if (length < 4) {
            length = 4; // Minimum length to include all character types
        }
        
        StringBuilder password = new StringBuilder(length);
        
        // Ensure at least one character from each category
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        
        // Fill the rest with random characters from all categories
        String allChars = LOWERCASE + UPPERCASE + DIGITS + SPECIAL;
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password to randomize positions
        return shuffleString(password.toString());
    }

    /**
     * Generates a random string of specified length using the given character set.
     * @param length the desired string length
     * @param characters the character set to use
     * @return a random string
     */
    private static String generateRandomString(int length, String characters) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }

    /**
     * Shuffles a string using Fisher-Yates algorithm.
     * @param input the string to shuffle
     * @return the shuffled string
     */
    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}