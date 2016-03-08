package com.hannikkala.liferay.actions.layout;

import com.hannikkala.liferay.actions.LiferayActionService;
import com.liferay.portal.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tommi Hännikkälä <tommi.hannikkala@soikea.com>
 * Date: 06/03/16
 * Time: 22:56
 */
public class LayoutUtil {

    private static final Log _log = LogFactoryUtil.getLog(LayoutUtil.class);

    public static Layout createLayout(LayoutParameters params) throws SystemException, PortalException {
        long userId = params.getDefaultUserId();
        long groupId = params.getGroupId();
        boolean privateLayout = params.isPrivateLayout();
        long parentLayoutId = params.getParentLayoutId();
        Map<Locale, String> nameMap = params.getNameMap();
        Map<Locale, String> descriptionMap = params.getDescriptionMap();
        Map<Locale, String> titleMap = params.getTitleMap();
        Map<Locale, String> keywordMap = new HashMap<>();
        Map<Locale, String> robotsMap = new HashMap<>();
        String type = params.getType();
        String typeSettings = params.getLayoutTemplate() != null ? "layout-template-id=" + params.getLayoutTemplate() : "";
        boolean hidden = params.isHidden();
        Map<Locale, String> friendlyURLMap = params.getFriendlyUrlMap();


        if(nameMap.isEmpty()) {
            throw new RuntimeException("At least one name must be given to a page.");
        }

        if(friendlyURLMap.isEmpty()) {
            throw new RuntimeException("At least one friendly url must be given to a page.");
        }

        String primaryName = nameMap.values().toArray(new String[0])[0];
        String primaryFriendlyUrl = friendlyURLMap.values().toArray(new String[0])[0];

        LiferayActionService.getLocales().forEach(locale -> {
            if(!nameMap.containsKey(locale)) {
                nameMap.put(locale, primaryName);
            }
            if(!titleMap.containsKey(locale)) {
                titleMap.put(locale, primaryName);
            }
            if(!descriptionMap.containsKey(locale)) {
                descriptionMap.put(locale, primaryName);
            }
        });

        Layout layout = null;
        try {
            layout = LayoutLocalServiceUtil.getFriendlyURLLayout(groupId, privateLayout, primaryFriendlyUrl);
        } catch (NoSuchLayoutException e) {
            // Ignored
        }

        if(layout == null) {
            _log.info("Adding Layout " + nameMap);
            return LayoutLocalServiceUtil.addLayout(userId,
                    groupId,
                    privateLayout,
                    parentLayoutId,
                    nameMap,
                    titleMap,
                    descriptionMap,
                    keywordMap,
                    robotsMap,
                    type,
                    typeSettings,
                    hidden,
                    friendlyURLMap, new ServiceContext());
        } else {
            _log.info("Layout " + friendlyURLMap + " found.");
        }

        return layout;
    }
}
