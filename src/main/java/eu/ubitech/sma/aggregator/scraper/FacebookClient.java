/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ubitech.sma.aggregator.scraper;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva
 */
public class FacebookClient {


    //Facebook fixed values
    private final String FACEBOOK_USERNAME = "anetos17@yahoo.gr";
    private final String FACEBOOK_PASSWORD = "tass590385";
    private final String FACEBOOK_HEAD = "Facebook";
    private final String FACEBOOK_PAGE = "https://www.facebook.com/";

    //Predefined Facebook Pages
    public final String FACEBOOK_ABOUT_PAGE = "/about";
    public final String FACEBOOK_LOGIN_PAGE = "login";

    private final int MAX_RECONNECT_TIMES = 3;
    private final long MAX_RECONNECT_PAUSE_TIME = 60000; //60 sec

    private final WebClient webClient = new WebClient();

    FacebookClient(Boolean isLoggingEnabled) {
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        if (!isLoggingEnabled) {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);
        }
    }

    public String getUserFacebookPage(String username, String userFacebookPage) {

        Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "Requested About page for user: {0}", username);
        String aboutPageText;
        //Check if a valid session to facebook exists
        if (!isClientLoggedIn()) {
            Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient is not logged-in to facebook account");
            for (int tries = 1; tries <= MAX_RECONNECT_TIMES; tries++) {
                Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient is connecting to facebook... (tries: " + tries + "/" + MAX_RECONNECT_TIMES+")");
                if (loginToFacebook()) {
                    break;
                } else if (tries == MAX_RECONNECT_TIMES) {
                    return FacebookClientError.NOT_LOGGED_IN.toString();
                }
                Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient failed connecting to facebook retrying in " + MAX_RECONNECT_PAUSE_TIME / 1000 + " seconds...");
                try {
                    Thread.sleep(MAX_RECONNECT_PAUSE_TIME);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FacebookClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //Try to fetch users' about page
        try {
            HtmlPage aboutPage = webClient.getPage(FACEBOOK_PAGE + username + userFacebookPage);
            aboutPageText = aboutPage.asXml();
        } catch (IOException | FailingHttpStatusCodeException ex) {
            aboutPageText = FacebookClientError.PAGE_NOT_FETCHED.toString();
            Logger.getLogger(FacebookClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return aboutPageText;
    }

    private boolean loginToFacebook() {
        Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient is logging to facebook using {0} account", FACEBOOK_USERNAME);
        try {
            HtmlPage loginPage = webClient.getPage(FACEBOOK_PAGE + FACEBOOK_LOGIN_PAGE);
            HtmlForm loginForm = loginPage.getElementById("login_form", false);
            HtmlInput loginButton = loginForm.getInputByValue("Log In");
            HtmlTextInput username = loginForm.getInputByName("email");
            username.setValueAttribute(FACEBOOK_USERNAME);
            HtmlPasswordInput password = loginForm.getInputByName("pass");
            password.setValueAttribute(FACEBOOK_PASSWORD);
            loginButton.click();
            Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient connection to Facebook success!");
            return true;
        } catch (IOException | ElementNotFoundException | FailingHttpStatusCodeException ex) {
            ex.printStackTrace();
            Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "WebClient was unable to connect to facebook...");
            return false;
        }
    }

    private boolean isClientLoggedIn() {
        try {
            HtmlPage mainPage = webClient.getPage(FACEBOOK_PAGE);
            return (mainPage.getTitleText().equalsIgnoreCase(FACEBOOK_HEAD));
        } catch (IOException |  FailingHttpStatusCodeException | ScriptException ex) {
            ex.printStackTrace();
            Logger.getLogger(FacebookClient.class.getName()).log(Level.INFO, "Unable to fetch facebook page: {0}", FACEBOOK_PAGE);
            return false;
        }
    }
}
