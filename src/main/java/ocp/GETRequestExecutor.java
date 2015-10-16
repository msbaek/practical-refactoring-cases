package ocp;

import java.net.MalformedURLException;
import java.net.URL;

public class GETRequestExecutor extends HttpRequestExecutor {
    @Override
    protected URL createUrl(String requestURI, String paramsString) throws MalformedURLException {
        URL url;
        if(isGet)
			url = new URL(requestURI + paramsString);
		else
			url = new URL(requestURI);
        return url;
    }
}
