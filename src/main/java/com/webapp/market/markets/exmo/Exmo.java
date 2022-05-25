package com.webapp.market.markets.exmo;

import com.webapp.market.exceptions.ExmoException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Exmo {

    private static long nonce;

    public Exmo() {
        this.nonce = System.currentTimeMillis() + 1119621336617600L;
    }

    public static String request(String method, Map<String, String> arguments) throws ExmoException {

        if (arguments == null) {  // If the user provided no arguments, just create an empty argument array.
            arguments = new HashMap<>();
        }

        arguments.put("nonce", "" + ++nonce);  // Add the dummy nonce.

        String postData = "";

        for (Map.Entry<String, String> argument : arguments.entrySet()) {
            //Map.Entry argument = (Map.Entry) stringStringEntry;

            if (postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }

        // Now do the actual request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.exmo.com/v1.1/" + method))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Key", Security.getPublicKey())
                .header("Sign", Security.signRest(postData))
                .method("POST",HttpRequest.BodyPublishers.ofString(postData))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body().contains("error"))
                throw new ExmoException(LocalTime.now() + " - " + method + "\n\t" + postData + "\n\t" + response.body());

            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
