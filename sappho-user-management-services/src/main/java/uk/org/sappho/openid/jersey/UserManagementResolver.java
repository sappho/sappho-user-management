/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import uk.org.sappho.openid.user.management.UserManagement;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.security.NoSuchAlgorithmException;

@Provider
public class UserManagementResolver implements ContextResolver<UserManagement> {

    private final UserManagement userManagement;

    public UserManagementResolver() throws NoSuchAlgorithmException {

        userManagement = new UserManagement();
    }

    public UserManagement getContext(Class<?> type) {

        return userManagement;
    }
}
