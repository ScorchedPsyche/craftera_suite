package com.github.scorchedpsyche.craftera_suite.modules.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TradeEntryModel
{
    @SerializedName("type")
    @Expose
    private String type = "sell";
    @SerializedName("minecraft_id")
    @Expose
    private String minecraftId = "dirt";
    @SerializedName("name")
    @Expose
    private String name = "Special Dirt Block";
    @SerializedName("owner_id")
    @Expose
    private String ownerId;
    @SerializedName("texture")
    @Expose
    private String texture;
    @SerializedName("amount")
    @Expose
    private Integer amount = 1;
    @SerializedName("price_item1")
    @Expose
    private String priceItem1 = "diamond";
    @SerializedName("price1")
    @Expose
    private Integer price1 = 1;
    @SerializedName("price_item2")
    @Expose
    private String priceItem2;
    @SerializedName("price2")
    @Expose
    private Integer price2;
    @SerializedName("uses")
    @Expose
    private Integer uses = 0;
    @SerializedName("uses_max")
    @Expose
    private Integer usesMax = 1;
    @SerializedName("experience_reward")
    @Expose
    private Integer experienceReward = 0;
    @SerializedName("price_multiplier")
    @Expose
    private Integer priceMultiplier = 0;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinecraftId() {
        return minecraftId;
    }

    public void setMinecraftId(String minecraftId) {
        this.minecraftId = minecraftId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPriceItem1() {
        return priceItem1;
    }

    public void setPriceItem1(String priceItem1) {
        this.priceItem1 = priceItem1;
    }

    public Integer getPrice1() {
        return price1;
    }

    public void setPrice1(Integer price1) {
        this.price1 = price1;
    }

    public String getPriceItem2() {
        return priceItem2;
    }

    public void setPriceItem2(String priceItem2) {
        this.priceItem2 = priceItem2;
    }

    public Integer getPrice2() {
        return price2;
    }

    public void setPrice2(Integer price2) {
        this.price2 = price2;
    }

    public Integer getUses() {
        return uses;
    }

    public void setUses(Integer uses) {
        this.uses = uses;
    }

    public Integer getUsesMax() {
        return usesMax;
    }

    public void setUsesMax(Integer usesMax) {
        this.usesMax = usesMax;
    }

    public Integer getExperienceReward() {
        return experienceReward;
    }

    public void setExperienceReward(Integer experienceReward) {
        this.experienceReward = experienceReward;
    }

    public Integer getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(Integer priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }
}
