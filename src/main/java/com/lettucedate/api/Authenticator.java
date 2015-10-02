package com.lettucedate.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lettucedate.core.FacebookLocation;
import com.lettucedate.core.FacebookLocationWrapper;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.lettucedate.core.FacebookUser;
import com.lettucedate.core.UserRecord;

/**
 * Created by ultradad on 9/22/15.
 */
public  class Authenticator {
    private static final Logger log = Logger.getLogger(Authenticator.class.getName());
    private static Authenticator instance = null;
    public static final String USERID = "userid";
    private final static int ITERATIONS = 1000;
    private static final String TWO_WAY_CRYPT_METHOD = "PBEWithMD5AndDES";
    private static final char[] MASTER_PASSWORD = "23&-*/F43v02!s_83jJ@=a".toCharArray();
    private static final byte[] MASTER_SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };

    private static final String FBAppId = "822825007835530";
    private static final String FBSecret = "c12cac4381e3ca3dbc6590384275a0f4";



    protected Authenticator() {
        // Exists only to defeat instantiation.
    }
    public static Authenticator getInstance() {
        if(instance == null) {
            instance = new Authenticator();
        }
        return instance;
    }

    public static String[] createSaltedPassword(final String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        final byte[] bSalt = new byte[8];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(bSalt);

        final byte[] bDigest = getHash(ITERATIONS, password, bSalt);

        return new String[]{new String(Base64.encodeBase64(bDigest)), new String(Base64.encodeBase64(bSalt))};
    }

    public static boolean authenticate(String digest, String salt, final String password) throws Exception {
        final byte[] proposedDigest = getHash(ITERATIONS, password, Base64.decodeBase64(salt.getBytes()));

        return Arrays.equals(proposedDigest, Base64.decodeBase64(digest.getBytes()));
    }




    public static Boolean AuthenticateFBUser(HttpSession session, String fbId, String authToken)
    {
        if (UserIsLoggedIn(session)) {
            Logout(session);
        }

        try {
            // first, verify that the app token is good.
            String baseURL = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token";
            baseURL += "&client_id=" + FBAppId;
            baseURL += "&client_secret=" + FBSecret;
            baseURL += "&fb_exchange_token=" + authToken;

            URL url = new URL(baseURL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line, resultStr = "";

            while ((line = reader.readLine()) != null) {
                resultStr += line;
            }
            reader.close();
            String accessToken = resultStr.substring(resultStr.indexOf('=') + 1, resultStr.indexOf('&'));
            String serverProof = hmacSHA256(accessToken, FBSecret);

            baseURL = "https://graph.facebook.com/me?";
            baseURL += "access_token=" + accessToken;
            baseURL += "&fields=name,id,birthday,first_name,gender,last_name,interested_in,location";
            baseURL += "&client_id=" + FBAppId;
            baseURL += "&appsecret_proof=" + serverProof;
            url = new URL(baseURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            resultStr = "";

            while ((line = reader.readLine()) != null) {
                resultStr += line;
            }
            reader.close();
            Gson gson = new GsonBuilder().create();
            FacebookUser fbUser = gson.fromJson(resultStr, FacebookUser.class);
            if (!fbUser.id.equals(fbId)) {
                // the ids do not match - bad!!
                log.log(Level.SEVERE, "Facebook ID#s do not match!");
                return false;
            }

            // Update the Facebook location...
            if (fbUser.location != null) {
                baseURL = "https://graph.facebook.com/" + fbUser.location.id + "?";
                baseURL += "access_token=" + accessToken;
                baseURL += "&fields=location";
                baseURL += "&client_id=" + FBAppId;
                baseURL += "&appsecret_proof=" + serverProof;
                url = new URL(baseURL);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                resultStr = "";

                while ((line = reader.readLine()) != null) {
                    resultStr += line;
                }
                reader.close();

                FacebookLocationWrapper locDetail = gson.fromJson(resultStr, FacebookLocationWrapper.class);
                fbUser.location.city = locDetail.location.city;
                fbUser.location.country = locDetail.location.country;
                fbUser.location.latitude = locDetail.location.latitude;
                fbUser.location.longitude = locDetail.location.longitude;
                fbUser.location.region = locDetail.location.region;
                fbUser.location.state = locDetail.location.state;
                fbUser.location.street = locDetail.location.street;
                fbUser.location.zip  = locDetail.location.zip;
            }

            // then, see if the user exists and create it if it doesn't
            UserRecord newUser = UserRecord.FindByFBID(fbId);

            if (newUser == null) {
                // create user
                newUser = UserRecord.CreateFromFacebook(fbUser);
                newUser = new UserRecord();
                newUser.facebookid = fbId;
                newUser.nickname = fbUser.name;

                newUser.Create();

                session.setAttribute(USERID, newUser.id);
            } else {
                // now set up the session and go
                newUser.UpdateFromFacebook(fbUser);
                session.setAttribute(USERID, newUser.id);
            }
            return true;
        }
        catch (Exception exp)
        {
            log.log(Level.SEVERE, String.format("Exception in Facebook access: %s!", exp.getMessage()));
            return false;
        }

    }


    public static long CurrentUserId(HttpSession session) {
        Object theId = session.getAttribute(USERID);
        if (theId != null)
            return (long)theId;
        else
            return 0;
    }

    public static UserRecord CurrentUser(HttpSession session) {
        Long theId = (Long)session.getAttribute(USERID);
        if (theId != null) {
            UserRecord newUser = UserRecord.FindByID(theId);
            return newUser;
        }
        else
            return null;
    }

    public static Boolean UserIsLoggedIn(HttpSession session) {
        return session.getAttribute(USERID) != null;
    }


    public static Boolean Logout(HttpSession session)
    {
        if (session.getAttribute(USERID) != null) {
            // TODO:  update user's online status
            long userId = CurrentUserId(session);
            session.removeAttribute(USERID);

            return true;
        }
        else
            return false;

    }

    private static String hmacSHA256(String accessToken, String appSecret) throws Exception {
        try {
            byte[] key = appSecret.getBytes(Charset.forName("UTF-8"));
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] raw = mac.doFinal(accessToken.getBytes());
            byte[] hex = encodeHex(raw);
            return new String(hex, "UTF-8");
        } catch (Exception e) {
            throw new IllegalStateException("Creation of appsecret_proof has failed", e);
        }
    }

    public static byte[] encodeHex(final byte[] data) {
        if (data == null)
            throw new NullPointerException("Parameter 'data' cannot be null.");

        final char[] toDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }

        return new String(out).getBytes(Charset.forName("UTF-8"));
    }

    public static byte[] getHash(final int iterationNb, final String password, final byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }




}