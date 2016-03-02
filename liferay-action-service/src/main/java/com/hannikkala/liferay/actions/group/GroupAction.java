package com.hannikkala.liferay.actions.group;

import com.liferay.portal.model.Group;

/**
 * User: bleed
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

    public GroupAction withDescription(String description) {
        this.params.setDescription(description);
        return this;
    }

    public GroupSubActions create() {
        this.group = GroupUtil.createOrGetGroup(params);
        return new GroupSubActions(this, this.group);
    }
}
