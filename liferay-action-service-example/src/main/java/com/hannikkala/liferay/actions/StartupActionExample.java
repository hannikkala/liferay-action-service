package com.hannikkala.liferay.actions;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;

import java.util.Locale;

import static com.hannikkala.liferay.actions.LiferayActionService.locales;
import static com.hannikkala.liferay.actions.LiferayActionService.role;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 23:36
 */
public class StartupActionExample extends SimpleAction {

    private static final Locale fi_FI = new Locale("fi", "FI");
    
    @Override
    public void run(String[] ids) throws ActionException {
        // @formatter:off
        locales(Locale.US, fi_FI)
            .allowUpdate(false)
            .group()
                .withName("Test")
                .withDescription("Test description")
                .withFriendlyUrl("/testsite")
                .withTheme("profibusiness_WAR_profibusinesstheme")
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
                .createAll()
                .page()
                    .template1Column()
                    .buildNameMap((map) -> {
                        map.put(Locale.US, "Test page");
                        map.put(fi_FI, "Testisivu");
                    })
                    .buildFriendlyUrlMap((map) -> {
                        map.put(Locale.US, "/testpage");
                        map.put(fi_FI, "/testisivu");
                    }).createAndSubAction()
                    .page()
                        .template2Column3070()
                        .withName("Test sub page")
                        .withFriendlyUrl("/testsubpage")
                        .createAndNext()
                    .page()
                        .template1Column()
                        .withName("Test sub page sibling")
                        .withFriendlyUrl("/testsibling")
                        .createAndEnd()
                ;

        role("My Role").create();
        // @formatter:on
    }
}
