package io.paymentservice.testhelpers.apiexecutor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public abstract class AbstractRequestExecutor {

	protected static ExtractableResponse<Response> doPost(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().post(urlPath)
			.then().log().all()
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPost(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().post(urlPath)
			.then().log().all()
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPostWithOk(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().post(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPostWithCreated(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().post(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.CREATED.value())
			.extract();
	}

	protected static ExtractableResponse<Response> doGet(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().get(urlPath)
			.then().log().all()
			.extract();
	}



	protected static ExtractableResponse<Response> doGetWithOk(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().get(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}



	protected static ExtractableResponse<Response> doPut(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().put(urlPath)
			.then().log().all()
			.extract();
	}


	protected static <T> ExtractableResponse<Response> doPut(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().put(urlPath)
			.then().log().all()
			.extract();
	}

	protected static ExtractableResponse<Response> doPutWithOk(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().put(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}


	protected static <T> ExtractableResponse<Response> doPutWithOk(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().put(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPatch(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().patch(urlPath)
			.then().log().all()
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPatch(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().patch(urlPath)
			.then().log().all()
			.extract();
	}

	protected static <T> ExtractableResponse<Response> doPatchWithOk(RequestSpecification requestSpecification, String urlPath, T requestBody) {
		return requestSpecification
			.body(requestBody)
			.when().patch(urlPath)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract();
	}

	protected static ExtractableResponse<Response> doDelete(RequestSpecification requestSpecification, String urlPath) {
		return requestSpecification
			.when().delete(urlPath)
			.then().log().all()
			.extract();
	}

	protected static <T> Map<String, Object> convertDtoToMap(T dto) {
		Map<String, Object> queryParams = new HashMap<>();
		for (Field field : dto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object value = field.get(dto);
				if (value != null) {
					queryParams.put(field.getName(), value);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Failed to access field value", e);
			}
		}
		return queryParams;
	}

}



