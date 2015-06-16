package com.euromoney.semantic;

import com.hp.hpl.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
    final static String DBPEDIA_PROPERTY_URI = DBPEDIA_BASE_URI + "property/";

//    public String extractResult(final String resourceName) throws ServiceException {
//        final Model model = getModel(resourceName);
//        final Property birthPlaceProperty = model.createProperty(DBPEDIA_PROPERTY_URI, "birthPlace");
//        SimpleSelector selector = new SimpleSelector(null, birthPlaceProperty, (String)null);
//        List<Statement> statements = model.listStatements(selector).toList();
//        if(statements.size() > 0) {
//            Statement statement = statements.get(0); // FIXME - there could be more than one result!
//            return statement.getObject().asLiteral().getString();
//        } else {
//            return "Sorry, no answer found.";
//        }
//    }

//    private Model getModel(final String resourceName) throws ServiceException {
//        final String resourceUrl = DBPEDIA_DATA_URI + resourceName + ".n3";
//        Model resultModel = null;
//        try {
//            resultModel = RDFDataMgr.loadModel(resourceUrl, Lang.N3);
//        }
//        catch(Exception ex) {
//            throw new ServiceException(ex.getMessage());
//        }
//        return resultModel;
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

            final Model model = ModelFactory.createDefaultModel();
            model.read(conn.getInputStream(),null,"TURTLE");

            final String town = extractValue("town", model);
            final String country = extractValue("country", model);
            return town + ", " + country;
        } catch (IOException mfex) {
            throw new ServiceException(mfex.getMessage());
        }

    }

    private String extractValue(final String varName, final Model model) {
        SimpleSelector selector = new SimpleSelector(null, Vocabulary.VARIABLE_PROPERTY, varName);
        List<Statement> statements = model.listStatements(selector).toList();
        Resource townResource = statements.get(0).getSubject();
        List<Statement> valueStatements = model.listStatements(townResource, Vocabulary.VALUE_PROPERTY, (String)null).toList();
        return valueStatements.get(0).getObject().asLiteral().toString();
    }


    private String encodeUrl(final String url) throws ServiceException {
        try {
            final String encodedUrl = URLEncoder.encode(url, "UTF-8");
            return encodedUrl;
        } catch (UnsupportedEncodingException unex) {
            throw new ServiceException(unex.getMessage());
        }
    }

    private String getSparqlQuery(final String resourceName) {
        final StringBuilder sparqlBuilder = new StringBuilder();
        sparqlBuilder.append("select (str(?townName) as ?town) (str(?countryName) as ?country)");
        sparqlBuilder.append("where {");
        sparqlBuilder.append("?place rdfs:label ?townName .");
        sparqlBuilder.append("filter(langMatches(lang(?townName), \"EN\"))");
        sparqlBuilder.append("?place dbpedia-owl:country ?country .");
        sparqlBuilder.append("?country rdfs:label ?countryName .");
        sparqlBuilder.append("filter(langMatches(lang(?countryName), \"EN\"))");
        sparqlBuilder.append("{select ?place ");
        sparqlBuilder.append("where {");
        sparqlBuilder.append("dbpedia:" + resourceName + " dbpedia-owl:birthPlace ?place .");
        sparqlBuilder.append("?place a dbpedia-owl:Settlement .");
        sparqlBuilder.append("}}}");
        final String sparql = sparqlBuilder.toString();
        return sparql;
    }

//    private String getQueryPrefixes() {
//        StringBuilder prefixBuilder = new StringBuilder();
//        prefixBuilder.append("prefix dbpedia: <" + DBPEDIA_RESOURCE_URI + "> ");
//        prefixBuilder.append("prefix dbpedia-owl: <" + DBPEDIA_ONTOLOGY_URI + "> ");
//        prefixBuilder.append("prefix dbpprop: <" + DBPEDIA_PROPERTY_URI + "> ");
//        prefixBuilder.append("prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
//        return prefixBuilder.toString();
//    }

//    private String getRdfString(final HttpURLConnection conn) {
//        String rdfResult = "";
//        FileInputStream fis = null;
//        ByteArrayOutputStream bos = null;
//
//        try {
//            byte[] buf = new byte[1024];
//            // = new FileInputStream(file);
//            bos = new ByteArrayOutputStream();
//            InputStream is = conn.getInputStream();
//            for (int readNum; (readNum = is.read(buf)) != -1;) {
//                bos.write(buf, 0, readNum); //no doubt here is 0
//                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
//                System.out.println("read " + readNum + " bytes,");
//            }
//            //Model model = ModelFactory.createDefaultModel();
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            rdfResult = bos.toString();
//            //esult = bos.toByteArray();
//        }
//        catch (IOException ioex) {
//            ioex.printStackTrace(); // TODO
//        }
//        finally {
//            if(fis != null) {
//                if(fis != null) {
//                    try {
//                        fis.close();
//                    } catch (IOException ex) {};
//                }
//                if(bos != null) {
//                    try {
//                        bos.close();
//                    } catch (IOException ex) {};
//                }
//            }
//        }
//        return rdfResult;
//    }






}
