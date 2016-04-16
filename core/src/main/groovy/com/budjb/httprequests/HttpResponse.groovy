package com.budjb.httprequests

import groovy.json.JsonSlurper

/**
 * An object that represents the response of an HTTP request.
 *
 * If the response contains an entity, and {@link HttpRequest#autoBufferEntity} is <code>true</code>, the response
 * entity is read from the response and the {@link #inputStream} is automatically closed. If
 * {@link HttpRequest#autoBufferEntity} is <code>false</code>, the {@link #inputStream} can be used to read the
 * response entity. In the latter case, it is important to call {@link #close} so that the underlying resources
 * can be freed up.
 */
class HttpResponse implements Closeable {
    /**
     * The HTTP status of the response.
     */
    int status

    /**
     * Content type of the response.
     */
    String contentType

    /**
     * Headers of the response.
     */
    private Map<String, List<String>> headers = [:]

    /**
     * Entity of the response.
     */
    private byte[] entity

    /**
     * The character set of the response.
     */
    String charset = 'UTF-8'

    /**
     * Input stream of the response.
     */
    InputStream inputStream

    /**
     * Request properties used to configure the request that generated this response.
     */
    HttpRequest request

    /**
     * Constructor.
     *
     * @param request Request properties used to configure the request that generated this response.
     */
    HttpResponse(HttpRequest request) {
        this.request = request
    }

    /**
     * Sets the character set of the response.
     *
     * @param charset Character set of the response.
     */
    void setCharset(String charset) {
        if (charset) {
            this.charset = charset
        }
    }

    /**
     * Return the entity of the response as a <code>String</code>.
     *
     * @return The entity of the response converted to a <code>String</code>.
     */
    String getEntityAsString() {
        return new String(getEntity(), charset)
    }

    /**
     * Parses the entity of the response as JSON and returns the resulting object.
     *
     * @return The entity of the response parsed as JSON.
     */
    Object getEntityAsJson() {
        return new JsonSlurper().parse(getEntity(), charset)
    }

    /**
     * Sets the response headers from the given map.
     *
     * @param headers Response headers of the request.
     */
    void setHeaders(Map<String, ?> headers) {
        headers.each { name, values ->
            setHeader(name, values)
        }
    }

    /**
     * Sets the response header with the given name, parsing out individual values.
     *
     * @param name Name of the header.
     * @param value Value(s) of the header.
     */
    void setHeader(String name, Object value) {
        if (!headers.containsKey(name)) {
            headers.put(name, [])
        }

        if (value instanceof Collection) {
            value.each {
                headers.get(name).add(it.toString())
            }
        }
        else {
            value.toString().split(/,\s*/).each {
                headers.get(name).add(it.toString())
            }
        }
    }

    /**
     * Returns the first value of the header with the given name, or null if it doesn't exist.
     *
     * @param name Name of the header.
     * @return The first value of the requested header, or null if it doesn't exist.
     */
    String getHeader(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name).first()
        }
        return null
    }

    /**
     * Returns a list of values of the header with the given name, or null if the header doesn't exist.
     *
     * @param name Name of the header.
     * @return A list of values of the requested header, or null if it doesn't exist.
     */
    List<String> getHeaders(String name) {
        if (headers.containsKey(name)) {
            return headers.get(name)
        }
        return null
    }

    /**
     * Return all response headers.
     *
     * This method returns a map where the key is the name of the header, and the value is a list of values
     * for the response header. This is true even when there is only one value for the header.
     *
     * @return A copy of the map containing the response headers.
     */
    Map<String, List<String>> getHeaders() {
        return headers.clone() as Map<String, List<String>>
    }

    /**
     * Return all response headers.
     *
     * This method returns a map where the key is the name of the header, and the value is either the single value
     * for that header or a list of values if multiple were received.
     *
     * @return A copy of the map containing the response headers.
     */
    Map<String, Object> getFlattenedHeaders() {
        return headers.collectEntries { name, values ->
            if (values.size() == 1) {
                return [(name): values[0]]
            }
            else {
                return [(name): values]
            }
        } as Map<String, Object>
    }

    /**
     * Returns the entity as a byte array. Note that calling this method will automatically close the input stream.
     *
     * @return The entity of the response as a byte array.
     */
    byte[] getEntity() {
        if (entity == null) {
            bufferEntity()
        }

        return entity
    }

    /**
     * Sets the byte array entity of the response.
     *
     * Note that setting the byte array entity directly will close the input stream (if it was open) and thus discard
     * the stream's contents.
     *
     * @param entity Byte array entity to store in the response.
     */
    void setEntity(byte[] entity) {
        close()
        this.entity = entity
    }

    /**
     * Sets the input stream of the entity of the response.
     *
     * If {@link HttpRequest#autoBufferEntity} is <code>true</code>, the input stream is read and closed,
     * with its contents stored in {@link #entity} as a byte array.
     *
     * @param inputStream Input stream from the HTTP client response.
     */
    void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream

        if (request.getAutoBufferEntity()) {
            bufferEntity()
        }
    }

    /**
     * Closes the input stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    void close() throws IOException {
        inputStream?.close()
    }

    /**
     * Reads the contents of the entity from the input stream and stores it as a byte array.
     */
    protected void bufferEntity() {
        if (!inputStream) {
            return
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()

        int read
        byte[] tmp = new byte[8192]

        while ((read = inputStream.read(tmp, 0, 1892)) != -1) {
            outputStream.write(tmp, 0, read)
        }

        outputStream.flush()
        entity = outputStream.toByteArray()
        inputStream.close()
    }

    /**
     * Returns whether the response contains an entity.
     *
     * @return Whether the response contains an entity.
     */
    boolean hasEntity() {
        return inputStream != null || entity != null
    }
}