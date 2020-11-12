package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.revinate.assertj.json.JsonPathAssert;
import java.util.UUID;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void demoEndpoint() {

		var responseEntity = testRestTemplate.getForEntity("/", String.class);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		final DocumentContext documentContext = JsonPath.parse(responseEntity.getBody());

		JsonPathAssert.assertThat(documentContext)
				.jsonPathAsString("$.tip")
				.isEqualTo("Do It Yourself!");
		JsonPathAssert.assertThat(documentContext)
				.jsonPathAsString("$.random")
				.is(uuidString());

	}

	@Test
	void actuatorBuildInfo() {
		var responseEntity = testRestTemplate.getForEntity("/actuator/info", String.class);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonPathAssert.assertThat(JsonPath.parse(responseEntity.getBody()))
				.jsonPathAsString("$.build.version").isEqualTo("0.0.1-SNAPSHOT");
	}

	@Test
	void actuatorHealth() {
		var responseEntity = testRestTemplate.getForEntity("/actuator/health", String.class);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		JsonPathAssert.assertThat(JsonPath.parse(responseEntity.getBody()))
				.jsonPathAsString("$.status").isEqualTo("UP");
	}

	private static Condition<String> uuidString() {
		return new Condition<>(value -> {
			try {
				UUID.fromString(value);
				return true;
			} catch (IllegalArgumentException e) {
				return false;
			}
		}, "uuid-string");
	}

}
