package com.euromoney.semantic;

import com.hp.hpl.jena.rdf.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by rob on 17/06/15.
 */
public class QueryHandlerTest {

    // Class under test
    private QueryHandler queryHandler;

    // Mocks
    private Endpoint mockEndpoint = mock(Endpoint.class);
    private Model mockModel = mock(Model.class);
    private StmtIterator mockStmtIterator = mock(StmtIterator.class);
    private Statement mockStatement = mock(Statement.class);
    private Resource mockResource = mock(Resource.class);
    private RDFNode mockNode = mock(RDFNode.class);
    private Literal mockLiteral = mock(Literal.class);

    private List<Statement> statementList;

    @Before
    public void setUp() {
        queryHandler = new QueryHandler();
        queryHandler.setEndpoint(mockEndpoint);
        queryHandler.setModel(mockModel);
        statementList = new ArrayList<>();
        statementList.add(mockStatement);
    }

    @Test
    public void testExtract() throws Exception {
        when(mockModel.listStatements(any(Selector.class))).thenReturn(mockStmtIterator);
        when(mockStmtIterator.toList()).thenReturn(statementList);
        when(mockStatement.getSubject()).thenReturn(mockResource);
        when(mockModel.listStatements(eq(mockResource), eq(Vocabulary.VALUE_PROPERTY), isNull(String.class))).thenReturn(mockStmtIterator);
        when(mockStatement.getObject()).thenReturn(mockNode);
        when(mockNode.asLiteral()).thenReturn(mockLiteral);
        when(mockLiteral.toString()).thenReturn("London","United Kingdom");
        String result = queryHandler.runQuery("David_Cameron");
        assertEquals("London, United Kingdom", result);
    }

}
