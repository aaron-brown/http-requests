/*
 * Copyright 2016 Bud Byrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.budjb.httprequests

import com.budjb.httprequests.converter.EntityConverter
import com.budjb.httprequests.converter.EntityConverterManager
import com.budjb.httprequests.filter.HttpClientFilter
import com.budjb.httprequests.filter.HttpClientFilterManager

import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.X509Certificate

/**
 * A base class for HTTP clients that implements most of the functionality of the {@link HttpClient} interface.
 *
 * Individual HTTP client library implementations should extend this class.
 */
abstract class AbstractHttpClient implements HttpClient {
    /**
     * Converter manager.
     */
    EntityConverterManager converterManager

    /**
     * Filter manager.
     */
    HttpClientFilterManager filterManager

    /**
     * Implements the logic to make an actual request with an HTTP client library.
     *
     * @param context HTTP request context.
     * @param entity An {@link HttpEntity} containing the response body. May be <code>null</code>.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    protected abstract HttpResponse doExecute(HttpContext context, HttpEntity entity) throws IOException

    /**
     * Execute an HTTP request with the given method and request parameters and without a request entity.
     *
     * @param method HTTP method to use with the HTTP request.
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse execute(HttpMethod method, HttpRequest request) throws IOException {
        return run(method, request)
    }

    /**
     * Executes an HTTP request with the given method and closure to configure the request without a request entity.
     *
     * @param method HTTP method to use with the HTTP request.
     * @param closure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse execute(HttpMethod method, @DelegatesTo(HttpRequest) Closure closure) throws IOException {
        return run(method, createHttpRequest(closure))
    }

    /**
     * Perform an HTTP GET request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse get(HttpRequest request) throws IOException {
        return execute(HttpMethod.GET, request)
    }

    /**
     * Perform an HTTP GET request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse get(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.GET, requestClosure)
    }

    /**
     * Perform an HTTP POST request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse post(HttpRequest request) throws IOException {
        return execute(HttpMethod.POST, request)
    }

    /**
     * Perform an HTTP POST request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse post(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.POST, requestClosure)
    }

    /**
     * Perform an HTTP PUT request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse put(HttpRequest request) throws IOException {
        return execute(HttpMethod.PUT, request)
    }

    /**
     * Perform an HTTP PUT request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse put(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.PUT, requestClosure)
    }

    /**
     * Perform an HTTP DELETE request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse delete(HttpRequest request) throws IOException {
        return execute(HttpMethod.DELETE, request)
    }

    /**
     * Perform an HTTP DELETE request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse delete(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.DELETE, requestClosure)
    }

    /**
     * Perform an HTTP OPTIONS request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse options(HttpRequest request) throws IOException {
        return execute(HttpMethod.OPTIONS, request)
    }

    /**
     * Perform an HTTP OPTIONS request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse options(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.OPTIONS, requestClosure)
    }

    /**
     * Perform an HTTP HEAD request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse head(HttpRequest request) throws IOException {
        return execute(HttpMethod.HEAD, request)
    }

    /**
     * Perform an HTTP HEAD request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse head(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.HEAD, requestClosure)
    }

    /**
     * Perform an HTTP TRACE request.
     *
     * @param request Request properties to use with the HTTP request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse trace(HttpRequest request) throws IOException {
        return execute(HttpMethod.TRACE, request)
    }

    /**
     * Perform an HTTP TRACE request.
     *
     * @param requestClosure Closure that configures the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     * @throws IOException
     */
    @Override
    HttpResponse trace(@DelegatesTo(HttpRequest) Closure requestClosure) throws IOException {
        return execute(HttpMethod.TRACE, requestClosure)
    }

    /**
     * Adds a {@link HttpClientFilter} to the HTTP client.
     *
     * @param filter Filter instance to register with the client.
     * @return The object the method was called on.
     */
    @Override
    HttpClient addFilter(HttpClientFilter filter) {
        filterManager.add(filter)
        return this
    }

    /**
     * Unregisters a {@link HttpClientFilter} from the HTTP client.
     *
     * @param filter Filter instance to remove from the client.
     * @return The object the method was called on.
     */
    @Override
    HttpClient removeFilter(HttpClientFilter filter) {
        filterManager.remove(filter)
        return this
    }

    /**
     * Returns the list of all registered {@link HttpClientFilter} instances.
     *
     * @return The list of registered filter instances.
     */
    @Override
    List<HttpClientFilter> getFilters() {
        return filterManager.getAll()
    }

    /**
     * Removes all registered filters.
     *
     * @return The object the method was called on.
     */
    @Override
    HttpClient clearFilters() {
        filterManager.clear()
        return this
    }

    /**
     * Orchestrates making the HTTP request. Fires appropriate filter events and hands off to the implementation
     * to perform the actual HTTP request.
     *
     * @param method HTTP request method.
     * @param request {@link HttpRequest} object to configure the request.
     * @return A {@link HttpResponse} object containing the properties of the server response.
     */
    protected HttpResponse run(HttpMethod method, HttpRequest request) {
        HttpContext context = new HttpContext()
        context.setMethod(method)

        HttpEntity entity = request.getEntity()

        // Requests whose client contains a retry filter must have their entity buffered.
        // If it is not, the retried request will either throw an error due to the entity
        // input stream being closed, or the entity will not actually transmit. So, requests
        // that could potentially be retried are automatically bufferedso that it can be
        // transmitted more than once.
        if (entity != null && filterManager.hasRetryFilters()) {
            entity.buffer()
        }

        while (true) {
            if (entity != null && entity.isBuffered()) {
                entity.reset()
            }

            HttpRequest newRequest = request.clone() as HttpRequest
            context.setRequest(newRequest)
            context.setResponse(null)

            filterManager.filterHttpRequest(context)

            // Requests that do not include an entity should still have their
            // {@link HttpClientLifecycleFilter#onRequest} method called. If the request does
            // contain an entity, it is the responsibility of the implementation to make a call
            // to {@link #filterOutputStream}.
            if (!entity) {
                filterManager.onRequest(context, null)
            }

            // Note that {@link HttpClientRequestEntityFilter#filterRequestEntity} and
            // {@link HttpClientLifecycleFilter#onRequest} should be initiated from the
            // client implementation, and will occur during the execution started below.
            HttpResponse response = doExecute(context, entity)

            context.setResponse(response)

            filterManager.filterHttpResponse(context)

            filterManager.onResponse(context)

            if (!filterManager.onRetry(context)) {
                filterManager.onComplete(context)
                return response
            }

            context.incrementRetries()
        }
    }

    /**
     * Filter the output stream.
     *
     * @param context HTTP request context.
     * @param outputStream Output stream of the request.
     */
    protected OutputStream filterOutputStream(HttpContext context, OutputStream outputStream) {
        outputStream = filterManager.filterRequestEntity(context, outputStream)

        filterManager.onRequest(context, outputStream)

        return outputStream
    }

    /**
     * Adds an entity converter to the factory.
     *
     * @param converter Converter to add to the factory.
     */
    void addEntityConverter(EntityConverter converter) {
        converterManager.add(converter)
    }

    /**
     * Returns the list of entity converters.
     *
     * @return List of entity converters.
     */
    List<EntityConverter> getEntityConverters() {
        return converterManager.getAll()
    }

    /**
     * Remove an entity converter.
     *
     * @param converter Entity converter to remove.
     */
    void removeEntityConverter(EntityConverter converter) {
        converterManager.remove(converter)
    }

    /**
     * Remove all entity converters.
     */
    void clearEntityConverters() {
        converterManager.clear()
    }

    /**
     * Create and return an all-trusting TLS {@link SSLContext}.
     *
     * @return An all-trusting TLS {@link SSLContext}.
     */
    protected SSLContext createTrustingSSLContext() {
        TrustManager[] certs = [new X509TrustManager() {
            X509Certificate[] getAcceptedIssuers() {
                null
            }

            void checkClientTrusted(X509Certificate[] certs, String authType) {}

            void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }]

        SSLContext sslContext = SSLContext.getInstance('TLS')
        sslContext.init(null, certs, new SecureRandom())

        return sslContext
    }

    /**
     * Create and return an all-trusting {@link HostnameVerifier}.
     *
     * @return An all-trusting {@link HostnameVerifier}.
     */
    protected HostnameVerifier createTrustingHostnameVerifier() {
        return new HostnameVerifier() {
            boolean verify(String hostname, SSLSession session) {
                return true
            }
        }
    }

    /**
     * Creates an {@link HttpRequest} with all of its dependencies included.
     *
     * @return A new {@link HttpRequest}.
     */
    @Override
    HttpRequest createHttpRequest() {
        return new HttpRequest(converterManager)
    }

    /**
     * Creates an {@link HttpRequest} and configures it with the given closure.
     *
     * @param closure Closure used to configure the request.
     * @return A new, configured {@link HttpRequest}.
     */
    @Override
    HttpRequest createHttpRequest(@DelegatesTo(HttpRequest) Closure closure) {
        HttpRequest request = createHttpRequest()
        closure = closure.clone() as Closure
        closure.delegate = request
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()
        return request
    }
}
