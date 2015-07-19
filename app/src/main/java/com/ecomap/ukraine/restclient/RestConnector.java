package com.ecomap.ukraine.restclient;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

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
        ResultReceiver receiver = null;
        int requestType = 0;
        try {
            String temp;
            receiver = intent.getParcelableExtra("Receiver");
            requestType = intent.getIntExtra("RequestType", 0);

            switch (requestType) {
                case RequestTypes.ALL_PROBLEMS:
                    temp = getServerResponse(PROBLEMS_REQUEST_URL);
                    output = new JSONParser()
                            .parseBriefProblems(temp);
                    break;

                case RequestTypes.PROBLEM_DETAIL:
                    temp = getServerResponse(PROBLEMS_REQUEST_URL
                            + intent.getStringExtra("Parameters"));
                    output = new JSONParser()
                            .parseDetailedProblem(getServerResponse(temp));
                    break;
            }
        } catch (Exception e) {
            output = null;
        } finally {
            DataManager.getInstance().setRequestResult(output);
            try {
                receiver.send(requestType, new Bundle());

            } catch (Exception e) {
                String s = e.toString();
            }
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
