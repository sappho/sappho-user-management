/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import org.openid4java.consumer.ConsumerException;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.message.MessageException;
import uk.org.sappho.openid.user.management.UserManagement;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;
import java.io.IOException;

@Path("/logout")
public class Logout {

    @CookieParam(UserManagement.COOKIE_NAME)
    private String userIdDigest;
    @Context
    ContextResolver<UserManagement> userManagementContextResolver;

    @GET
    public Response logout() throws DiscoveryException, ConsumerException, MessageException, IOException {

        userManagementContextResolver.getContext(UserManagement.class).logoff(userIdDigest);
        return Response.ok().cookie(
                new NewCookie(UserManagement.COOKIE_NAME, null, null, null, null, 0, false)).build();
    }
}
