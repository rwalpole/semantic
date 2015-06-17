package com.euromoney.semantic;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by rob on 17/06/15.
 */
public class QueryHandlerTest {

    // Class under test
    private QueryHandler queryHandler;

    @Before
    public void setUp() {
        queryHandler = new QueryHandler();
        Endpoint endpoint = new Endpoint();
        queryHandler.setEndpoint(endpoint);
    }

    @Test
    public void test() throws Exception {
        String result = queryHandler.extractResultWithSparql("David_Cameron");
        assertEquals("London, United Kingdom", result);
    }
}
