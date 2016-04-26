/*
 * Copyright (c) 2016.
 * @gklimek
 */

package connection;

import java.io.Closeable;
import java.io.IOException;

public class ConnectionHandling {

    public static void close(Closeable object) {

        try {
            object.close();

        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void join(Thread thread) {

        try {
            thread.join();

        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }
}