package com.intuso.housemate.webserver.api.oauth;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tomc on 21/01/17.
 */
@Ignore // requires manual setting of the client id/secret and auth code until they're persisted or automated and we can run the server as part of this test
public class TestOAuthClient {

    private final String CLIENT_ID = "f3a0b38c-4329-444c-b083-834ead9992c1";
    private final String CLIENT_SECRET = "762f32e1-cc32-4f3d-8af3-ef49bb030936";
    private final String AUTH_CODE = "883b7dd7358b6c14f1809ddfdc26bb2b";
    private final String TOKEN = "ad5327e7dd891ec6a99d7396abf8e575";
    private final String DEVICE = "812576b6-c073-4118-8ef0-d04b47ff30cb";

    private final String AUTHORIZE_URL = "http://localhost:8080/api/oauth/1.0/authorize";
    private final String TOKEN_URL = "http://localhost:8080/api/oauth/1.0/token";
    private final String LIST_URL = "http://localhost:8080/api/server/1.0/power?limit=-1";
    private final String ON_URL = "http://localhost:8080/api/server/1.0/power/" + DEVICE + "/on";
    private final String OFF_URL = "http://localhost:8080/api/server/1.0/power/" + DEVICE + "/off";

    @Test
    public void getAuthCode() throws OAuthSystemException {

        OAuthClientRequest authRequest = OAuthClientRequest
                .authorizationLocation(AUTHORIZE_URL)
                .setResponseType(ResponseType.CODE.toString())
                .setClientId(CLIENT_ID)
                .setRedirectURI(LIST_URL)
                .buildQueryMessage();

        System.out.println("Visit: " + authRequest.getLocationUri());
    }

    @Test
    public void getToken() throws OAuthSystemException, OAuthProblemException, IOException {

        //create OAuth client that uses custom http client under the hood
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation(TOKEN_URL)
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectURI("http://www.example.com/redirect")
                .setCode(AUTH_CODE)
                .buildQueryMessage();

        OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);

        System.out.println("Token: " + oAuthResponse.getAccessToken());
    }

    @Test
    public void testList() throws OAuthSystemException, OAuthProblemException, IOException {

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(LIST_URL)
                .setAccessToken(TOKEN)
                .buildQueryMessage();

        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceResponse.getBodyAsInputStream()));
        String line;
        while((line = br.readLine()) != null)
            System.out.println(line);
    }

    @Test
    public void testOn() throws OAuthSystemException, OAuthProblemException, IOException {

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(ON_URL)
                .setAccessToken(TOKEN)
                .buildQueryMessage();

        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.POST, OAuthResourceResponse.class);
        BufferedReader br = new BufferedReader(new InputStreamReader(resourceResponse.getBodyAsInputStream()));
        String line;
        while((line = br.readLine()) != null)
            System.out.println(line);
    }
}
