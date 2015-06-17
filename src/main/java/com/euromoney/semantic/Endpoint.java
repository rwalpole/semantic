package com.euromoney.semantic;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by rob on 17/06/15.
 */
public class Endpoint {

    private HttpURLConnection connection;

    final static String DBPEDIA_BASE_URI = "http://dbpedia.org";
    final static String DBPEDIA_SPARQL_URI = DBPEDIA_BASE_URI + "/sparql";

    private String sparqlQuery;

    public void openConnection() throws ServiceException {
        try{
            final URL url = new URL(getUrl());
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException ioex) {
            throw new ServiceException(ioex.getMessage());
        }

    }

    public void setDoOutput(final boolean doOutput) {
        connection.setDoOutput(doOutput);
    }

    public void setSparqlQuery(final String sparqlQuery) {
        this.sparqlQuery = sparqlQuery;
    }

    public String getSparqlQuery() {
        return sparqlQuery;
    }

    public void setRequestMethod(final String method) throws ServiceException {
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException prex) {
            throw new ServiceException(prex.getMessage());
        }
    }

    public InputStream getInputStream() throws ServiceException {
        try {
            return connection.getInputStream();
        } catch (IOException ioex) {
            throw new ServiceException(ioex.getMessage());
        }
    }

    private String getUrl() throws ServiceException {
        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(DBPEDIA_SPARQL_URI);
        urlBuilder.append("?default-graph-uri=" + DBPEDIA_BASE_URI);
        urlBuilder.append("&query=" + encodeUrl(getSparqlQuery()));
        urlBuilder.append("&format=text/turtle");
        return urlBuilder.toString();
    }

    private String encodeUrl(final String url) throws ServiceException {
        try {
            final String encodedUrl = URLEncoder.encode(url, "UTF-8");
            return encodedUrl;
        } catch (UnsupportedEncodingException unex) {
            throw new ServiceException(unex.getMessage());
        }
    }
}
