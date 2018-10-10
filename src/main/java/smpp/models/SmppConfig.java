package smpp.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by luyenchu on 6/10/16.
 */
public class SmppConfig {
    private int numberOfSessions;
    private int windowSize;
    private String sessionName;
    private String subIdPattern;
    private String bindType;
    private String smppHost;
    private int smppPort;
    private int smppTimeout;
    private String smppUsername;
    private String smppPassword;
    private boolean enableLogByte;
    private boolean enableLogPdu;
    private boolean enableCounters;
    private String shortCode;

    public SmppConfig() {
        smppTimeout = 10000;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public boolean isEnableCounters() {
        return enableCounters;
    }

    public void setEnableCounters(boolean enableCounters) {
        this.enableCounters = enableCounters;
    }

    public boolean isEnableLogByte() {
        return enableLogByte;
    }

    public void setEnableLogByte(boolean enableLogByte) {
        this.enableLogByte = enableLogByte;
    }

    public boolean isEnableLogPdu() {
        return enableLogPdu;
    }

    public void setEnableLogPdu(boolean enableLogPdu) {
        this.enableLogPdu = enableLogPdu;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSmppHost() {
        return smppHost;
    }

    public void setSmppHost(String smppHost) {
        this.smppHost = smppHost;
    }

    public String getSmppPassword() {
        return smppPassword;
    }

    public void setSmppPassword(String smppPassword) {
        this.smppPassword = smppPassword;
    }

    public int getSmppPort() {
        return smppPort;
    }

    public void setSmppPort(int smppPort) {
        this.smppPort = smppPort;
    }

    public int getSmppTimeout() {
        return smppTimeout;
    }

    public void setSmppTimeout(int smppTimeout) {
        this.smppTimeout = smppTimeout;
    }

    public String getSmppUsername() {
        return smppUsername;
    }

    public void setSmppUsername(String smppUsername) {
        this.smppUsername = smppUsername;
    }

    public String getSubIdPattern() {
        return subIdPattern;
    }

    public void setSubIdPattern(String subIdPattern) {
        this.subIdPattern = subIdPattern;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("bindType", bindType)
                .append("numberOfSessions", numberOfSessions)
                .append("windowSize", windowSize)
                .append("sessionName", sessionName)
                .append("subIdPattern", subIdPattern)
                .append("smppHost", smppHost)
                .append("smppPort", smppPort)
                .append("smppTimeout", smppTimeout)
                .append("smppUsername", smppUsername)
                .append("smppPassword", smppPassword)
                .append("enableLogByte", enableLogByte)
                .append("enableLogPdu", enableLogPdu)
                .append("enableCounters", enableCounters)
                .append("shortCode", shortCode)
                .toString();
    }
}
