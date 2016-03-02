package com.hannikkala.liferay.actions;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Locale;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 23:36
 */
public class StartupActionExample extends SimpleAction {

    private static final Log _log = LogFactoryUtil.getLog(StartupActionExample.class);

    private static final Locale fi_FI = new Locale("fi", "FI");
    
    @Override
    public void run(String[] ids) throws ActionException {
        LiferayActionService.locales(Locale.US, fi_FI)
                .group()
                    .withName("Test")
                    .withDescription("Test description")
                    .withFriendlyUrl("/testsite")
                    .create()
                    .structure()
                        .withDirectory("structures")
                        .buildDescriptionMap("article-block-lift.xml", (descrMap) -> {
                            descrMap.put(Locale.US, "Article Block Lift");
                            descrMap.put(fi_FI, "Artikkelipalikan nosto");
                        })
                        .buildNameMap("article-block-lift.xml", (nameMap) -> {
                            nameMap.put(Locale.US, "Article Block Lift");
                            nameMap.put(fi_FI, "Artikkelipalikan nosto");
                        })
                        .createAll()
                    .template()
                        .withDirectory("templates")
                        .buildNameMap("article-block-lift.vm", (nameMap) -> {
                            nameMap.put(Locale.US, "Article Block Lift");
                            nameMap.put(fi_FI, "Artikkelipalikan nosto");
                        })
                        .buildDescriptionMap("article-block-lift.vm", (descrMap) -> {
                            descrMap.put(Locale.US, "Article Block Lift");
                        })
                        .buildStructureMap((map) -> {
                            map.put("article-block-lift.vm", "article-block-lift.xml");
                            map.put("article-block-lift-img-left.vm", "article-block-lift.xml");
                            map.put("article-block-lift-img-right.vm", "article-block-lift.xml");
                        })
                    .createAll();
        LiferayActionService.role("My Role").create();
    }
}
