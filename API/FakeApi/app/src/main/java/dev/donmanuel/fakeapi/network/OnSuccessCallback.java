package dev.donmanuel.fakeapi.network;

/**
 * Interface for handling successful API responses
 * Following the Interface Segregation Principle
 */
public interface OnSuccessCallback<T> {
    void onSuccess(T result);
}
