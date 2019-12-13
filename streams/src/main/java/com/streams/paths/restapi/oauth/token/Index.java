package com.streams.paths.restapi.oauth.token;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.streams.ContentType;
import com.streams.RestClient;
import com.streams.definitions.GetTokenRequest;
import com.streams.definitions.TokenInfo;

import okhttp3.ResponseBody;

//@Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\000,\n\002\030\002\n\002\020\000\n\000\n\002\030\002\n\002\b\004\n\002\030\002\n\002\b\005\n\002\020\016\n\000\n\002\030\002\n\000\n\002\030\002\n\000\030\0002\0020\001B\r\022\006\020\002\032\0020\003\006\002\020\004J\006\020\r\032\0020\016J\020\020\017\032\004\030\0010\0202\006\020\021\032\0020\022R\021\020\002\032\0020\003\006\b\n\000\032\004\b\005\020\006R\032\020\007\032\0020\bX\016\006\016\n\000\032\004\b\t\020\n\"\004\b\013\020\f\006\023"}, d2 = {"Lcom/ringcentral/paths/restapi/oauth/token/Index;", "", "parent", "Lcom/ringcentral/paths/restapi/oauth/Index;", "(Lcom/ringcentral/paths/restapi/oauth/Index;)V", "getParent", "()Lcom/ringcentral/paths/restapi/oauth/Index;", "rc", "Lcom/ringcentral/RestClient;", "getRc", "()Lcom/ringcentral/RestClient;", "setRc", "(Lcom/ringcentral/RestClient;)V", "path", "", "post", "Lcom/ringcentral/definitions/TokenInfo;", "getTokenRequest", "Lcom/ringcentral/definitions/GetTokenRequest;", "ringcentral-java"})
public final class Index {

	private RestClient rc;

	public final Index getParent() {
		return this.parent;
	}

	private final Index parent;

	public Index(Index parent) {
		this.parent = parent;
		this.rc = this.parent.getRc();
	}

	public final RestClient getRc() {
		return this.rc;
	}

	public final void setRc(RestClient _rc) { 
		  this.rc = _rc;
		  }

	public final String path() {
		return this.parent.path() + "/token";
	}

	public final TokenInfo post(GetTokenRequest getTokenRequest) throws IOException {
		ResponseBody rb = this.rc.post(path(), getTokenRequest, null, ContentType.FORM);
		return (TokenInfo) JSON.parseObject(rb.string(), TokenInfo.class);
	}
}
