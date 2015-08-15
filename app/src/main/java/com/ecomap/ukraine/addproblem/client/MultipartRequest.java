package com.ecomap.ukraine.addproblem.client;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.ContentType;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

public class MultipartRequest extends Request<String> {

    private static String REQUEST_HEADER = "POST /api/problempost HTTP/1.1\n" +
            "Host: localhost:8090\n" +
            "Cache-Control: no-cache";

    private static String PARAM_TEMPLATE = "\n" +
            "\n" +
            "----WebKitFormBoundaryE19zNvXGzXaLvS5C\n" +
            "Content-Disposition: form-data; name=\"";


    private HttpEntity mHttpEntity;
    private MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    Charset chars = Charset.forName("UTF-8");

    private static final String FILE_PART_NAME = "file";

    private final Response.Listener<String> mListener;
    private  ArrayList<Bitmap> mFiles = new ArrayList<>();
    private final HashMap<String, String> params;

    public MultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, ArrayList<Bitmap> files, HashMap<String, String> params) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        if (mFiles != null) {
            mFiles = files;
        }
        this.params = params;
        buildMultipartEntity();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return params != null ? params : super.getHeaders();
    }


    private void buildMultipartEntity() {
       // entity.setCharset(chars);
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);



        if(mFiles!=null) {
            int i = 0;
            for (Bitmap image : mFiles) {

        /*Compress bitmap*/
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 50, bos);

                builder.addPart("file[" + i + "]", new ByteArrayBody(bos.toByteArray(), "image_" + i));
                i++;
            }
        }
        builder.setCharset(chars);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(),entry.getValue(), ContentType.DEFAULT_BINARY);
        }
            mHttpEntity = builder.build();
        } catch (Exception e) {
            Log.e("EncodingException", "Exception");
        }
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //DataOutputStream dataOutputStream = new DataOutputStream(bos);
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();



        }

    /**
     * copied from Android StringRequest class
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}