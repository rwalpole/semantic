package com.euromoney.semantic;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * An implementation of EndpointReader specifically for DBpedia
 *
 * Created by Rob Walpole on 10/06/15.
 */
public class DBpediaReader implements EndpointReader {

    final static String DBPEDIA_BASE_URI = "http://dbpedia.org/";
    final static String DBPEDIA_DATA_URI = DBPEDIA_BASE_URI + "data/";
    final static String DBPEDIA_SPARQL_URI = DBPEDIA_BASE_URI + "sparql";
    final static String DBPEDIA_RESOURCE_URI = DBPEDIA_BASE_URI + "resource/";
    final static String DBPEDIA_PROPERTY_URI = DBPEDIA_BASE_URI + "property/";
    final static String DBPEDIA_ONTOLOGY_URI = DBPEDIA_BASE_URI + "ontology/";

    public String extractResult(final String resourceName) throws ServiceException {
        final Model model = getModel(resourceName);
        final Property birthPlaceProperty = model.createProperty(DBPEDIA_PROPERTY_URI, "birthPlace");
        SimpleSelector selector = new SimpleSelector(null, birthPlaceProperty, (String)null);
        List<Statement> statements = model.listStatements(selector).toList();
        if(statements.size() > 0) {
            Statement statement = statements.get(0); // FIXME - there could be more than one result!
            return statement.getObject().asLiteral().getString();
        } else {
            return "Sorry, no answer found.";
        }
    }

    private Model getModel(final String resourceName) throws ServiceException {
        final String resourceUrl = DBPEDIA_DATA_URI + resourceName + ".n3";
        Model resultModel = null;
        try {
            resultModel = RDFDataMgr.loadModel(resourceUrl, Lang.N3);
        }
        catch(Exception ex) {
            throw new ServiceException(ex.getMessage());
        }
        return resultModel;
    }


    /* This was an attempt to answer the problem using the SPARQL Protocol directly, rather than
        using the Jena RDFDataMgr - however I hit upon what may be a URI encoding problem or
        possibly a bug in either Virtuoso or Jersey which prevented me from completing it
        quickly. Hopefully you can see what I was trying to do though..
     */
//    public String extractResultWithSparql(final String resourceName) throws ServiceException {
//        final Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(DBPEDIA_SPARQL_URI)
//                .queryParam("default-graph-uri", "http://dbpedia.org")
//                .queryParam("query", getQueryPrefixes() + getSparqlQuery(resourceName))
//                .queryParam("format", "text/turtle");
//
//        final Invocation.Builder builder = target.request();
//        //builder.header("Content-Type", "application/sparql-query; charset=UTF-8");
//        String result = builder.get(String.class);
//        Model model = ModelFactory.createDefaultModel();
//        model.read(result,"TURTLE");
//        final Property valueProperty = model.createProperty("http://www.w3.org/2005/sparql-results#", "value");
//        SimpleSelector selector = new SimpleSelector(null, valueProperty, (String)null);
//        List<Statement> statements = model.listStatements(selector).toList();
//        Statement statement = statements.get(0); // FIXME - there could be more than one result!
//        return statement.getObject().asLiteral().getString();
//    }

    private String getUrl(final String resourceName) throws ServiceException {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(DBPEDIA_SPARQL_URI);
        urlBuilder.append("?default-graph-uri=http://dbpedia.org");
        urlBuilder.append("&query=" + encodeUrl(getSparqlQuery(resourceName)));
        urlBuilder.append("&format=text/turtle");
        return urlBuilder.toString();
    }


    public String extractResultWithSparql(final String resourceName) throws ServiceException {
        try {
            final URL url = new URL(getUrl(resourceName));
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            Model model = ModelFactory.createDefaultModel();
            model.read(getRdfString(conn),"TURTLE");
            final Property valueProperty = model.createProperty("http://www.w3.org/2005/sparql-results#", "value");
            SimpleSelector selector = new SimpleSelector(null, valueProperty, (String)null);
            List<Statement> statements = model.listStatements(selector).toList();
            Statement statement = statements.get(0); // FIXME - there could be more than one result!
            return statement.getObject().asLiteral().getString();


            //byte[]
            //ByteArrayInputStream is = new ByteArrayInputStream(conn.getInputStream());
            //PrintWriter out = new PrintWriter(conn.getOutputStream().write(););
            //String name = "name="+URLEncoder.encode("myname", "UTF-8");
            //String email = "email="+URLEncoder.encode("email@email.com", "UTF-8");
            //out.println(name+"&"+email);
            //out.close();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            in.read()
//
//
//            String line;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
//
//            Mo
        } catch (IOException mfex) {
            throw new ServiceException(mfex.getMessage());
        }

    }

    private String encodeUrl(final String url) throws ServiceException {
        try {
            // Seems like angle brackets are not being encoded properly. The query works fine in the browser.
            final String encodedUrl = URLEncoder.encode(url, "UTF-8");
            return encodedUrl;
        } catch (UnsupportedEncodingException unex) {
            throw new ServiceException(unex.getMessage());
        }
    }

    private String getSparqlQuery(final String resourceName) {
        StringBuilder sparqlBuilder = new StringBuilder("select ?place where ");
        sparqlBuilder.append("{ dbpedia:" + resourceName);
        sparqlBuilder.append(" dbpprop:birthPlace ");
        sparqlBuilder.append("?place }");
        String result = sparqlBuilder.toString();
        return result;
    }

    private String getQueryPrefixes() {
        StringBuilder prefixBuilder = new StringBuilder();
        prefixBuilder.append("prefix dbpedia: <" + DBPEDIA_RESOURCE_URI + "> ");
        prefixBuilder.append("prefix dbpedia-owl: <" + DBPEDIA_ONTOLOGY_URI + "> ");
        prefixBuilder.append("prefix dbpprop: <" + DBPEDIA_PROPERTY_URI + "> ");
        prefixBuilder.append("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        return prefixBuilder.toString();
    }

    private String getRdfString(final HttpURLConnection conn) {
        String rdfResult = "";
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            byte[] buf = new byte[1024];
            // = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            InputStream is = conn.getInputStream();
            for (int readNum; (readNum = is.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
            //Model model = ModelFactory.createDefaultModel();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            rdfResult = bos.toString();
            //esult = bos.toByteArray();
        }
        catch (IOException ioex) {
            ioex.printStackTrace(); // TODO
        }
        finally {
            if(fis != null) {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ex) {};
                }
                if(bos != null) {
                    try {
                        bos.close();
                    } catch (IOException ex) {};
                }
            }
        }
        return rdfResult;
    }






}
