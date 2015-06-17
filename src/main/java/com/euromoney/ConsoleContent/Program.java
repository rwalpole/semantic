package com.euromoney.ConsoleContent;

import com.euromoney.semantic.Endpoint;
import com.euromoney.semantic.QueryHandler;
import com.euromoney.semantic.QueryService;
import com.euromoney.semantic.ServiceException;

import java.io.IOException;
import java.util.Scanner;

public class Program {

	/**
	 * Initialises the application in the
	 * console.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final QueryService queryService = new QueryService();
		final QueryHandler queryHandler = new QueryHandler();
		final Endpoint endpoint = new Endpoint();
		queryHandler.setEndpoint(endpoint);
		queryService.setQueryHandler(queryHandler);
		System.out.println("Enter a question, e.g. 'Where was David Cameron born?' or type quit to exit..");
		final Scanner input = new Scanner(System.in);
		String question = input.nextLine();
		do{
			try{
				System.out.println(queryService.ask(question));
			} catch (ServiceException svex) {
				System.err.println("Sorry, there was an error: " + svex.getMessage());
			}
			question = input.nextLine();
		} while (!question.equals("quit"));

	}

}
