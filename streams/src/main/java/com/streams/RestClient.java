package com.streams;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.streams.definitions.GetTokenRequest;
import com.streams.definitions.TokenInfo;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RestClient {

	public String clientId;
	public String clientSecret;
	public String server;
	public OkHttpClient httpClient;
	public TokenInfo token;

	private static final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
	private static final MediaType textMediaType = MediaType.parse("text/plain; charset=utf-8");

	public List<HttpEventListener> httpEventListeners = new ArrayList<>();

	public RestClient(String clientId, String clientSecret, String server, OkHttpClient okHttpClient) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.server = server;
		if (okHttpClient == null) {
			this.httpClient = new OkHttpClient();
		} else {
			this.httpClient = okHttpClient;
		}
	}

	public RestClient(String clientId, String clientSecret, String server) {
		this(clientId, clientSecret, server, null);
	}

	private String basicKey() {
		return new String(
				Base64.getEncoder().encode(MessageFormat.format("{0}:{1}", clientId, clientSecret).getBytes()));
	}

	public TokenInfo authorize(String username, String password, String granttype) throws IOException {
		GetTokenRequest getTokenRequest = new GetTokenRequest().grant_type("password").username(username)
				.password(password);
		return authorize(getTokenRequest);
	}

	public TokenInfo authorize(GetTokenRequest getTokenRequest) throws IOException {
		token = null;

        //token = this.restapi(null).oauth().token().post(getTokenRequest);
		
		return token;
	}

	public ResponseBody post(String endpoint) throws IOException {
		return request(HttpMethod.POST, endpoint, null, null, ContentType.JSON);
	}

	public ResponseBody post(String endpoint, Object object) throws IOException {
		return request(HttpMethod.POST, endpoint, null, object, ContentType.JSON);
	}

	public ResponseBody post(String endpoint, Object object, Object queryParameters) throws IOException {
		return request(HttpMethod.POST, endpoint, queryParameters, object, ContentType.JSON);
	}

	public ResponseBody post(String endpoint, Object object, Object queryParameters, ContentType contentType)
			throws IOException {
		return request(HttpMethod.POST, endpoint, queryParameters, object, contentType);
	}

	public ResponseBody request(HttpMethod httpMethod, String endpoint, Object queryParameters, Object body,
			ContentType contentType) throws IOException {
		RequestBody requestBody = null;
		switch (contentType) {
		case JSON:
			if (body != null && body.getClass().equals(String.class)) { // PUT text
				requestBody = RequestBody.create(textMediaType, (String) body);
			} else {
				requestBody = RequestBody.create(jsonMediaType, body == null ? "" : JSON.toJSONString(body));
			}
			break;
		default:
			break;
		}
		return request(httpMethod, endpoint, queryParameters, requestBody);
	}

	public ResponseBody request(HttpMethod httpMethod, String endpoint, Object queryParameters, RequestBody requestBody)
			throws IOException {
		Response response = requestRaw(httpMethod, endpoint, queryParameters, requestBody);
		return response.peekBody(Long.MAX_VALUE);
	}

	public Response requestRaw(HttpMethod httpMethod, String endpoint, Object queryParameters, RequestBody requestBody)
			throws IOException {
		HttpUrl.Builder urlBuilder = HttpUrl.parse(server).newBuilder(endpoint);

		if (queryParameters != null) {
			for (Field field : queryParameters.getClass().getFields()) {
				Object value = null;
				try {
					value = field.get(queryParameters);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (value != null) {
					if (value.getClass().isArray()) { // ?a=hello&a=world
						for (int i = 0; i < Array.getLength(value); i++) {
							urlBuilder = urlBuilder.addQueryParameter(field.getName(), Array.get(value, i).toString());
						}
					} else {
						urlBuilder = urlBuilder.addQueryParameter(field.getName(), value.toString());
					}
				}
			}
		}

		HttpUrl httpUrl = urlBuilder.build();

		Request.Builder builder = new Request.Builder().url(httpUrl);
		switch (httpMethod) {
		case GET:
			builder = builder.get();
			break;
		case POST:
			builder = builder.post(requestBody);
			break;
		case PUT:
			builder = builder.put(requestBody);
			break;
		case DELETE:
			builder = builder.delete();
			break;
		case PATCH:
			builder = builder.patch(requestBody);
			break;
		default:
			break;
		}

		String userAgentHeader = String.format("RC-JAVA-SDK Java %s %s", System.getProperty("java.version"),
				System.getProperty("os.name"));
		Request request = builder.addHeader("Authorization", authorizationHeader())
				.addHeader("X-User-Agent", userAgentHeader).build();

		Response response = httpClient.newCall(request).execute();
		int statusCode = response.code();
		if (statusCode < 200 || statusCode > 299) {
			System.out.println("finally got the access token ....." + response);
		}
		for (HttpEventListener httpEventListener : httpEventListeners) {
			httpEventListener.afterHttpCall(response, request);
		}
		return response;
	}

	private String authorizationHeader() {
		if (token != null) {
			return MessageFormat.format("Bearer {0}", token.access_token);
		}
		return MessageFormat.format("Basic {0}", basicKey());
	}
	// top level paths
    public com.streams.paths.restapi.Index restapi(String apiVersion) {
        return new com.streams.paths.restapi.Index(this, apiVersion);
    }

    public com.streams.paths.restapi.Index restapi() {
        return new com.streams.paths.restapi.Index(this, "v1.0");
    }
}
