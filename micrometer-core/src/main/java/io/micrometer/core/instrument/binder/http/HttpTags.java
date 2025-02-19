/**
 * Copyright 2021 VMware, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.binder.http;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.transport.http.HttpRequest;
import io.micrometer.core.instrument.transport.http.HttpResponse;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * Utility class providing convenience methods to generate tags for HTTP metrics based on
 * the {@link HttpRequest} and {@link HttpResponse} abstraction.
 *
 * @author Jon Schneider
 * @since 2.0.0
 */
public class HttpTags {
    private static final Tag EXCEPTION_NONE = Tag.of("exception", "None");

    private static final Tag STATUS_UNKNOWN = Tag.of("status", "UNKNOWN");

    private static final Tag METHOD_UNKNOWN = Tag.of("method", "UNKNOWN");

    private static final Tag OUTCOME_UNKNOWN = Tag.of("outcome", "UNKNOWN");

    private static final Tag URI_UNKNOWN = Tag.of("uri", "UNKNOWN");

    private HttpTags() {
    }

    /**
     * Creates a {@code method} tag based on the {@link HttpRequest#method()
     * method} of the given {@code request}.
     * @param request the request
     * @return the method tag whose value is a capitalized method (e.g. GET).
     */
    public static Tag method(HttpRequest request) {
        return (request != null) ? Tag.of("method", request.method()) : METHOD_UNKNOWN;
    }

    /**
     * Creates a {@code status} tag based on the status of the given {@code response}.
     * @param response the HTTP response
     * @return the status tag derived from the status of the response
     */
    public static Tag status(HttpResponse response) {
        return (response != null) ? Tag.of("status", Integer.toString(response.statusCode())) : STATUS_UNKNOWN;
    }

    /**
     * Creates a {@code exception} tag based on the {@link Class#getSimpleName() simple
     * name} of the class of the given {@code exception}.
     * @param exception the exception, may be {@code null}
     * @return the exception tag derived from the exception
     */
    public static Tag exception(Throwable exception) {
        if (exception != null) {
            String simpleName = exception.getClass().getSimpleName();
            return Tag.of("exception", StringUtils.isNotBlank(simpleName) ? simpleName : exception.getClass().getName());
        }
        return EXCEPTION_NONE;
    }

    /**
     * Creates an {@code outcome} tag based on the status of the given {@code response}.
     * @param response the HTTP response
     * @return the outcome tag derived from the status of the response
     */
    public static Tag outcome(HttpResponse response) {
        return (response != null) ? Outcome.forStatus(response.statusCode()).asTag() : OUTCOME_UNKNOWN;
    }

    public static Tag uri(HttpRequest request) {
        String uri = request.route();
        return uri == null ? URI_UNKNOWN : Tag.of("uri", uri);
    }
}
