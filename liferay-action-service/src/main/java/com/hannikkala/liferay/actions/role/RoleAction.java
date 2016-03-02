package com.hannikkala.liferay.actions.role;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 26/02/16
 * Time: 21:20
 */
public class RoleAction {

    private final RoleParameters params = new RoleParameters();
    private static final Log _log = LogFactoryUtil.getLog(RoleAction.class);

    public RoleAction(String name) {
        this.params.setName(name);
    }

    public RoleAction buildTitleMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getTitleMap());
        return this;
    }

    public RoleAction buildDescriptionMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getDescriptionMap());
        return this;
    }


    public void create() {
        try {
            RoleUtil.createRole(this.params);
        } catch (SystemException e) {
            _log.error("Creating role " + this.params.getName() + " failed.", e);
        } catch (PortalException e) {
            _log.error("Creating role " + this.params.getName() + " failed.", e);
        }
    }
}
