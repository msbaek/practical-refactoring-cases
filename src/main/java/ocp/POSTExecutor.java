package ocp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class POSTExecutor extends HttpRequestExecutor {
    @Override
    protected URL createUrl(String requestURI, String paramsString) throws MalformedURLException {
        return new URL(requestURI);
    }

    @Override
    protected void additionalWorkWith(String paramsString) throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(urlConnection.getOutputStream());
        bufferedOutputStream.write(paramsString.getBytes(outputEncoding));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    @Override
    protected void setAdditionalConnectionSettings() {
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
    }
}
