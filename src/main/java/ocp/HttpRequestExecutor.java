package ocp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class HttpRequestExecutor {
	public static final int DEFAULT_CONNECT_TIMEOUT_SEC = 10000;
	public static final int DEFAULT_SOCKET_TIMEOUT_SEC = 30000;
	private static final String inputEncoding = "UTF-8";
	private HttpURLConnection urlConnection;

	public ResponseModel handleRequest(String requestURI, Map<String, String> params) throws IOException {
		String paramsString = getParamsString(params);

		urlConnection = openConnection(requestURI, paramsString);

		initDefaultSetting(DEFAULT_SOCKET_TIMEOUT_SEC);

		String responseBody = getBody(urlConnection);

		urlConnection.disconnect();

		return new ResponseModel(requestURI, responseBody);
	}

	private String getParamsString(Map<String, String> params) {
		String paramsString = "";
		if (params != null) {
			Set<String> keySet = params.keySet();
			paramsString += "?";
			for (String key : keySet) {
				paramsString += key + "=" + params.get(key) + "&";
			}
		}

		return paramsString;
	}

	private HttpURLConnection openConnection(String requestURI, String paramsString) throws IOException {
		URL url = new URL(requestURI + paramsString);
		return (HttpURLConnection) url.openConnection();
	}

	private void initDefaultSetting(int socketTimeout) {
		urlConnection.setReadTimeout(socketTimeout);
		urlConnection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_SEC);
		urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
	}

	private String getBody(HttpURLConnection urlConnection) throws IOException {
		InputStream inputStream = urlConnection.getInputStream();
		return convertInputStreamToString(inputStream);
	}

	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream, inputEncoding));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null) {
			result += line;
		}

		if (bufferedReader != null) {
			bufferedReader.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		return result;
	}
}
