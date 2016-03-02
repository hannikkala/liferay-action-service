package com.hannikkala.liferay.actions.role;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.hannikkala.liferay.actions.LiferayActionService;

import java.util.Locale;
import java.util.Map;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 26/02/16
 * Time: 21:43
 */
public class RoleUtil {

    private static final Log _log = LogFactoryUtil.getLog(RoleUtil.class);

    public static Role createRole(RoleParameters params) throws SystemException, PortalException {
        long companyId = PortalUtil.getDefaultCompanyId();
        long userId = UserLocalServiceUtil.getDefaultUserId(companyId);
        long classNameId = ClassNameLocalServiceUtil.fetchClassNameId(Role.class.getName());
        Map<Locale, String> titleMap = params.getTitleMap();
        Map<Locale, String> descriptionMap = params.getDescriptionMap();

        LiferayActionService.getLocales().forEach(locale -> {
            if(!titleMap.containsKey(locale)) {
                titleMap.put(locale, params.getName());
            }
            if(!descriptionMap.containsKey(locale)) {
                descriptionMap.put(locale, params.getName());
            }
        });

        Role role = RoleLocalServiceUtil.fetchRole(companyId, params.getName());

        if(role == null) {
            _log.info("Add role '" + params.getName() + "'");
            return RoleLocalServiceUtil.addRole(userId, Role.class.getName(), classNameId, params.getName(), titleMap, descriptionMap, RoleConstants.TYPE_REGULAR, null, null);
        } else {
            _log.info("Updating role '" + params.getName() + "'");
            return RoleLocalServiceUtil.updateRole(role.getRoleId(), params.getName(), params.getTitleMap(), params.getDescriptionMap(), role.getSubtype(), null);
        }
    }
}
