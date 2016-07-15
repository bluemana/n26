package eu.number26.challenge;

import eu.number26.challenge.core.Context;

public class Main {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		new N26Server(new Context(), port).start();
	}
}
