package com.hannikkala.liferay.actions.layout;

import com.hannikkala.liferay.actions.LiferayActionService;
import com.hannikkala.liferay.actions.group.GroupSubActions;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Tommi Hännikkälä <tommi.hannikkala@soikea.com>
 * Date: 06/03/16
 * Time: 22:52
 */
public class LayoutAction {

    private final static Log _log = LogFactoryUtil.getLog(LayoutAction.class);

    private Layout parentLayout;
    private LayoutAction parentLayoutAction;

    private final Group group;
    private final GroupSubActions groupSubActions;
    private final LayoutParameters params = new LayoutParameters();

    public LayoutAction(Group group, GroupSubActions groupSubActions) {
        this.group = group;
        this.groupSubActions = groupSubActions;
        this.params.setGroupId(group.getGroupId());
        this.params.setDefaultUserId(group.getCreatorUserId());
    }

    public LayoutAction(Group group, GroupSubActions groupSubActions, Layout layout, LayoutAction layoutAction) {
        this(group, groupSubActions);
        this.parentLayout = layout;
        this.parentLayoutAction = layoutAction;
    }

    /**
     * Sets page hidden. Default false.
     */
    public LayoutAction hide() {
        this.params.setHidden(true);
        return this;
    }

    /**
     * Sets friendly URL for each allowed locale. Start with slash.
     * @param friendlyUrl
     * @return
     */
    public LayoutAction withFriendlyUrl(String friendlyUrl) {
        LiferayActionService.getLocales().forEach(locale -> this.params.getFriendlyUrlMap().put(locale, friendlyUrl));
        return this;
    }

    public LayoutAction buildNameMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.getParams().getNameMap());
        return this;
    }

    /**
     * Use name with all allowed locales.
     * @param name
     * @return
     */
    public LayoutAction withName(String name) {
        LiferayActionService.getLocales().forEach(locale -> this.params.getNameMap().put(locale, name));
        return this;
    }

    public LayoutAction buildTitleMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getTitleMap());
        return this;
    }

    /**
     * Use title with all allowed locales.
     * @param title
     * @return
     */
    public LayoutAction withTitle(String title) {
        LiferayActionService.getLocales().forEach(locale -> this.params.getTitleMap().put(locale, title));
        return this;
    }

    public LayoutAction buildDescriptionMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getDescriptionMap());
        return this;
    }

    /**
     * Use description with all allowed locales.
     * @param description
     * @return
     */
    public LayoutAction withDescription(String description) {
        LiferayActionService.getLocales().forEach(locale -> this.params.getDescriptionMap().put(locale, description));
        return this;
    }

    public LayoutAction buildFriendlyUrlMap(Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getFriendlyUrlMap());
        return this;
    }

    protected LayoutAction withParentLayoutId(long parentLayoutId) {
        this.params.setParentLayoutId(parentLayoutId);
        return this;
    }

    /**
     * Set type for the page. Use <link>com.liferay.portal.model.LayoutConstants</link> type constant.
     * @param type
     */
    public LayoutAction type(String type) {
        this.params.setType(type);
        return this;
    }

    /**
     * Set 1 column layout template.
     * @return
     */
    public LayoutAction template1Column() {
        return template("1_column");
    }

    /**
     * Set 2 columns layout template (50%/50%).
     * @return
     */
    public LayoutAction template2Column5050() {
        return template("2_columns_i");
    }

    /**
     * Set 2 columns layout template (30%/70%).
     * @return
     */
    public LayoutAction template2Column3070() {
        return template("2_columns_ii");
    }

    /**
     * Set 2 columns layout template (30%/70%).
     * @return
     */
    public LayoutAction template2Column7030() {
        return template("2_columns_iii");
    }

    /**
     * Sets 3 columns layout template.
     * @return
     */
    public LayoutAction template3Column() {
        return template("3_columns");
    }

    /**
     * Sets custom template for this layout. Corresponds to layout-template element attribute "id".
     * @param templateId
     * @return
     */
    public LayoutAction template(String templateId) {
        this.params.setLayoutTemplate(templateId);
        return this;
    }

    public LayoutSubActions createAndSubAction() {
        Layout layout = create();
        return new LayoutSubActions(layout, this);
    }

    public LayoutSubActions createAndNext() {
        if(this.parentLayout == null || this.parentLayoutAction == null) {
            throw new RuntimeException("You cannot use createAndNext at this point. There are no parent layout.");
        }
        create();
        return new LayoutSubActions(this.parentLayout, this.parentLayoutAction);
    }

    public GroupSubActions createAndEnd() {
        create();
        return this.getGroupSubActions();
    }

    private Layout create() {
        try {
            return LayoutUtil.createLayout(this.getParams());
        } catch (SystemException e) {
            _log.error("Creating page failed.", e);
            throw new RuntimeException(e);
        } catch (PortalException e) {
            _log.error("Creating page failed.", e);
            throw new RuntimeException(e);
        }
    }

    protected Group getGroup() {
        return group;
    }

    protected GroupSubActions getGroupSubActions() {
        return groupSubActions;
    }

    protected LayoutParameters getParams() {
        return params;
    }
}
