/*
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 * Copyright: Etive Technologies Ltd. 2016 (www.etive.org). All rights reserved.
 * Created  : 02-Dec-2016
 * Author   : Craig, (c dot speedie at etive dot org)
 * Enquiries: please send any enquiries to hello at etive dot org
 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */
package org.etive.city4age.withings;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.AbstractRequest;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthConstants;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;

/**
 * Extension of the {@code com.github.scribejava.core.builder.api.DefaultApi10a} class from ScribeJava.
 * @author Craig
 */
public class WithingsApi extends DefaultApi10a {

    protected static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    /** Used in step 1 of the authorisation dance. */
    private static final String REQUEST_TOKEN_ENDPOINT = "https://oauth.withings.com/account/request_token";
    /** Used in step 2 of the authorisation dance. */
    private static final String AUTHORISE_URL = "https://oauth.withings.com/account/authorize?oauth_consumer_key=";
    /** Used in step 3 of the authorisation dance. */
    private static final String ACCESS_TOKEN_ENDPOINT = "https://oauth.withings.com/account/access_token";
    /** The unique Withings Consumer Key for Developer Account support@digitallogbook.org. */
    public static final String CONS_KEY = System.getenv("CONS_KEY");
    /** The unique Withings Consumer Secret for Developer Account support@digitallogbook.org. */
    public static final String CONS_SECRET=System.getenv("CONS_SECRET");
    /** The URI to where the user will be returned after allowing the app access to Withings data.  */
    public static final String CALLBACK = "http://www.digitallogbook.org/dlb/app/c4a/auth/response";
    /** The method used for the OAuth signature generation. */
    private static final String SIG_METHOD = "HMAC-SHA1";
    /** The OAuth version number. */
    private static final String OAUTH_VERSION = "1.0";

    protected WithingsApi() {
    }

    private static class InstanceHolder {
        private static final WithingsApi INSTANCE = new WithingsApi();
    }

    public static WithingsApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_ENDPOINT;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_ENDPOINT;
    }

    @Override
    public String getAuthorizationUrl(final OAuth1RequestToken requestToken) {
        final String nonce = getTimestampService().getNonce();
        final String timestamp = getTimestampService().getTimestampInSeconds();
        final StringBuilder sb = new StringBuilder();
        sb.append(AUTHORISE_URL);
        sb.append("?oauth_consumer_key=");
        sb.append(CONS_KEY);
        sb.append("&oauth_nonce=");
        sb.append(nonce);
        sb.append("&oauth_signature=");
        sb.append(getSignature(requestToken, nonce, timestamp));
        sb.append("&oauth_signature_method=");
        sb.append(SIG_METHOD);
        sb.append("&oauth_timestamp=");
        sb.append(timestamp);
        sb.append("&oauth_token=");
        sb.append(requestToken.getToken());
        sb.append("&oauth_version=");
        sb.append(OAUTH_VERSION);
        return sb.toString();
    }

    /**
     *  A modified version of the private method in {@code com.github.scribejava.core.oauth.OAuth10aService}.
     *  @param tokenSecret The secret token from a request token response.
     */
    private String getSignature(final OAuth1RequestToken token, final String nonce, final String timestamp) {
        final AbstractRequest request = new OAuthRequest(Verb.GET, AUTHORISE_URL, null);
        request.addOAuthParameter(OAuthConstants.CONSUMER_KEY, CONS_KEY);
        request.addOAuthParameter(OAuthConstants.NONCE, nonce);
        request.addOAuthParameter(OAuthConstants.SIGN_METHOD, SIG_METHOD);
        request.addOAuthParameter(OAuthConstants.TIMESTAMP, timestamp);
        request.addOAuthParameter(OAuthConstants.TOKEN, token.getToken());
        request.addOAuthParameter(OAuthConstants.VERSION, OAUTH_VERSION);
        final String baseString = getBaseStringExtractor().extract(request);
        final String signature = getSignatureService().getSignature(baseString, CONS_SECRET, token.getTokenSecret());
        return signature;
    }

}
