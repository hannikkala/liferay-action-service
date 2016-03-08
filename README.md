# Simple Liferay service for creating items for development

Does it bug you that when you're starting a project with Liferay, it requires lots of work to get Sites, Roles etc up and running? Here's the solution. A simple utility to hide all Liferay API madness and gives an imperative way to define the structure.

Utility can be ran as Liferay hook. Now it supports creating sites, structures, templates, pages and roles.

### TODO

- [x] ~~Pages/Layouts~~
- [ ] Theme selection
- [ ] Adding portlets

> Note! This utility requires Java8.

## How to setup

The fastest way is to use Liferay hook archetype of your Liferay version. Then it requires changes in a few files:

#### liferay-hook.xml

``` xml
<hook>
    <portal-properties>portal-override.properties</portal-properties>
</hook>
```

#### portal-override.properties

``` properties
application.startup.events=com.hannikkala.liferay.actions.StartupActionExample
```

If the file does not exist, create one to your classpath (in Maven src/main/resources). The class mentioned points to your startup hook.

#### StartupActionExample.java (or what ever you chose)

This class is your entry point and here you can start using some utility magic.

``` java
// Import LiferayActionService statically
import static com.hannikkala.liferay.actions.LiferayActionService.*;

public class StartupActionExample extends SimpleAction {

    @Override
    public void run(String[] ids) throws ActionException {
        locales(Locale.US)
            .allowUpdate(false) // We don't want to force portal to update objects on every startup.
            .group() // Begin to create a site with name and description. Liferay doesn't allow localization.
                .withName("Test")
                .withDescription("Test description")
                .withFriendlyUrl("/testsite") // Required. Use only the last part with preceeding slash.
                .create() // This gives you actions to create structures, templates and pages on created site.
                
                // Structure action
                .structure() // Create structures from classpath relative directory "structures". Files with .xml extension are noticed.
                    .withDirectory("structures")
                    .buildDescriptionMap("article-block-lift.xml", (descrMap) -> { // Give human readable name(s) to the structure
                        descrMap.put(Locale.US, "Article Block Lift");
                    })
                    .buildNameMap("article-block-lift.xml", (nameMap) -> { // Additionally you can give description.
                        nameMap.put(Locale.US, "Article Block Lift");
                    })
                    .createAll() // Create all structures in directory to "/testsite"
                    
                // Template action
                .template() // Create structures from classpath relative directory "templates". Files with .vm and .ftl extensions are noticed.
                    .withDirectory("templates")
                    .buildNameMap("article-block-lift.vm", (nameMap) -> { // Give names
                        nameMap.put(Locale.US, "Article Block Lift");
                    })
                    .buildDescriptionMap("article-block-lift.vm", (descrMap) -> { // Give descriptions
                        descrMap.put(Locale.US, "Article Block Lift");
                    })
                    .buildStructureMap((map) -> { // This mapping is in format "template" -> "structure"
                        map.put("article-block-lift.vm", "article-block-lift.xml");
                        map.put("article-block-lift-img-left.vm", "article-block-lift.xml");
                        map.put("article-block-lift-img-right.vm", "article-block-lift.xml");
                    })
                    .createAll(); // Create all templates to group "/testsite"
                    
                // Page hierarchy
                .page()
                    .template1Column()
                    .buildNameMap((map) -> { // Either build name, description and friendly url map
                        map.put(Locale.US, "Test page");
                    })
                    .buildFriendlyUrlMap((map) -> {
                        map.put(Locale.US, "/testpage");
                    }).createAndSubAction() // Create page and start creating child objects.
                    .page()
                        .template2Column3070()
                        .withName("Test sub page") // Or use convenience methods if you have only one locale.
                        .withFriendlyUrl("/testsubpage")
                        .createAndNext() // Create page and start creating siblings
                    .page()
                        .template1Column()
                        .withName("Test sub page sibling")
                        .withFriendlyUrl("/testsibling")
                        .createAndEnd()
                        
        // Utility allows to create roles
        role("My Role").create();
    }
}
```

> Note! If you don't give structures or templates names, file name without extension will be used.