package com.hannikkala.liferay.actions;

import com.hannikkala.liferay.actions.group.GroupAction;
import com.hannikkala.liferay.actions.role.RoleAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * User: tommih
 * Date: 23/02/16
 * Time: 14:51
 */
public class LiferayActionService {

    private static List<Locale> locales = new ArrayList<>();
    private static boolean updateMode = true;

    public static GroupAction group() {
        return new GroupAction();
    }

    public static RoleAction role(String name) {
        return new RoleAction(name);
    }

    public static List<Locale> getLocales() {
        return locales;
    }

    /**
     * Which locales should we use for objects?
     * @param locales
     * @return
     */
    public static LiferayActionService locales(Locale... locales) {
        LiferayActionService.locales.addAll(Arrays.asList(locales));
        return new LiferayActionService();
    }

    /**
     * Should we allow updating objects with given information? Default is <i>true</i>.
     * @param allow
     * @return
     */
    public static LiferayActionService allowUpdate(boolean allow) {
        LiferayActionService.updateMode = allow;
        return new LiferayActionService();
    }

    public static boolean isUpdateMode() {
        return LiferayActionService.updateMode;
    }
}