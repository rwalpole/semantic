package com.euromoney.semantic;

/**
 * Created by rob on 10/06/15.
 */
public class QueryService {

    private EndpointReader endpointReader;

    /**
     * Returns the place of birth, if available, for a named person.
     * This method attempts to extract the person's name based on it's
     * presumed position within the text.
     *
     * @param question
     * @return
     * @throws ServiceException
     */
    public String ask(final String question) throws ServiceException {
        final String lcQuestion = question.toLowerCase();
        if(lcQuestion.contains("birth place of")) {
            final String name = extractName(question, "birth place of", "?", 14);
            return getEndpointReader().extractResult(name.replace(" ", "_"));
        }
        else if(lcQuestion.contains("where was") && lcQuestion.contains("born?")) {
            final String name = extractName(question, "where was", "born?", 9);
            return getEndpointReader().extractResult(name.replace(" ", "_"));

        } else {
            return "Sorry, I do not understand your question";
        }
    }

    /**
     * Returns the place of birth, if available, for a named person.
     * This method attempts to extract the person's name based on it's
     * presumed position within the text.
     *
     * Once the SPARQL Protocol query is function correctly this method
     * could be refactored to remove commonality with the ask method (above).
     *
     * @param question
     * @return
     * @throws ServiceException
     */
    public void askWithSparql(final String question) throws ServiceException {
        // Example questions..
        // What is the birth place of David Cameron?
        // Where was David Cameron born?
        final String lcQuestion = question.toLowerCase();
        if(lcQuestion.contains("birth place of")) {
            final String name = extractName(question, "birth place of", "?", 14);
            //return getEndpointReader().extractResultWithSparql(name.replace(" ", "_"));
            getEndpointReader().extractResultWithSparql(name.replace(" ", "_"));
        }
        else if(lcQuestion.contains("where was") && lcQuestion.contains("born?")) {
            final String name = extractName(question, "where was", "born?", 9);
            //return getEndpointReader().extractResultWithSparql(name.replace(" ", "_"));

        } else {
            //return "Sorry, I do not understand your question";
        }
    }

    private String extractName(final String question, final String startPhrase, final String endPhrase, final int startPhraseLength) {
        int start = question.toLowerCase().indexOf(startPhrase) + startPhraseLength;
        int end = question.toLowerCase().indexOf(endPhrase);
        return question.substring(start, end).trim();
    }

    public void setEndpointReader(final EndpointReader endpointReader) {
        this.endpointReader = endpointReader;
    }

    public EndpointReader getEndpointReader() {
        return endpointReader;
    }






}
