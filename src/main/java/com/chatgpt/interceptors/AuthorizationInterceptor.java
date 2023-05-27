package com.chatgpt.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private static final String ENCODING = "UTF-8";

    @Value("${auth.client.secret}")
    String clientSecret;

    @Value("${auth.skip}")
    boolean skipAuth;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (skipAuth) return true;

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            boolean isSignSuccess = checkAuthorizationHeader(splitBearer(authorizationHeader));
            if (isSignSuccess) return true;

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return false;
    }

    String splitBearer(String header) {
        return header.substring(7);
    }

    private boolean checkAuthorizationHeader(String url) throws Exception {
        Map<String, String> queryParams = getQueryParams(new URL(url));

        String checkString = queryParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("vk_"))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> encode(entry.getKey()) + "=" + (entry.getValue() == null ? encode("") : encode(entry.getValue())))
                .collect(Collectors.joining("&"));

        String sign = getHashCode(checkString, clientSecret);

        return sign.equals(queryParams.getOrDefault("sign", ""));
    }


    private static Map<String, String> getQueryParams(URL url) {
        String query = url.getQuery();
        if (query == null) return new LinkedHashMap<>();

        final Map<String, String> result = new LinkedHashMap<>();
        final String[] pairs = query.split("&");

        for (String pair:pairs) {
            int index = pair.indexOf("=");
            String key = index > 0 ? decode(pair.substring(0, index)) : pair;
            String value = index > 0 && pair.length() > index + 1 ? decode(pair.substring(index + 1)) : null;
            result.put(key, value);
        }

        return result;
    }

    private static String getHashCode(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key.getBytes(ENCODING), "HmacSHA256"));

        return new String(
                Base64
                        .getUrlEncoder()
                        .withoutPadding()
                        .encode(mac.doFinal(data.getBytes(ENCODING)))
        );
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return value;
    }
}
