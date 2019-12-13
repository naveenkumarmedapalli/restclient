package com.streams.paths.restapi;

import com.streams.RestClient;

public final class Index {
	private final RestClient rc;

	public final RestClient getRc() {
		return this.rc;
	}

	private final String apiVersion;

	public final String getApiVersion() {
		return this.apiVersion;
	}

	public Index(RestClient rc, String apiVersion) {
		this.rc = rc;
		this.apiVersion = apiVersion;
	}

	

	public final String path(boolean withParameter) {
		if (withParameter && this.apiVersion != null) {
			return "/restapi/" + this.apiVersion;
		}

		return "/restapi";
	}
}
