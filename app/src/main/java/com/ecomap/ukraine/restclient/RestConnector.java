package com.ecomap.ukraine.restclient;

import android.app.IntentService;
import android.content.Intent;

import com.ecomap.ukraine.convertion.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Oleh on 7/19/2015.
 */
public class RestConnector extends IntentService {
    Object output;

    public static final String PROBLEMS_REQUEST_URL = "http://ecomap.org/api/problems/";

    public RestConnector() {
        super("RestThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String respond;
        try {
            switch (intent.getIntExtra("RequestType", 0)) {
                case RequestTypes.ALL_PROBLEMS:
                    respond = getServerResponse(PROBLEMS_REQUEST_URL);
                    output = new JSONParser()
                            .parseBriefProblems(respond);
                    break;

                case RequestTypes.PROBLEM_DETAIL:
                    output = new JSONParser()
                            .parseDetailedProblem(getServerResponse(PROBLEMS_REQUEST_URL)
                                    + intent.getStringExtra("Parameters"));
                    break;
            }
        } catch (Exception e) {
            output = null;
        } finally {
            DataManager.getInstance()
                    .notifyListeners(intent.getIntExtra("RequestType", 0), output);
        }

    }

    private String getServerResponse(String requestURL) throws IOException {
        URL url = new URL(requestURL);
        URLConnection connection = url.openConnection();
        BufferedReader input = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
