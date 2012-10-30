/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import uk.org.sappho.openid.user.management.UserManagement;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;

@Path("/get-user-id")
public class GetLoggedInUser {

    @CookieParam(UserManagement.COOKIE_NAME)
    private String userIdDigest;
    @Context
    ContextResolver<UserManagement> userManagementContextResolver;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getLoggedInUserPlain() {

        return userManagementContextResolver.getContext(UserManagement.class).getLoggedInUserId(userIdDigest);
    }

    class LoggedInUser {

        private String userId;

        LoggedInUser(String userId) {

            this.userId = userId;
        }

        public String getUserId() {

            return userId;
        }
    }

    @Path("/json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public LoggedInUser getLoggedInUserJson() {

        return new LoggedInUser(
                userManagementContextResolver.getContext(UserManagement.class).getLoggedInUserId(userIdDigest));
    }
}
