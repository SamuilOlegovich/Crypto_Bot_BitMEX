/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.view.ConsoleHelper;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.Objects;
import java.io.IOError;





/**
 *
 * @author RobTerpilowski
 */
public class BitmexApiKey {

    public static String PROPKEY_USE_PRODUCTION = "use.production";
    public static String PROPKEY_API_KEY_NAME = "api.key.name";
    public static String PROPKEY_API_KEY = "api.key";

    protected boolean useProduction = false;
    protected String apiKeyName = "";
    protected String apiKey = "";

    public BitmexApiKey(String apiKeyName, String apiKey, boolean useProduction) {
        this.useProduction = useProduction;
        this.apiKeyName = apiKeyName;
        this.apiKey = apiKey;
    }

    public String getApiKeyName() {
        return apiKeyName;
    }

    public void setApiKeyName(String apiKeyName) {
        this.apiKeyName = apiKeyName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isUseProduction() {
        return useProduction;
    }

    public void setUseProduction(boolean useProduction) {
        this.useProduction = useProduction;
    }

    
    public static BitmexApiKey readApiKey(String propFile) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propFile));
            String name  = props.getProperty(PROPKEY_API_KEY_NAME);
            String value = props.getProperty(PROPKEY_API_KEY);
            boolean useProd = Boolean.parseBoolean(props.getProperty(PROPKEY_USE_PRODUCTION, "false"));

            return new BitmexApiKey(name, value, useProd);
        } catch (IOException ex) {
//            throw new SumZeroException(ex.getMessage(), ex);  /////////////////////
            ConsoleHelper.writeERROR(ex.toString());
            throw new IOError(ex);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.apiKeyName);
        hash = 59 * hash + Objects.hashCode(this.apiKey);
        hash = 59 * hash + (this.useProduction ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BitmexApiKey other = (BitmexApiKey) obj;
        if (this.useProduction != other.useProduction) {
            return false;
        }
        if (!Objects.equals(this.apiKeyName, other.apiKeyName)) {
            return false;
        }
        if (!Objects.equals(this.apiKey, other.apiKey)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BitmexApiKey{" + "apiKeyName=" + apiKeyName + ", apiKey=" + apiKey +
                ", useProduction=" + useProduction + '}';
    }
}
