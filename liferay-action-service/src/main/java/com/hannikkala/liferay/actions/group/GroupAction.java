package com.hannikkala.liferay.actions.group;

import com.liferay.portal.model.Group;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 16:23
 */
public class GroupAction {

    private GroupParameters params = new GroupParameters();
    private Group group;

    public GroupAction() {
    }

    /**
     * Create group with site name. Required.
     * @param name
     * @return
     */
    public GroupAction withName(String name) {
        this.params.setName(name);
        return this;
    }

    /**
     * Create group with friendly URL. Required.
     * @param friendlyUrl
     * @return
     */
    public GroupAction withFriendlyUrl(String friendlyUrl) {
        this.params.setFriendlyUrl(friendlyUrl);
        return this;
    }

    /**
     * Add description for group to be created.
     * @param description
     * @return
     */
    public GroupAction withDescription(String description) {
        this.params.setDescription(description);
        return this;
    }

    /**
     * Sets theme id for group. If you use theme from custom WAR, set value to themeid_WAR_warname.
     * @param themeId
     * @return
     */
    public GroupAction withTheme(String themeId) {
        this.params.setThemeId(themeId);
        return this;
    }

    public GroupSubActions create() {
        this.group = GroupUtil.createOrGetGroup(params);
        return new GroupSubActions(this, this.group);
    }
}
