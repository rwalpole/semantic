package com.euromoney.semantic;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * PLEASE NOTE - this is more of an integration test than a unit test as I want to show that the
 * QueryService works. Normally I would create separate integration and unit tests, with the unit
 * test using a mock EndpointReader so that only the QueryService is actually tested.
 *
 * Created by Rob Walpole on 10/06/15.
 */
public class QueryServiceTest {

    private static String expectedCameron = "London, United Kingdom";
    private static String expectedBlair = "Edinburgh, Scotland";

    // Class under test
    private QueryService queryService;

    private QueryHandler queryHandler = new QueryHandler();

    @Before
    public void setUp() {
        queryService = new QueryService();
        queryService.setQueryHandler(queryHandler);
    }

    /* Remove Ignore to see problem outstanding issue with SPARQL Protocol request */
    //@Ignore
    @Test
    public void testQuestion1WithSparql() throws Exception {
        String result = queryService.ask("What is the birth place of David Cameron?");
        assertEquals(expectedCameron,result);
    }


    /* Remove Ignore to see problem outstanding issue with SPARQL Protocol request */
    @Ignore
    @Test
    public void testQuestion2WithSparql() throws Exception {
        String result = queryService.ask("Where was David Cameron born?");
        assertEquals(expectedCameron,result);
    }

    /* Trying a different name */
    @Ignore
    @Test
    public void testQuestion3() throws Exception {
        String result = queryService.ask("Where was Tony Blair" + "born?");
        assertEquals(expectedBlair, result);
    }

    @Ignore
    @Test
    public void testQuestion4() throws Exception {
        String result = queryService.ask("When was David Cameron born?");
        assertEquals("Sorry, I do not understand your question", result);
    }



}
