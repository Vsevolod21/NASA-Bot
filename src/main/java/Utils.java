import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Utils {
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static ObjectMapper mapper = new ObjectMapper();

    static String getUrl(String nasaUrl) {

        HttpGet request = null;
        CloseableHttpResponse response = null;
        NasaObject answer = null;

        try {
            request = new HttpGet(nasaUrl);
            response = httpClient.execute(request);
            answer = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answer.getUrl();
    }
    static String getExplanation(String nasaUrl) {

        HttpGet request = null;
        CloseableHttpResponse response = null;
        NasaObject answer = null;

        try {
            request = new HttpGet(nasaUrl);
            response = httpClient.execute(request);
            answer = mapper.readValue(response.getEntity().getContent(), NasaObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return answer.getExplanation();
    }

}
