package ocp;

import java.io.*;
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

		URL url = new URL(requestURI + paramsString);
		urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setReadTimeout(DEFAULT_SOCKET_TIMEOUT_SEC);
		urlConnection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_SEC);

		InputStream inputStream = urlConnection.getInputStream();
		String responseBody = convertInputStreamToString(inputStream);

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

	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, inputEncoding));
		String line;
		StringBuffer result = new StringBuffer();

		while ((line = bufferedReader.readLine()) != null)
			result.append(line);

		close(bufferedReader);
		close(inputStream);

		return result.toString();
	}

	private void close(Closeable closeable) throws IOException {
		if (closeable != null) {
			closeable.close();
		}
	}
}
