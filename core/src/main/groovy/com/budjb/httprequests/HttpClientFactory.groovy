package com.budjb.httprequests

import com.budjb.httprequests.listener.HttpClientListener

/**
 * Describes a factory class that creates an {@link HttpClient} instance. Individual HTTP client libraries
 * should implement this factory for the creation of HTTP clients.
 *
 * These factories are suitable for instantiation as Spring beans so that the factory can be injected where needed.
 */
interface HttpClientFactory {
    /**
     * Create a new HTTP client.
     *
     * @return An {@link HttpClient} implementation instance.
     */
    HttpClient createHttpClient()

    /**
     * Adds a {@link com.budjb.httprequests.listener.HttpClientListener}.
     *
     * Listeners that are added to the factory are added to {@link HttpClient} objects that the factory creates.
     *
     * @param listener Listener instance to register.
     */
    void addListener(HttpClientListener listener)

    /**
     * Returns the list of all registered {@link HttpClientListener} instances.
     *
     * @return The list of registered listener instances.
     */
    List<HttpClientListener> getListeners()

    /**
     * Unregisters a {@link HttpClientListener}.
     *
     * @param listener Listener instance to remove.
     */
    void removeListener(HttpClientListener listener)

    /**
     * Removes all registered listeners.
     */
    void clearListeners()
}
