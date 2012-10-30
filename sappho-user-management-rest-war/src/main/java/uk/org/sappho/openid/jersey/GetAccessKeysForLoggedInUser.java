/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import uk.org.sappho.openid.user.management.UserAccessKeyReport;
import uk.org.sappho.openid.user.management.UserManagement;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import java.util.Map;

@Path("/get-access-keys/json")
public class GetAccessKeysForLoggedInUser {

    @CookieParam(UserManagement.COOKIE_NAME)
    private String userIdDigest;
    @Context
    ContextResolver<UserManagement> userManagementContextResolver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, UserAccessKeyReport> getLoggedInUser() {

        return userManagementContextResolver.getContext(UserManagement.class).GetAccessKeysForLoggedInUser(userIdDigest);
    }
}
