/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import org.openid4java.discovery.DiscoveryInformation;

public class LoginInformation {

    private DiscoveryInformation discoveryInformation;
    private String serviceUrl;

    public LoginInformation(DiscoveryInformation discoveryInformation, String serviceUrl) {

        this.discoveryInformation = discoveryInformation;
        this.serviceUrl = serviceUrl;
    }

    public DiscoveryInformation getDiscoveryInformation() {

        return discoveryInformation;
    }

    public String getServiceUrl() {

        return serviceUrl;
    }
}
