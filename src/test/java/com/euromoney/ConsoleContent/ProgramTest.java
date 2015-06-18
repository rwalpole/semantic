package com.euromoney.ConsoleContent;

import java.io.*;
import java.util.Scanner;

import com.euromoney.semantic.QueryService;
import org.junit.Test;

public class ProgramTest {

	@Test
	public final void testMain() throws IOException {
		final Writer out = new StringWriter();
		final Writer err = new StringWriter();
		final Reader in = new StringReader("Hello");

		//Program.execute(new Scanner(in), new PrintStream(), new PrintStream());

	}

}
