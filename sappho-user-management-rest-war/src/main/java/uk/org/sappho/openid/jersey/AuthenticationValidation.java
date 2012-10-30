/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import uk.org.sappho.openid.user.management.UserManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

@Path("/authentication-validation")
public class AuthenticationValidation {

    @Context
    private ContextResolver<ConsumerManager> consumerManagerContextResolver;
    @Context
    ContextResolver<UserManagement> userManagementContextResolver;
    @Context
    private HttpServletRequest httpServletRequest;

    @GET
    public Response processLogin()
            throws DiscoveryException, AssociationException, MessageException, IOException, NoSuchAlgorithmException {

        HttpSession httpSession = httpServletRequest.getSession();
        LoginInformation loginInformation =
                (LoginInformation) httpSession.getAttribute("sappho-user-management-authentication-information");
        Response.ResponseBuilder responseBuilder = Response.seeOther(URI.create(loginInformation.getServiceUrl()));
        ConsumerManager consumerManager = consumerManagerContextResolver.getContext(ConsumerManager.class);
        DiscoveryInformation discoveryInformation = loginInformation.getDiscoveryInformation();
        String requestUrl = httpServletRequest.getRequestURL().toString();
        ParameterList parameterList = new ParameterList(httpServletRequest.getParameterMap());
        VerificationResult verificationResult =
                consumerManager.verify(requestUrl, parameterList, discoveryInformation);
        Identifier verifiedId = verificationResult.getVerifiedId();
        if (verifiedId != null) {
            String userId = verifiedId.getIdentifier();
            String userIdDigest =
                    userManagementContextResolver.getContext(UserManagement.class).getNewUserIdDigest(userId);
            responseBuilder = responseBuilder.cookie(new NewCookie(UserManagement.COOKIE_NAME, userIdDigest));
        }
        return responseBuilder.build();
    }
}
