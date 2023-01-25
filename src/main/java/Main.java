import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    //my key - https://api.nasa.gov/planetary/apod?api_key=fjH0ouD0XDl4pWuOFBM0hDSfKjr4SHXA0TRcopho
    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=fjH0ouD0XDl4pWuOFBM0hDSfKjr4SHXA0TRcopho";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        CloseableHttpResponse response = httpClient.execute(request);
        NASA nasa = mapper.readValue(response.getEntity().getContent(), NASA.class);
        System.out.println(nasa);

        HttpGet request2 = new HttpGet(nasa.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);

        String[] nasaSplit = nasa.getUrl().split("/");
        String requiredFile = nasaSplit[6];
        HttpEntity entity = response2.getEntity();
        if (entity != null) {
            FileOutputStream fos = new FileOutputStream(requiredFile);
            entity.writeTo(fos);
            fos.close();
        }
    }
}
