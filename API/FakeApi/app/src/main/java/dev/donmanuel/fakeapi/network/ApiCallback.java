package dev.donmanuel.fakeapi.network;

/**
 * Combined callback interface that extends both success and error callbacks
 * Following the Interface Segregation Principle
 */
public interface ApiCallback<T> extends OnSuccessCallback<T>, OnErrorCallback {
    // No additional methods needed as they are inherited from parent interfaces
}
