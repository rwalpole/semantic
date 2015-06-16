package com.euromoney.semantic;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * Created by rob on 16/06/15.
 */
public class Vocabulary {

    final static String SPARQL_RESULT_URI = "http://www.w3.org/2005/sparql-results#";

    final static Model MODEL = createModel();

    final static Property VARIABLE_PROPERTY = MODEL.createProperty(SPARQL_RESULT_URI, "variable");
    final static Property VALUE_PROPERTY = MODEL.createProperty(SPARQL_RESULT_URI, "value");

    public static Model createModel() {
		return ModelFactory.createDefaultModel();
        //model.setNsPrefix("", SPARQL_RESULT_URI);
        //model.setNsPrefix("dcterms", DCTerms.getURI());
        //return model;
    }
}
