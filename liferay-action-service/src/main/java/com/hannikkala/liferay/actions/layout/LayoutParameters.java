package com.hannikkala.liferay.actions.layout;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tommi Hännikkälä <tommi.hannikkala@soikea.com>
 * Date: 06/03/16
 * Time: 22:54
 */
public class LayoutParameters {
    private long defaultUserId;
    private long groupId;
    private String layoutTemplate;
    private boolean hidden = false;
    private final Map<Locale, String> friendlyUrlMap = new HashMap<>();
    private final Map<Locale, String> nameMap = new HashMap<>();
    private final Map<Locale, String> titleMap = new HashMap<>();
    private final Map<Locale, String> descriptionMap = new HashMap<>();
    private String type = LayoutConstants.TYPE_PORTLET;
    private boolean privateLayout = false;
    private long parentLayoutId = 0L;


    private static final Log _log = LogFactoryUtil.getLog(LayoutParameters.class);

    public LayoutParameters() {
        try {
            long companyId = PortalUtil.getDefaultCompanyId();
            this.defaultUserId = UserLocalServiceUtil
                    .getDefaultUserId(companyId);
        } catch (PortalException e) {
            _log.error("Error finding default user.", e);
        } catch (SystemException e) {
            _log.error("Error finding default user.", e);
        }
    }

    public void setLayoutTemplate(String layoutTemplate) {
        this.layoutTemplate = layoutTemplate;
    }

    public String getLayoutTemplate() {
        return layoutTemplate;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Map<Locale, String> getFriendlyUrlMap() {
        return friendlyUrlMap;
    }

    public Map<Locale, String> getNameMap() {
        return nameMap;
    }

    public Map<Locale, String> getTitleMap() {
        return titleMap;
    }

    public Map<Locale, String> getDescriptionMap() {
        return descriptionMap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public long getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(long defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public boolean isPrivateLayout() {
        return privateLayout;
    }

    public void setPrivateLayout(boolean privateLayout) {
        this.privateLayout = privateLayout;
    }

    public long getParentLayoutId() {
        return parentLayoutId;
    }

    public void setParentLayoutId(long parentLayoutId) {
        this.parentLayoutId = parentLayoutId;
    }
}
