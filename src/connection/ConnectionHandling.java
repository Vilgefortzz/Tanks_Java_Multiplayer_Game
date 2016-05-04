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

        } catch (IOException e) {
            System.out.println("Problem with closing the object!");
        }
    }

    public static void join(Thread thread) {

        try {
            thread.join();

        } catch (InterruptedException e) {
            System.out.println("Incorrectly stopping a thread!");
        }
    }
}