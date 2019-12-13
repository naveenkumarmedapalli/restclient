package com.streams.definitions;

public class GetTokenRequest {

	 /**
     * Phone number linked to an account or extension in E.164 format with or without leading '+' sign
     */
    public String username;
    /**
     * User's password
     */
    public String password;
    /**
     * Grant type
     * Default: password
     * Enum: authorization_code, password, refresh_token, client_credentials
     */
    public String grant_type;
    
    
    public GetTokenRequest username(String username) {
        this.username = username;
        return this;
    }

    public GetTokenRequest password(String password) {
        this.password = password;
        return this;
    }

    public GetTokenRequest grant_type(String grant_type) {
        this.grant_type = grant_type;
        return this;
    }

}
