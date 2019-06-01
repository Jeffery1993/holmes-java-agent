package com.jeffery.holmes.common.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utility for IO operations.
 */
public class IOUtils {

    /**
     * Close the {@link Closeable} resource quietly.
     *
     * @param closeable the resource to be closed
     */
    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }

}
