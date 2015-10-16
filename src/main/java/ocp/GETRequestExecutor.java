package ocp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GETRequestExecutor extends HttpRequestExecutor {
    @Override
    protected URL createUrl(String requestURI, String paramsString) throws MalformedURLException {
        return new URL(requestURI + paramsString);
    }

    @Override
    protected void additionalWorkWith(String paramsString) throws IOException {
    }

    @Override
    protected void setAdditionalConnectionSettings() {
    }
}
