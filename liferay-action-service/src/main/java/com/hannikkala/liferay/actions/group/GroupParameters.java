package com.hannikkala.liferay.actions.group;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * User: bleed
 * Date: 23/02/16
 * Time: 15:24
 */
public class GroupParameters {
    private long defaultUserId;
    private long parentGroupId = GroupConstants.DEFAULT_PARENT_GROUP_ID;
    private String friendlyUrl = "/guest";
    private long liveGroupId;
    private String name;
    private String description = "";
    private int type = GroupConstants.TYPE_SITE_OPEN;
    private boolean manualMembership = true;
    private int membershipRestriction = GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION;
    private boolean site = true;
    private boolean active = true;

    public GroupParameters() {
        try {
            Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));
            long companyId = company.getCompanyId();
            this.defaultUserId = UserLocalServiceUtil
                    .getDefaultUserId(companyId);

        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }

    }

    public long getDefaultUserId() {
        return defaultUserId;
    }

    public void setDefaultUserId(long defaultUserId) {
        this.defaultUserId = defaultUserId;
    }

    public long getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(long parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public long getLiveGroupId() {
        return liveGroupId;
    }

    public void setLiveGroupId(long liveGroupId) {
        this.liveGroupId = liveGroupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isManualMembership() {
        return manualMembership;
    }

    public void setManualMembership(boolean manualMembership) {
        this.manualMembership = manualMembership;
    }

    public int getMembershipRestriction() {
        return membershipRestriction;
    }

    public void setMembershipRestriction(int membershipRestriction) {
        this.membershipRestriction = membershipRestriction;
    }

    public boolean isSite() {
        return site;
    }

    public void setSite(boolean site) {
        this.site = site;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
