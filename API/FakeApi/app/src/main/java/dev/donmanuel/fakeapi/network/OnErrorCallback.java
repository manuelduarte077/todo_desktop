package dev.donmanuel.fakeapi.network;

/**
 * Interface for handling API errors
 * Following the Interface Segregation Principle
 */
public interface OnErrorCallback {
    void onError(Exception e);
}
