package de.lasse.risk_server.Game.Settings;

import org.springframework.data.mongodb.core.mapping.Field;

public class SettingsState {

    @Field("is_fixed")
    private boolean isFixed;

    public boolean isIsFixed() {
        return this.isFixed;
    }

    public boolean getIsFixed() {
        return this.isFixed;
    }

    public void setIsFixed(boolean isFixed) {
        this.isFixed = isFixed;
    }

    public SettingsState(boolean isFixed) {
        this.isFixed = isFixed;
    }

}
