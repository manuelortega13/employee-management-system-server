package com.ethan.employee_system.urlshortener;

/**
 * Reversible Base62 codec used to turn a numeric id into a compact short code
 * and back again. The DB only maps id -> original URL; this class is what makes
 * the short code itself collision-free and decodable.
 */
final class Base62 {

    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    private Base62() {
    }

    static String encode(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        if (value == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(ALPHABET.charAt((int) (value % BASE)));
            value /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * @throws IllegalArgumentException if the code is empty or contains a
     *                                  character outside the Base62 alphabet
     */
    static long decode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("code must not be empty");
        }
        long value = 0;
        for (int i = 0; i < code.length(); i++) {
            int digit = ALPHABET.indexOf(code.charAt(i));
            if (digit < 0) {
                throw new IllegalArgumentException("invalid character in code: " + code.charAt(i));
            }
            value = value * BASE + digit;
        }
        return value;
    }
}
