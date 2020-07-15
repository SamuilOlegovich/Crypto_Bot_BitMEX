/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import bitmex.Bot.model.bitMEX.entity.BitmexCancelOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexInstrument;
import bitmex.Bot.model.bitMEX.entity.BitmexAmendOrder;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
import org.glassfish.jersey.client.ClientProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
//import com.sumzerotrading.data.SumZeroException;
import bitmex.Bot.model.bitMEX.entity.BitmexError;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import org.glassfish.jersey.client.ClientConfig;
import bitmex.Bot.model.bitMEX.enums.Verb;
import javax.ws.rs.client.ClientBuilder;
//import com.sumzerotrading.data.Ticker;
import com.google.common.base.Strings;
import bitmex.Bot.view.ConsoleHelper;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import java.util.Collections;
import java.util.Objects;
import java.util.Arrays;
import java.io.IOError;
import java.util.List;
import java.net.URI;

import static bitmex.Bot.model.bitMEX.enums.Verb.*;



/**
 *
 * @author RobTerpilowski
 */
public class BitmexRestClient implements IBitmexRestClient {

//    protected static Logger logger = LoggerFactory.getLogger(BitmexRestClient.class);///////////////////////

    protected Client client;

    protected ISignatureGenerator signatureGenerator = new BitmexSignatureGenerator();
    protected String apiURL = "";
    protected String productionApiUrl = "https://www.bitmex.com/api/v1";
    protected String testnetApiUrl = "https://testnet.bitmex.com/api/v1";

    protected String apiKeyName;
    protected String apiKey;

    //Used by unit tests
    protected BitmexRestClient() {
        this(true);
    }

    public BitmexRestClient(boolean useProduction) {
        if (useProduction) {
            apiURL = productionApiUrl;
        } else {
            apiURL = testnetApiUrl;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider(mapper, new Annotations[0]);
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);   
        config.register(provider);
        config.register(JacksonFeature.class);
                
        client = ClientBuilder.newBuilder().withConfig(config).build();
    }

    public BitmexRestClient(boolean useProduction, String apiKeyName, String apiKey) {
        this(useProduction);
        this.apiKeyName = apiKeyName;
        this.apiKey = apiKey;
    }

    @Override
    public BitmexInstrument getInstrument(Ticker ticker) {
        WebTarget target = client.target(apiURL)
                .path("instrument")
                .queryParam("symbol", ticker.getSymbol());

        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
        addHeaders(builder, target.getUri());

        Response response = builder.get();

        response.bufferEntity();
        ConsoleHelper.writeINFO("Response: " + response.readEntity(String.class));//////////////////////
        BitmexInstrument[] instruments = response.readEntity(BitmexInstrument[].class);
        return instruments[0];
    }

    @Override
    public BitmexOrder submitOrder(BitmexOrder order) { // подтвердить заказ
        Response response = submitRequestWithBody("order", order, POST);
        ConsoleHelper.writeDEBUG("Response code: " + response.getStatus());/////////////////////////////////info
        
        if (response.getStatus() == 503) {
            ConsoleHelper.writeERROR("503 response returned");
            throw new BitmexSystemOverloadedException(order);
        }
        return response.readEntity(BitmexOrder.class);
    }

    @Override
    public BitmexOrder[] cancelOrder(BitmexOrder order) {   // отменить заказ
        BitmexCancelOrder cancel = new BitmexCancelOrder();
        cancel.setOrderID(order.getOrderID());
        Response response = submitRequestWithBody("order", cancel, DELETE);
        ConsoleHelper.writeINFO("Response code: " + response.getStatus());//////////////////////////////////////
        return response.readEntity(BitmexOrder[].class);
    }

    @Override
    public BitmexOrder amendOrder(BitmexAmendOrder order) { // изменить ордер
        Response response = submitRequestWithBody("order", order, PUT);
        ConsoleHelper.writeINFO("Response code: " + response.getStatus());/////////////////////////////////////
        return response.readEntity(BitmexOrder.class);
    }

    @Override
    // получить данные котировок
    public List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize) {
        return getChartData(ticker, count, binSize, "");
    }
    
    @Override
    // получить данные котировок
    public List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize, String endTime) {
        return getChartData(ticker, count, binSize, "", false);
    }

    @Override
    // получить данные котировок
    public List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize,
                                              String endTime, boolean getInprogressBar) {
        WebTarget target = client.target(apiURL)
                .path("trade/bucketed")
                .queryParam("symbol", ticker.getSymbol())
                .queryParam("count", count)
                .queryParam("binSize", binSize.getBin())
                .queryParam("endTime", endTime)
                .queryParam("partial", getInprogressBar)
                .queryParam("reverse", true);
        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
        addHeaders(builder, target.getUri());

        Response response = builder.get();

        response.bufferEntity();

        ConsoleHelper.writeINFO("Response: " + response.readEntity(String.class));////////////////////////////
        BitmexChartData[] data = response.readEntity(BitmexChartData[].class);
        List<BitmexChartData> returnList = Arrays.asList(data);
        Collections.reverse(returnList);

        return returnList;   
    }
    
    

    protected Response submitRequestWithBody(String path, Object object, Verb verb) {
        if (verb == GET) {
            // Не удается вызвать этот метод для запроса GET
//            throw new SumZeroException("Can't call this method for a GET request");
            ConsoleHelper.writeINFO("Can't call this method for a GET request");
            return null;
        }

        String jsonObject = toJson(object);
        ConsoleHelper.writeDEBUG("Submitting object: " + jsonObject);
        WebTarget target = client.target(apiURL).path(path);
        Invocation.Builder builder = target.request(MediaType.APPLICATION_JSON);
        addHeaders(builder, target.getUri(), verb.toString(), jsonObject);
        Entity entity = Entity.json(jsonObject);
        Response response = builder.build(verb.toString(), entity).invoke();
        response.bufferEntity();    
        String stringResponse = response.readEntity(String.class);
        ConsoleHelper.writeDEBUG("Response: " + stringResponse);

        if( stringResponse.contains("error") ) {
            ConsoleHelper.writeINFO("HTTP: " + verb.name() + " Error Submitting object: " + jsonObject);///////////////////////
            throw new BitmexException( response.readEntity(BitmexError.class).getError() );
        }
        
        return response;
    }

    protected String toJson(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
//            throw new SumZeroException(ex);
            ConsoleHelper.writeERROR(ex.toString());
            throw new IOError(ex);
        }
    }

    protected void addHeaders(Invocation.Builder builder, URI uri) {
        addHeaders(builder, uri, "GET", "");
    }

    protected void addHeaders(Invocation.Builder builder, URI uri, String verb, String data) {
        if (!Strings.isNullOrEmpty(apiKey) && !Strings.isNullOrEmpty(apiKeyName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(uri.getPath());
            if (!Strings.isNullOrEmpty(uri.getQuery())) {
                sb.append("?").append(uri.getQuery());
            }

            String urlPath = sb.toString();
            int expiry = getExpiry();
            ConsoleHelper.writeDEBUG("expiry: " + expiry);
            String apiSignature = signatureGenerator.generateSignature(apiKey, verb, urlPath, expiry, data);
            builder.header("api-expires", Integer.toString(expiry))
                    .header("api-key", apiKeyName)
                    .header("api-signature", apiSignature);
            ConsoleHelper.writeDEBUG("api-signature: " + apiSignature);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    protected int getExpiry() {
        return (int) ((getSystemTime() / 1000) + 15);
    }

    protected long getSystemTime() {
        return System.currentTimeMillis();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.apiURL);
        hash = 97 * hash + Objects.hashCode(this.productionApiUrl);
        hash = 97 * hash + Objects.hashCode(this.testnetApiUrl);
        hash = 97 * hash + Objects.hashCode(this.apiKeyName);
        hash = 97 * hash + Objects.hashCode(this.apiKey);
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
        final BitmexRestClient other = (BitmexRestClient) obj;
        if (!Objects.equals(this.apiURL, other.apiURL)) {
            return false;
        }
        if (!Objects.equals(this.productionApiUrl, other.productionApiUrl)) {
            return false;
        }
        if (!Objects.equals(this.testnetApiUrl, other.testnetApiUrl)) {
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
}
