package com.hannikkala.liferay.actions.ddmtemplate;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplateConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.hannikkala.liferay.actions.LiferayActionService;

import java.util.Locale;
import java.util.Map;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 16:13
 */
public class DDMTemplateUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMTemplateUtil.class);

    public static DDMTemplate addDDMTemplate(DDMTemplateParameters params, String filename) throws Exception {

        Long templateClassNameId = ClassNameLocalServiceUtil
                .getClassNameId(DDMStructure.class);
        String templateKey = filename.substring(0, filename.lastIndexOf('.'));
        long userId = params.getUserId();
        long groupId = params.getGroupId();

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setAddGroupPermissions(true);
        serviceContext.setAddGuestPermissions(true);

        String type = filename.substring(filename.lastIndexOf('.') + 1);

        Map<Locale, String> nameMap = params.getNameMap(filename);
        Map<Locale, String> descriptionMap = params.getDescriptionMap(filename);
        String script = params.getScript();
        long structureId = params.getStructureId();

        LiferayActionService.getLocales().forEach(locale -> {
            if(!nameMap.containsKey(locale)) {
                nameMap.put(locale, templateKey);
            }
            if(!descriptionMap.containsKey(locale)) {
                descriptionMap.put(locale, templateKey);
            }
        });

        DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.fetchTemplate(
                groupId, templateClassNameId, templateKey, true);
        if (ddmTemplate == null) {
            _log.info("Adding Template " + nameMap);
            return DDMTemplateLocalServiceUtil.addTemplate(userId, groupId,
                    templateClassNameId, structureId,
                    templateKey, nameMap, descriptionMap,
                    DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, type,
                    script, true, false, null, null, serviceContext);
        } else if (params.getLastUpdated().after(ddmTemplate.getModifiedDate())) {
            _log.info("Updating " + nameMap.values() + " template");
            return DDMTemplateLocalServiceUtil.updateTemplate(
                    ddmTemplate.getTemplateId(), structureId,
                    nameMap, descriptionMap,
                    DDMTemplateConstants.TEMPLATE_TYPE_DISPLAY, null, type,
                    script, true, false, null, null, serviceContext);
        }

        _log.info("Template " + nameMap.values() + " has newer version in Liferay. Not updating.");
        return ddmTemplate;
    }
}
