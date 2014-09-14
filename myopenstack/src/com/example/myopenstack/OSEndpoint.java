package com.example.myopenstack;

public class OSEndpoint {
	private static OSClient client = null;
	public static OSClient get() {
		if (client == null) {
			client = new OSClient();
		}
		return client;
	}
}
