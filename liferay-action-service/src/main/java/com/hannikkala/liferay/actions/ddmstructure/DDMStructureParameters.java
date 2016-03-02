package com.hannikkala.liferay.actions.ddmstructure;

import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.util.ContentUtil;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * User: bleed
 * Date: 23/02/16
 * Time: 17:44
 */
public class DDMStructureParameters {
    private Map<String, Map<Locale, String>> nameMap = new HashMap<String, Map<Locale, String>>();
    private Map<String, Map<Locale, String>> descriptionMap = new HashMap<String, Map<Locale, String>>();
    private long userId;
    private long groupId;
    private String xml;
    private Date lastUpdated;
    private Map<String, Long> timestampMap;

    public void addName(String filename, Locale locale, String name) {
        if(!this.nameMap.containsKey(filename)) {
            this.nameMap.put(filename, new HashMap<Locale, String>());
        }
        this.nameMap.get(filename).put(locale, name);
    }

    public Map<Locale, String> getNameMap(String filename) {
        if(!this.nameMap.containsKey(filename)) {
            this.nameMap.put(filename, new HashMap<Locale, String>());
        }
        return nameMap.get(filename);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void addDescription(String filename, Locale locale, String description) {
        if(!this.descriptionMap.containsKey(filename)) {
            this.descriptionMap.put(filename, new HashMap<Locale, String>());
        }
        this.descriptionMap.get(filename).put(locale, description);
    }

    public Map<Locale, String> getDescriptionMap(String filename) {
        if(!this.descriptionMap.containsKey(filename)) {
            this.descriptionMap.put(filename, new HashMap<Locale, String>());
        }
        return descriptionMap.get(filename);
    }

    public void setFile(File file) throws IOException {
        this.xml = FileCopyUtils.copyToString(new FileReader(file));
        if(this.timestampMap.containsKey(file.getName())) {
            this.lastUpdated = new Date(this.timestampMap.get(file.getName()));
        } else {
            this.lastUpdated = new Date(file.lastModified());
        }
    }

    public String getXml() {
        return this.xml;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setTimestampMap(Map<String, Long> timestampMap) {
        this.timestampMap = timestampMap;
    }

    public Map<String, Long> getTimestampMap() {
        return timestampMap;
    }
}
