package dev.lysmux.fcgi;

import com.google.gson.Gson;
import dev.lysmux.fcgi.annotations.Param;
import dev.lysmux.fcgi.annotations.RouteMapping;
import dev.lysmux.fcgi.dto.Request;
import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.fcgi.dto.Route;
import dev.lysmux.fcgi.enums.HTTPMethod;
import dev.lysmux.fcgi.enums.HTTPParamType;
import dev.lysmux.fcgi.exception.RouteNotFoundException;
import dev.lysmux.fcgi.param.parser.ObjectParser;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class Router {
    @Getter
    @Setter
    private Router parent;
    private final List<Router> routers = new ArrayList<>();
    private final String prefix;
    @Getter
    private final List<Route> routes = new ArrayList<>();

    {
        prefix = Optional.ofNullable(this.getClass().getAnnotation(RouteMapping.class))
                .map(RouteMapping::path)
                .orElse("/");

        collectRoutes();
    }

    private void collectRoutes() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            RouteMapping mapping = method.getAnnotation(RouteMapping.class);
            if (mapping == null) continue;

            if (
                    Arrays.stream(method.getParameters())
                            .filter(param -> param.getType().isRecord())
                            .count() > 1
            ) throw new IllegalArgumentException("Router method can have only one record parameter");

            String path = mapping.path();
            HTTPMethod[] httpMethods = mapping.methods();

            if (path.contains("?")) {
                throw new IllegalArgumentException("Invalid path format");
            }

            for (HTTPMethod httpMethod : httpMethods) {
                routes.add(new Route(httpMethod, path, mapping.status(), method, this));
            }
        }
    }

    public <T extends Router> void includeRouter(T router) {
        if (router.getParent() != null) throw new IllegalArgumentException("Router already has registered");

        router.setParent(this);
        routers.add(router);
    }

    private static String combineUrl(String... parts) {
        StringBuilder url = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (i > 0 && part.startsWith("/")) part = part.substring(1);
            url.append(part);
        }

        return url.toString();
    }

    private Route resolveRoute(Request request) {
        List<Route> routes = this.routes.stream()
                .filter(route -> route.method() == request.method())
                .toList();

        StringTokenizer requestPathTokenizer = new StringTokenizer(request.path(), "/");

        M:
        for (Route route : routes) {
            String path = combineUrl(prefix, route.path());
            StringTokenizer tokenizer = new StringTokenizer(path, "/");

            if (tokenizer.countTokens() != requestPathTokenizer.countTokens()) continue;

            for (int i = 0; i < tokenizer.countTokens(); i++) {
                String token = tokenizer.nextToken();
                String requestToken = requestPathTokenizer.nextToken();

                if (requestToken.startsWith(":")) continue;
                if (!token.equalsIgnoreCase(requestToken)) continue M;
            }

            return route;
        }

        return null;
    }

    private Object[] resolveArgs(Request request, Route route) {
        Object[] args = new Object[route.handler().getParameterCount()];

        for (int i = 0; i < args.length; i++) {
            Parameter param = route.handler().getParameters()[i];
            Class<?> paramType = param.getType();

            String paramName = param.getName();
            HTTPParamType httpParamType = HTTPParamType.JSON;

            if (param.isAnnotationPresent(Param.class)) {
                Param annotation = param.getAnnotation(Param.class);
                paramName = annotation.alias().isEmpty() ? paramName : annotation.alias();
                httpParamType = annotation.type();
            }

            Object arg = null;

            switch (httpParamType) {
                case JSON:
                    arg = ObjectParser.parseJson(paramName, paramType, request.body());
                    break;
                case FORM:
                    break;
                case PATH:
                    arg = ObjectParser.parsePath(paramName, paramType, route.path(), request.path());
                    break;
                case QUERY:
                    arg = ObjectParser.parseQuery(paramName, paramType, request.queryParams());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported param type");
            }

            args[i] = arg;
        }

        return args;
    }

    public Response route(Request request) {
        Route route = resolveRoute(request);
        if (route == null) {
            for (Router router : routers) {
                route = router.resolveRoute(request);
            }
        }

        if (route == null) {
            throw new RouteNotFoundException(request.path());
        }

        try {
            Object[] args = resolveArgs(request, route);
            return toResponse(route, route.handler().invoke(route.controller(), args));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Response toResponse(Route route, Object response) {
        String message;
        String contentType = "text/plain";

        if (response instanceof Response) {
            return (Response) response;
        } else if (response instanceof Record) {
            message = new Gson().toJson(response);
            contentType = "application/json";
        } else {
            message = response.toString();
        }
        return Response.builder()
                .status(route.status())
                .contentType(contentType)
                .body(message)
                .build();
    }
}
