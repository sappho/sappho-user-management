/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.MessageException;
import uk.org.sappho.openid.user.management.UserManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;
import java.net.URI;

@Path("/authentication")
public class Authentication {

    @Context
    private ContextResolver<ConsumerManager> consumerManagerContextResolver;
    @Context
    private HttpServletRequest httpServletRequest;

    @GET
    public Response loginFromGet(@QueryParam("id") String id, @QueryParam("url") String serviceUrl)
            throws DiscoveryException, ConsumerException, MessageException, IOException {

        return login(id, serviceUrl);
    }

    @POST
    public Response loginFromPost(@FormParam("id") String id, @FormParam("url") String serviceUrl)
            throws DiscoveryException, ConsumerException, MessageException, IOException {

        return login(id, serviceUrl);
    }

    public Response login(String id, String serviceUrl)
            throws DiscoveryException, ConsumerException, MessageException, IOException {

        HttpSession httpSession = httpServletRequest.getSession();
        ConsumerManager consumerManager = consumerManagerContextResolver.getContext(ConsumerManager.class);
        DiscoveryInformation discoveryInformation = consumerManager.associate(consumerManager.discover(id));
        String responseUrl = httpServletRequest.getRequestURL().toString() + "-validation";
        String url = consumerManager.authenticate(discoveryInformation, responseUrl).getDestinationUrl(true);
        httpSession.setAttribute("sappho-user-management-authentication-information",
                new LoginInformation(discoveryInformation, serviceUrl));
        return Response.seeOther(URI.create(url)).cookie(
                new NewCookie(UserManagement.COOKIE_NAME, null, null, null, null, 0, false)).build();
    }
}
