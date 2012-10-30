/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.user.management;

import java.util.Calendar;

public class UserAccessKeyReport {

    private String creationTime;
    private String lastAccessTime;

    public UserAccessKeyReport(Calendar creationTime, Calendar lastAccessTime) {

        this.creationTime = creationTime.getTime().toString();
        this.lastAccessTime = lastAccessTime.getTime().toString();
    }

    public String getCreationTime() {

        return creationTime;
    }

    public String getLastAccessTime() {

        return lastAccessTime;
    }
}
