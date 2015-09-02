package com.ecomap.ukraine.addproblem.client;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Performs multipart request
 */
public class MultipartRequest extends Request<String> {

    private static final int QUALITY_OF_COMPRESION = 50;
    private final Response.Listener<String> listener;
    private final HashMap<String, String> params;
    private HttpEntity httpEntity;
    private MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    private Charset chars = Charset.forName("UTF-8");
    private Bitmap file;
    private int counter = 0;

    /**
     * Constructor
     */
    public MultipartRequest(final String url, final Response.Listener<String> listener,
                            final Response.ErrorListener errorListener,
                            final Bitmap file, final HashMap<String, String> params, int count) {
        super(Method.POST, url, errorListener);

        this.listener = listener;
        if (file != null) {
            this.file = file;
        }
        counter = count;
        this.params = params;
        buildMultipartEntity();
    }

    /**
     * Get headers of request
     * @return headers of request
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (params != null) ? params : super.getHeaders();
    }

    /**
     * Get request body content type
     * @return content type of request body
     */
    @Override
    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }

    /**
     * Gets request body in bytes
     * @return request body in bytes array
     * @throws IOException exception of conversion
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();

    }

    /**
     * Parse response from server
     * @param response response from server
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

    /**
     * Receive response from server
     * @param response response from server
     */
    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

    /**
     * Builds httpEntity for request
     */
    private void buildMultipartEntity() {
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        if (file != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            file.compress(Bitmap.CompressFormat.JPEG, QUALITY_OF_COMPRESION, bos);
            builder.addPart("file[" + counter + "]", new ByteArrayBody(bos.toByteArray(), "image_" + counter));
        }
        builder.setCharset(chars);
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY.withCharset("UTF-8"));
            }
            httpEntity = builder.build();
        } catch (Exception e) {
            Log.e("EncodingException", "Exception");
        }
    }
}