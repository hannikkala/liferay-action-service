package com.hannikkala.liferay.actions.ddmstructure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hannikkala.liferay.actions.LiferayActionService;
import com.hannikkala.liferay.actions.group.GroupSubActions;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.Group;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 23/02/16
 * Time: 17:42
 */
public class DDMStructureAction {

    private final Group group;
    private final DDMStructureParameters params = new DDMStructureParameters();
    private final GroupSubActions groupSubActions;
    private File structureDir;

    private static final Log _log = LogFactoryUtil.getLog(DDMStructureAction.class);

    public DDMStructureAction(Group group, GroupSubActions groupSubActions) {
        this.group = group;
        this.params.setGroupId(group.getGroupId());
        this.params.setUserId(group.getCreatorUserId());
        this.groupSubActions = groupSubActions;
    }

    public DDMStructureAction withDirectory(String path) {
        URL resourceURL = getClass().getClassLoader().getResource(path);
        try {
            this.structureDir = new File(resourceURL.toURI());
            if(!this.structureDir.exists()) {
                throw new RuntimeException("File " + this.structureDir.getAbsolutePath() + " does not exist.");
            }
        } catch (Exception e) {
            _log.error("Error setting structure path", e);
        }
        Map<String, Double> timestampMap = readTimestampMap(this.structureDir);
        Map<String, Long> map = new HashMap<>();
        for(Map.Entry<String, Double> entry : timestampMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().longValue());
        }
        this.params.setTimestampMap(map);
        return this;
    }

    /**
     * Use name with all allowed locales.
     * @param filename
     * @param name
     * @return
     */
    public DDMStructureAction withName(String filename, String name) {
        LiferayActionService.getLocales().forEach(locale -> {
            this.params.getNameMap(filename).put(locale, name);
        });
        return this;
    }

    public DDMStructureAction buildNameMap(String filename, Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getNameMap(filename));
        return this;
    }

    /**
     * Use description with all allowed locales.
     * @param filename
     * @param description
     * @return
     */
    public DDMStructureAction withDescription(String filename, String description) {
        LiferayActionService.getLocales().forEach(locale -> {
            this.params.getDescriptionMap(filename).put(locale, description);
        });
        return this;
    }

    public DDMStructureAction buildDescriptionMap(String filename, Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getDescriptionMap(filename));
        return this;
    }

    public GroupSubActions createAll() {
        for(File f : this.structureDir.listFiles()) {
            if(f.getName().endsWith("xml")) {
                try {
                    this.params.setFile(f);
                    DDMStructureUtil.addDDMStructure(f.getName(), params);
                } catch (Exception e) {
                    _log.error("Handling structure " + f.getName() + " failed.", e);
                }
            }
        }
        return this.groupSubActions;
    }

    private Map<String, Double> readTimestampMap(File dir) {
        Gson gson = new GsonBuilder().create();
        try {
            Reader reader = new FileReader(new File(dir, "timestamp.json"));
            return gson.fromJson(reader, Map.class);
        } catch (FileNotFoundException e) {
            _log.info("Exception reading timestamp.json from directory: " + dir.getAbsolutePath(), e);
        }
        return new HashMap<>();
    }

}
