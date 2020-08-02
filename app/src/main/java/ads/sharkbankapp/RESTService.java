package ads.sharkbankapp;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RESTService {
    public static String callREST(String restURL, String method, JSONObject
            jsonParam) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(restURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(
                    jsonParam.toString().getBytes().length);
            urlConnection.setRequestProperty(
                    "Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlConnection.connect();
            OutputStream os = new
                    BufferedOutputStream(urlConnection.getOutputStream());
            os.write(jsonParam.toString().getBytes());
            os.flush();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"),
                    8);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + " \n ");
            }
            inputStream.close();
            result = sBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public static String makeGetRequest(String restURL) throws IOException {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(restURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"),
                    8);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + " \n ");
            }
            inputStream.close();
            result = sBuilder.toString();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally  {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}