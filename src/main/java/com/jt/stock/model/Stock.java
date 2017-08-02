package com.jt.stock.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("t_stock_copy")
public class Stock {

    @Id
    private int id;
    @Column
    private String symbol;
    @Column
    private String code;
    @Column
    private String name;
    @Column
    private String current;
    @Column
    private String percent;
    @Column("chg")
    private String change;
    @Column
    private String high;
    @Column
    private String low;
    @Column
    private String high52w;
    @Column
    private String low52w;
    @Column
    private String marketcapital;
    @Column
    private String amount;
    @Column
    private String pettm;
    @Column
    private String volume;
    @Column
    private String hasexist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh52w() {
        return high52w;
    }

    public void setHigh52w(String high52w) {
        this.high52w = high52w;
    }

    public String getLow52w() {
        return low52w;
    }

    public void setLow52w(String low52w) {
        this.low52w = low52w;
    }

    public String getMarketcapital() {
        return marketcapital;
    }

    public void setMarketcapital(String marketcapital) {
        this.marketcapital = marketcapital;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPettm() {
        return pettm;
    }

    public void setPettm(String pettm) {
        this.pettm = pettm;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getHasexist() {
        return hasexist;
    }

    public void setHasexist(String hasexist) {
        this.hasexist = hasexist;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Stock{");
        sb.append("symbol='").append(symbol).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", current='").append(current).append('\'');
        sb.append(", percent='").append(percent).append('\'');
        sb.append(", change='").append(change).append('\'');
        sb.append(", high='").append(high).append('\'');
        sb.append(", low='").append(low).append('\'');
        sb.append(", high52w='").append(high52w).append('\'');
        sb.append(", low52w='").append(low52w).append('\'');
        sb.append(", marketcapital='").append(marketcapital).append('\'');
        sb.append(", amount='").append(amount).append('\'');
        sb.append(", pettm='").append(pettm).append('\'');
        sb.append(", volume='").append(volume).append('\'');
        sb.append(", hasexist='").append(hasexist).append('\'');
        sb.append('}');
        return sb.toString();
    }
}