package eu.number26.challenge;

public class Main {

	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		new N26Server(port).start();
	}
}
