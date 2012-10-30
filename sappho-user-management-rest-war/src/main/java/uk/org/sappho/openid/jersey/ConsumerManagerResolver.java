/**
 *** This software is licensed under the GNU General Public License, version 3.
 *** See http://www.gnu.org/licenses/gpl.html for full details of the license terms.
 *** Copyright 2012 Andrew Heald.
 */

package uk.org.sappho.openid.jersey;

import org.openid4java.consumer.ConsumerManager;
import org.openid4java.util.HttpClientFactory;
import org.openid4java.util.ProxyProperties;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ConsumerManagerResolver implements ContextResolver<ConsumerManager> {

    @Context
    private ServletContext servletContext;

    private ConsumerManager consumerManager = null;

    synchronized public ConsumerManager getContext(Class<?> type) {

        if (consumerManager == null) {
            String proxyHost = get("http.proxyHost");
            if (proxyHost != null) {
                String proxyPort = get("http.proxyPort");
                String proxyUser = get("http.proxyUser");
                String proxyPassword = get("http.proxyPassword");
                ProxyProperties proxyProperties = new ProxyProperties();
                proxyProperties.setProxyHostName(proxyHost);
                if (proxyPort != null) {
                    proxyProperties.setProxyPort(Integer.parseInt(proxyPort));
                }
                if (proxyUser != null) {
                    proxyProperties.setUserName(proxyUser);
                }
                if (proxyPassword != null) {
                    proxyProperties.setPassword(proxyPassword);
                }
                HttpClientFactory.setProxyProperties(proxyProperties);
            }
            consumerManager = new ConsumerManager();
        }
        return consumerManager;
    }

    private String get(String key) {

        String value = servletContext.getInitParameter(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }
}
