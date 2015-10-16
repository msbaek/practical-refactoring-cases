package ocp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public abstract class HttpRequestExecutor {
	public static final int DEFAULT_CONNECT_TIMEOUT_SEC = 10000;
	public static final int DEFAULT_SOCKET_TIMEOUT_SEC = 30000;
	private static final String inputEncoding = "UTF-8";
	private static final String outputEncoding = "UTF-8";
	private HttpURLConnection urlConnection;
    private Boolean isGet;

    public ResponseModel handleRequest(Boolean isGet, String requestURI, Map<String, String> params) throws IOException {
        this.isGet = isGet;

		String paramsString = getParamsString(params);

		URL url = createUrl(requestURI, paramsString);

        setDefaultConnectionSettings(url);

        setAdditionalConnectionSettings();

        additionalWorkWith(paramsString);

        String responseBody = getResponseBody();

		urlConnection.disconnect();

		return new ResponseModel(requestURI, responseBody);
	}

    private boolean isPOST() {
        return !isGet;
    }

    private String getResponseBody() throws IOException {
        InputStream inputStream = urlConnection.getInputStream();
        return convertInputStreamToString(inputStream);
    }

    private void additionalWorkWith(String paramsString) throws IOException {
        if(!isPOST())
            return;
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(urlConnection.getOutputStream());
        bufferedOutputStream.write(paramsString.getBytes(outputEncoding));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    private void setAdditionalConnectionSettings() {
        if(!isPOST())
            return;
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
    }

    private void setDefaultConnectionSettings(URL url) throws IOException {
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(DEFAULT_SOCKET_TIMEOUT_SEC);
        urlConnection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT_SEC);
    }

    protected abstract URL createUrl(String requestURI, String paramsString) throws MalformedURLException;

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
