/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.user.management;

import java.util.Calendar;

public class UserInformation {

    private String userId;
    private Calendar creationTime;
    private Calendar lastAccessTime;

    public UserInformation(String userId) {

        this.userId = userId;
        creationTime = lastAccessTime = Calendar.getInstance();
    }

    public String getUserId(boolean recordAccess) {

        if (recordAccess) {
            lastAccessTime = Calendar.getInstance();
        }
        return userId;
    }

    public Calendar getCreationTime() {

        return creationTime;
    }

    public Calendar getLastAccessTime() {

        return lastAccessTime;
    }
}
