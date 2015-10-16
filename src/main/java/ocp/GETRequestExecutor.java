package ocp;

import java.net.MalformedURLException;
import java.net.URL;

public class GETRequestExecutor extends HttpRequestExecutor {
    @Override
    protected URL createUrl(String requestURI, String paramsString) throws MalformedURLException {
        return new URL(requestURI + paramsString);
    }
}
