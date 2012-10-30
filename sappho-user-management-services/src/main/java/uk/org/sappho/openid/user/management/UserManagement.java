/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.user.management;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserManagement {

    public static final String COOKIE_NAME = "sappho-user-session";

    private final Map<String, UserInformation> userSessions = new LinkedHashMap<String, UserInformation>();
    private final Map<String, UserInformation> accessKeys = new LinkedHashMap<String, UserInformation>();
    private final Map<String, List<String>> accessKeysForUser = new LinkedHashMap<String, List<String>>();
    private final MessageDigest messageDigest;

    public UserManagement() throws NoSuchAlgorithmException {

        messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public String getNewUserIdDigest(String userId) {

        return getNewDigest(userSessions, userId);
    }

    public String getNewUserAccessKey(String userIdDigest) {

        String digest = "";
        String userId = getLoggedInUserId(userIdDigest);
        if (userId.length() != 0) {
            digest = getNewDigest(accessKeys, userId);
            synchronized (accessKeysForUser) {
                List<String> keyList = accessKeysForUser.get(userId);
                if (keyList == null) {
                    keyList = new LinkedList<String>();
                    accessKeysForUser.put(userId, keyList);
                }
                keyList.add(digest);
            }
        }
        return digest;
    }

    private String getNewDigest(Map<String, UserInformation> pool, String userId) {

        String randomText = UUID.randomUUID().toString();
        String digest = Hex.encodeHexString(messageDigest.digest(randomText.getBytes()));
        synchronized (pool) {
            pool.put(digest, new UserInformation(userId));
        }
        return digest;
    }

    public String getLoggedInUserId(String userIdDigest) {

        return getUserId(userSessions, userIdDigest);
    }

    public String getAccessKeyUserId(String userIdDigest) {

        return getUserId(accessKeys, userIdDigest);
    }

    private String getUserId(Map<String, UserInformation> pool, String userIdDigest) {

        String userId = "";
        if (userIdDigest != null) {
            synchronized (pool) {
                if (pool.containsKey(userIdDigest)) {
                    userId = pool.get(userIdDigest).getUserId(true);
                }
            }
        }
        return userId;
    }

    public Map<String, UserAccessKeyReport> GetAccessKeysForLoggedInUser(String userIdDigest) {

        Map<String, UserAccessKeyReport> accessKeysForUserReport = new LinkedHashMap<String, UserAccessKeyReport>();
        String userId = getLoggedInUserId(userIdDigest);
        if (userId.length() != 0) {
            synchronized (accessKeysForUser) {
                if (accessKeysForUser.containsKey(userId)) {
                    for (String key : accessKeysForUser.get(userId)) {
                        synchronized (accessKeys) {
                            UserInformation userInformation = accessKeys.get(key);
                            accessKeysForUserReport.put(key, new UserAccessKeyReport(userInformation.getCreationTime(),
                                    userInformation.getLastAccessTime()));
                        }
                    }
                }
            }
        }
        return accessKeysForUserReport;
    }

    public void logoff(String userIdDigest) {

        synchronized (userSessions) {
            if (userIdDigest != null && userSessions.containsKey(userIdDigest)) {
                userSessions.remove(userIdDigest);
            }
        }
    }

    public void deleteUserAccessKey(String userIdDigest, String keyDigest) {

        String userId = getLoggedInUserId(userIdDigest);
        if (userId.length() != 0) {
            synchronized (accessKeys) {
                if (keyDigest != null && accessKeys.containsKey(keyDigest) &&
                        accessKeys.get(keyDigest).getUserId(false).equals(userId)) {
                    accessKeys.remove(keyDigest);
                }
            }
        }
    }

    public void clearOldSessions() {

        synchronized (userSessions) {
            for (String userIdDigest : userSessions.keySet()) {
                UserInformation userInformation = userSessions.get(userIdDigest);
                if ((Calendar.getInstance().getTimeInMillis() - userInformation.getLastAccessTime().getTimeInMillis())
                        > 24 * 60 * 60 * 1000) {
                    userSessions.remove(userIdDigest);
                }
            }
        }
    }
}
