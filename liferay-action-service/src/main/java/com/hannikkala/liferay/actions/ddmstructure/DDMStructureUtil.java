package com.hannikkala.liferay.actions.ddmstructure;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructureConstants;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.hannikkala.liferay.actions.LiferayActionService;

import java.util.Locale;
import java.util.Map;

/**
 * User: tommih
 * Date: 23/02/16
 * Time: 14:52
 */
public class DDMStructureUtil {

    private static final Log _log = LogFactoryUtil.getLog(DDMStructureUtil.class);

    public static DDMStructure addDDMStructure(String filename, DDMStructureParameters params) throws Exception {

        Long structureClassNameId = ClassNameLocalServiceUtil
                .getClassNameId(JournalArticle.class);
        String ddmStructureKey = filename.substring(0, filename.lastIndexOf('.'));
        ;
        long userId = params.getUserId();
        long groupId = params.getGroupId();

        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setAddGroupPermissions(true);
        serviceContext.setAddGuestPermissions(true);
        serviceContext.setScopeGroupId(groupId);
        //serviceContext.setUserId(userId);

        String xml = params.getXml();
        Document document = SAXReaderUtil.read(xml);
        String xsd = document.getRootElement().asXML();
        Map<Locale, String> nameMap = params.getNameMap(filename);
        Map<Locale, String> descriptionMap = params.getDescriptionMap(filename);

        LiferayActionService.getLocales().forEach(locale -> {
            if(!nameMap.containsKey(locale)) {
                nameMap.put(locale, ddmStructureKey);
            }
            if(!descriptionMap.containsKey(locale)) {
                descriptionMap.put(locale, ddmStructureKey);
            }
        });

        DDMStructure ddmStructure = DDMStructureLocalServiceUtil
                .fetchStructure(groupId, structureClassNameId, ddmStructureKey);

        if (ddmStructure == null) {
            _log.info("Adding a new structure " + nameMap);
            return DDMStructureLocalServiceUtil.addStructure(userId,
                    groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
                    structureClassNameId, ddmStructureKey, nameMap,
                    descriptionMap, xsd, "xml",
                    DDMStructureConstants.TYPE_DEFAULT, serviceContext);
        } else if (params.getLastUpdated().after(ddmStructure.getModifiedDate())) {
            _log.info("Updating " + nameMap.values() + " structure");
            ddmStructure.setXsd(xsd);
            return DDMStructureLocalServiceUtil.updateStructure(
                    ddmStructure.getStructureId(),
                    DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID, nameMap,
                    descriptionMap, xsd, serviceContext);
        }

        _log.info("Structure " + nameMap.values() + " has newer version in Liferay. Not updating.");
        return ddmStructure;
    }
}
