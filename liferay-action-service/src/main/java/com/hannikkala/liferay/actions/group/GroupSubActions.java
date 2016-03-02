package com.hannikkala.liferay.actions.group;

import com.liferay.portal.model.Group;
import com.hannikkala.liferay.actions.ddmstructure.DDMStructureAction;
import com.hannikkala.liferay.actions.ddmtemplate.DDMTemplateAction;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 16:26
 */
public class GroupSubActions {

    private final GroupAction action;
    private final Group group;

    public GroupSubActions(GroupAction action, Group group) {
        this.action = action;
        this.group = group;
    }

    public DDMStructureAction structure() {
        return new DDMStructureAction(this.group, this);
    }

    public DDMTemplateAction template() {
        return new DDMTemplateAction(this.group, this);
    }

    public GroupAction end() {
        return this.action;
    }
}
