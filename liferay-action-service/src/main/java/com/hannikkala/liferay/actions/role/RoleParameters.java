package com.hannikkala.liferay.actions.role;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 26/02/16
 * Time: 21:25
 */
public class RoleParameters {
    private String name;
    private final Map<Locale, String> titleMap = new HashMap<>();
    private final Map<Locale, String> descriptionMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Locale, String> getTitleMap() {
        return titleMap;
    }

    public Map<Locale, String> getDescriptionMap() {
        return descriptionMap;
    }

}
