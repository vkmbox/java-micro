package vkmbox.micro.lib.errors;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FeignErrorDecoder implements ErrorDecoder {
    
    private static final String FAIL_TO_PROCESS_BODY = "Failed to process response body";

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpHeaders headers = new HttpHeaders();
        response.headers().entrySet().stream()
            .forEach(entry -> headers.put(entry.getKey(), new ArrayList<>(entry.getValue())));        
        
        HttpStatus status = HttpStatus.resolve(response.status());
        byte[] body = null;
        if (response.body() != null) {
            try (InputStream is = response.body().asInputStream()) {
                body = is.readAllBytes();
            } catch (IOException ex) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, FAIL_TO_PROCESS_BODY);
            }
        }

        if (status == null) {
            return errorStatus(response, headers, body);
        }
        
        if (response.status() >= 400 && response.status() <= 499) {
            return new HttpClientErrorException(status, response.reason(), headers, body, null);
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new HttpServerErrorException(status, response.reason(), headers, body, null);
        }
        return errorStatus(response, headers, body);
    }
    
    public Exception errorStatus(Response response, HttpHeaders headers, byte[] body) {
        return new UnknownHttpStatusCodeException(response.status()
            , response.reason(), headers, body, null);
    }
    
}
