package com.ecommerce.order.httpInterface;

import com.ecommerce.order.exceptions.MicroserviceCommunicationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.nio.charset.StandardCharsets;

@Configuration
public class HttpInterfaceConfig {
    @Bean
    public UserHttpInterface userHttpInterface(@Value("${services.user.url:http://localhost:8082}") String userUrl) {
        return createClient(userUrl, UserHttpInterface.class);
    }

    @Bean
    public ProductHttpInterface productHttpInterface(@Value("${services.product.url:http://localhost:8081}") String productUrl) {
        return createClient(productUrl, ProductHttpInterface.class);
    }

    private <T> T createClient(String baseUrl, Class<T> clientClass) {
        RestClient restClient = RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    String responseBody = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    throw new MicroserviceCommunicationException(
                            response.getStatusCode(),
                            "" + request.getURI(),
                            responseBody
                    );
                }))
                .baseUrl(baseUrl)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(clientClass);
    }
}


