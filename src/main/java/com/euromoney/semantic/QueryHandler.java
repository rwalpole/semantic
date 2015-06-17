package com.euromoney.semantic;

import com.hp.hpl.jena.rdf.model.*;

import java.util.List;

/**
 * Created by Rob Walpole on 10/06/15.
 */
public class QueryHandler {

    private Endpoint endpoint;

    public String extractResultWithSparql(final String resourceName) throws ServiceException {
        getEndpoint().setSparqlQuery(getSparqlQuery(resourceName));
        getEndpoint().openConnection();
        getEndpoint().setRequestMethod("GET");
        getEndpoint().setDoOutput(true);

        final Model model = ModelFactory.createDefaultModel();
        model.read(getEndpoint().getInputStream(),null,"TURTLE");

        final String town = extractValue("town", model);
        final String country = extractValue("country", model);
        if(town.length() == 0) {
            return "Sorry, no town of birth found.";
        } else if(country.length() == 0) {
            return "Place of birth is " + town + " but country unknown.";
        } else{
            return town + ", " + country;
        }
    }

    private String extractValue(final String varName, final Model model) {
        final SimpleSelector selector = new SimpleSelector(null, Vocabulary.VARIABLE_PROPERTY, varName);
        final List<Statement> statements = model.listStatements(selector).toList();
        if(!statements.isEmpty()) {
            final Resource townResource = statements.get(0).getSubject();
            final List<Statement> valueStatements = model.listStatements(townResource, Vocabulary.VALUE_PROPERTY, (String)null).toList();
            if(!valueStatements.isEmpty()) {
                return valueStatements.get(0).getObject().asLiteral().toString();
            } else {
                return "";
            }
        } else {
            return "";
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

    public Endpoint getEndpoint() {
        return endpoint;
    }

    //@Override
    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }


}
