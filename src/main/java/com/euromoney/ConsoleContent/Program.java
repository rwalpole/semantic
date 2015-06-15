package com.euromoney.ConsoleContent;

import com.euromoney.semantic.DBpediaReader;
import com.euromoney.semantic.EndpointReader;
import com.euromoney.semantic.QueryService;
import com.euromoney.semantic.ServiceException;

import java.io.IOException;

public class Program {

	/**
	 * Initialises the application in the
	 * console.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final QueryService queryService = new QueryService();
		final EndpointReader endpointReader = new DBpediaReader();
		queryService.setEndpointReader(endpointReader);
		try {
			queryService.askWithSparql("Where was David Cameron born?");
		} catch (ServiceException svex) {
			System.err.println(svex.getMessage());
		}



		// TODO - please see QueryService for functionality

	}

}
