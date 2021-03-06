package com.github.scribejava.apis;

import com.github.scribejava.apis.service.DoktornaraboteOAuthServiceImpl;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.utils.OAuthEncoder;
import com.github.scribejava.core.utils.Preconditions;

public class DoktornaraboteApi extends DefaultApi20 {

    private static final String AUTHORIZE_URL
            = "http://auth.doktornarabote.ru/OAuth/Authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    private static final String TOKEN_URL = "http://auth.doktornarabote.ru/OAuth/Token";

    protected DoktornaraboteApi() {
    }

    private static class InstanceHolder {
        private static final DoktornaraboteApi INSTANCE = new DoktornaraboteApi();
    }

    public static DoktornaraboteApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        Preconditions.checkValidUrl(
            config.getCallback(),
            "Must provide a valid url as callback. Doktornarabote does not support OOB");
        final StringBuilder sb = new StringBuilder(
            String.format(
                AUTHORIZE_URL,
                config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()),
                OAuthEncoder.encode(config.getScope())
            )
        );

        final String state = config.getState();
        if (state != null) {
            sb.append('&').append(OAuthConstants.STATE).append('=').append(OAuthEncoder.encode(state));
        }
        return sb.toString();
    }

    @Override
    public OAuth20Service createService(OAuthConfig config) {
        return new DoktornaraboteOAuthServiceImpl(this, config);
    }
}
