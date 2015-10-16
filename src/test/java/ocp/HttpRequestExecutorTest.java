package ocp;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.function.BooleanSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringStartsWith.startsWith;

public class HttpRequestExecutorTest {
    private String requestURI = "http://www.daum.net";
    private Map<String,String> params;
    private Boolean isGet = true;
    private HttpRequestExecutor executor;

    @Before
    public void setUp() throws Exception {
        executor = new HttpRequestExecutor();
    }

    @Test
    public void get() throws IOException {
        ResponseModel responseModel = executor.handleRequest(isGet, requestURI, params);
        assertThat(responseModel.getResponseAsText(), startsWith("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"utf-8\"><title>Daum &ndash; 모으다 잇다 흔들다</title"));
    }

    @Test
    public void post() throws IOException {
        isGet = false;
        ResponseModel responseModel = executor.handleRequest(isGet, requestURI, params);
        assertThat(responseModel.getResponseAsText(), startsWith("<!DOCTYPE html><html lang=\"ko\"><head><meta charset=\"utf-8\"><title>Daum &ndash; 모으다 잇다 흔들다</title"));
    }
}