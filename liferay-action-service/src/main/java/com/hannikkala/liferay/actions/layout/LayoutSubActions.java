package com.hannikkala.liferay.actions.layout;

import com.liferay.portal.model.Layout;

/**
 * User: bleed
 * Date: 07/03/16
 * Time: 13:05
 */
public class LayoutSubActions {
    private final Layout layout;
    private final LayoutAction layoutAction;

    public LayoutSubActions(Layout layout, LayoutAction layoutAction) {
        this.layout = layout;
        this.layoutAction = layoutAction;
    }

    /**
     * Create sub layout.
     * @return
     */
    public LayoutAction page() {
        LayoutAction layoutAction = new LayoutAction(this.layoutAction.getGroup(), this.layoutAction.getGroupSubActions(),
                this.layout, this.layoutAction);
        layoutAction.withParentLayoutId(layout.getLayoutId());
        return layoutAction;
    }

    public LayoutAction end() {
        return this.layoutAction;
    }
}
