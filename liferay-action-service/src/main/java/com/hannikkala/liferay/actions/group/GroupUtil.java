package com.hannikkala.liferay.actions.group;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 14:56
 */
public class GroupUtil {

    private static final Log _log = LogFactoryUtil.getLog(GroupUtil.class);

    public static Group createOrGetGroup(GroupParameters params) {

        try {
            String friendlyUrl = params.getFriendlyUrl();
            long companyId = PortalUtil.getDefaultCompanyId();
            Group group = GroupLocalServiceUtil.fetchFriendlyURLGroup(companyId, params.getFriendlyUrl());
            if(group != null) {
                _log.info("Found group by id " + group.getGroupId());
                return group;
            }
            long defaultUserId = params.getDefaultUserId();
            long parentGroupId = params.getParentGroupId();
            long liveGroupId = params.getLiveGroupId();
            String name = params.getName();
            String description = params.getDescription();
            int type = params.getType();
            boolean manualMembership = params.isManualMembership();
            int membershipRestriction = params.getMembershipRestriction();
            boolean site = params.isSite();
            boolean active = params.isActive();
            ServiceContext serviceContext = new ServiceContext();
            serviceContext.setAddGroupPermissions(true);
            serviceContext.setAddGuestPermissions(true);
            _log.info("Creating group.");
            group = GroupLocalServiceUtil.addGroup(defaultUserId, parentGroupId, Group.class.getName(), 0,
                    liveGroupId, name, description, type, manualMembership, membershipRestriction, friendlyUrl,
                    site, active, serviceContext);

            LayoutSetLocalServiceUtil.updateLookAndFeel(group.getGroupId(), params.getThemeId(), "", "", false);

            return group;
        } catch (PortalException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

    }
}
