package com.emillics.lemon.model;

import com.emillics.lemon.base.config.Constants;

import java.io.Serializable;

/**
 * @author Honey
 * Create on 2022-05-17 13:10:34
 */
public class Version implements Serializable {

    private static final long serialVersionUID = 1597710536334L;

    private Integer id;
    private Float version;
    private String winUrl;
    private String macUrl;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getVersion() {
        return version;
    }

    public void setVersion(Float version) {
        this.version = version;
    }

    public String getUrl() {
        return Constants.SYSTEM_WINDOWS ? getWinUrl() : getMacUrl();
    }

    public String getWinUrl() {
        return winUrl;
    }

    public void setWinUrl(String winUrl) {
        this.winUrl = winUrl;
    }

    public String getMacUrl() {
        return macUrl;
    }

    public void setMacUrl(String macUrl) {
        this.macUrl = macUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Version[");
        str.append("id=").append(id);
        str.append(",version=").append(version);
        str.append(",winUrl=").append(winUrl);
        str.append(",macUrl=").append(macUrl);
        str.append(",remark=").append(remark);
        str.append("]");
        return str.toString();
    }
}