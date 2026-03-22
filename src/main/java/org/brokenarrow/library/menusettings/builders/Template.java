package org.brokenarrow.library.menusettings.builders;

import java.util.List;

public class Template {

    private String matrial;
    private int amount;
    private String name;
    private List<String> lore;
    private String texture;

    public String getMatrial() {
        return matrial;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getTexture() {
        return texture;
    }

    public void setMaterial(final String matrial) {
        this.matrial = matrial;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public void setDisplayName(final String name) {
        this.name = name;
    }

    public void setLore(final List<String> lore) {
        this.lore = lore;
    }

    public void setTexture(final String texture) {
        this.texture = texture;
    }
}
