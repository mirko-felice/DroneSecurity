/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Helper class to create and check passwords.
 */
public final class PasswordHelper {

    private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    private static final int SALT_BYTES = 24;
    private static final int HASH_BYTES = 24;
    private static final int HASH_ITERATIONS = 65_536;

    private static final int ITERATION_INDEX = 0;
    private static final int SALT_INDEX = 1;
    private static final int HASH_INDEX = 2;
    private static final int RADIX = 16;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordHelper() { }

    /**
     * Generate hash from password string.
     * @param password the password to hash
     * @return the hash
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws InvalidKeySpecException if key is invalid
     */
    public static @NotNull String createHash(final @NotNull String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] salt = new byte[SALT_BYTES];
        SECURE_RANDOM.nextBytes(salt);

        final byte[] hash = hash(password.toCharArray(), salt, HASH_ITERATIONS, HASH_BYTES);
        // format iterations:salt:hash
        return HASH_ITERATIONS + ":" + Hex.encodeHexString(salt) + ":" + Hex.encodeHexString(hash);
    }

    /**
     * Checks if passwords are the same.
     * @param password the password to validate
     * @param storedPassword the real password
     * @return true if passwords are equals, otherwise false
     * @throws NoSuchAlgorithmException if algorithm is not available
     * @throws InvalidKeySpecException if key is invalid
     */
    public static boolean validatePassword(final @NotNull String password, final @NotNull String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Decode the hash into its parameters
        final String[] params = storedPassword.split(":");
        final int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        final byte[] salt = fromHex(params[SALT_INDEX]);
        final byte[] hash = fromHex(params[HASH_INDEX]);
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        final byte[] testHash = hash(password.toCharArray(), salt, iterations, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash);
    }

    @Contract(pure = true)
    private static boolean slowEquals(final byte @NotNull [] a, final byte @NotNull [] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    private static byte[] hash(final char[] password, final byte[] salt, final int iterations, final int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        return SecretKeyFactory.getInstance(HASH_ALGORITHM).generateSecret(spec).getEncoded();
    }

    private static byte @NotNull [] fromHex(final @NotNull String hex) {
        final byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), RADIX);
        }
        return binary;
    }

}
