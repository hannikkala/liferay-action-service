package com.hannikkala.liferay.actions.ddmtemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
 * User: bleed
 * Date: 23/02/16
 * Time: 23:03
 */
public class DDMTemplateAction {
    private final Group group;
    private final GroupSubActions groupSubActions;
    private File templateDir;
    private final DDMTemplateParameters params = new DDMTemplateParameters();
    private Map<String, String> structureMap = new HashMap<>();

    private static final Log _log = LogFactoryUtil.getLog(DDMTemplateAction.class);

    public DDMTemplateAction(Group group, GroupSubActions groupSubActions) {
        this.group = group;
        this.params.setGroupId(group.getGroupId());
        this.params.setUserId(group.getCreatorUserId());
        this.groupSubActions = groupSubActions;
    }

    public DDMTemplateAction withDirectory(String path) {
        URL resourceURL = getClass().getClassLoader().getResource(path);
        try {
            this.templateDir = new File(resourceURL.toURI());
            if(!this.templateDir.exists()) {
                throw new RuntimeException("File " + this.templateDir.getAbsolutePath() + " does not exist.");
            }
        } catch (Exception e) {
            _log.error("Error setting template path", e);
        }
        Map<String, Double> timestampMap = readTimestampMap(this.templateDir);
        Map<String, Long> map = new HashMap<>();
        for(Map.Entry<String, Double> entry : timestampMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().longValue());
        }
        this.params.setTimestampMap(map);
        return this;
    }

    public DDMTemplateAction buildNameMap(String filename, Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getNameMap(filename));
        return this;
    }

    public DDMTemplateAction buildDescriptionMap(String filename, Consumer<Map<Locale, String>> func) {
        func.accept(this.params.getDescriptionMap(filename));
        return this;
    }

    public DDMTemplateAction buildStructureMap(Consumer<Map<String, String>> func) {
        func.accept(this.structureMap);
        return this;
    }

    public GroupSubActions createAll() {
        for(File f : this.templateDir.listFiles()) {
            if(!this.structureMap.containsKey(f.getName())) {
                _log.info("Template " + f.getName() + " skipped because there were no structure mapping.");
                continue;
            }
            if(f.getName().endsWith("ftl") || f.getName().endsWith("vm")) {
                try {
                    this.params.setFile(f);
                    DDMTemplateUtil.addDDMTemplate(params, f.getName());
                } catch (Exception e) {
                    _log.error("Handling DDMTemplate failed.", e);
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
