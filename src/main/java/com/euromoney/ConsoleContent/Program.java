package com.euromoney.ConsoleContent;

import com.euromoney.semantic.Endpoint;
import com.euromoney.semantic.QueryHandler;
import com.euromoney.semantic.QueryService;
import com.euromoney.semantic.ServiceException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Program {

	/**
	 * Initialises the application in the
	 * console.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		execute(new Scanner(System.in), System.out, System.err);
	}

	public static void execute(final Scanner in, final PrintStream out, final PrintStream err) {
		final QueryService queryService = new QueryService();
		final QueryHandler queryHandler = new QueryHandler();
		final Endpoint endpoint = new Endpoint();
		queryHandler.setEndpoint(endpoint);
		queryService.setQueryHandler(queryHandler);
		out.println("Enter a question, e.g. 'Where was David Cameron born?' or type quit to exit..");
		String question = in.nextLine();
		do{
			try{
				out.println(queryService.ask(question));
			} catch (ServiceException svex) {
				err.println("Sorry, there was an error: " + svex.getMessage());
			}
			question = in.nextLine();
		} while (!question.equals("quit"));
	}

}
