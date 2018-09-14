package com.webcabi.snowlinebot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class WebApiClientConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder()
				.additionalInterceptors(Collections.singletonList(new LoggingClientHttpRequestInterceptor())).build();
	}
	
	@Bean
    public RestTemplate zipCodeSearchRestTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder()
				.additionalInterceptors(Collections.singletonList(new LoggingClientHttpRequestInterceptor())).build();
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>(messageConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(MediaType.TEXT_PLAIN); // text/plainのJacksonの処理対象にくわえる
        messageConverter.setSupportedMediaTypes(supportedMediaTypes);
        restTemplate.setMessageConverters(Collections.singletonList(messageConverter)); // カスタムしたHttpMessageConverterを適用
        return restTemplate;
    }

	@Slf4j
	private static class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
				ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
			log.info("Request: URI={}, Headers={}, Body={}", httpRequest.getURI(), httpRequest.getHeaders(),
					new String(bytes));
			ClientHttpResponse response = new BufferingClientHttpResponseWrapper(
					clientHttpRequestExecution.execute(httpRequest, bytes));
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("Api failed. Response: Status=" + response.getStatusCode() + ", Body="
						+ StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
			}
			log.info("Response: Body={}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
			return response;
		}
	}

	private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
		private final ClientHttpResponse response;
		private byte[] body;

		public BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
			this.response = response;
		}

		public HttpStatus getStatusCode() throws IOException {
			return this.response.getStatusCode();
		}

		public int getRawStatusCode() throws IOException {
			return this.response.getRawStatusCode();
		}

		public String getStatusText() throws IOException {
			return this.response.getStatusText();
		}

		public HttpHeaders getHeaders() {
			return this.response.getHeaders();
		}

		public InputStream getBody() throws IOException {
			if (this.body == null) {
				this.body = StreamUtils.copyToByteArray(this.response.getBody());
			}
			return new ByteArrayInputStream(this.body);
		}

		public void close() {
			this.response.close();
		}
	}
}
