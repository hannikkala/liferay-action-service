package com.soikea.liferay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * User: bleed
 * Date: 26/02/16
 * Time: 11:15
 */
@Mojo(name = "collect")
@Execute(goal = "collect", phase = LifecyclePhase.GENERATE_RESOURCES)
public class CollectTimestampsMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.resources}")
    private List<Resource> resources;

    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private File outputDir;

    private static final Set<String> extensions = new HashSet<String>();

    static {
        extensions.add("xml");
        extensions.add("vm");
        extensions.add("ftl");
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        for(Resource resource : resources) {
            File resdir = new File(resource.getDirectory());
            try {
                processDirectory(resdir, resdir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processDirectory(File resDir, File resRootDir) throws IOException {
        getLog().info("Processing directory: " + resDir.getAbsolutePath());
        Map<String, Long> fileTimestampMap = new HashMap<String, Long>();
        for(File resFile : resDir.listFiles()) {
            if(resFile.isDirectory()) {
                processDirectory(resFile, resRootDir);
            } else {
                String extension = resFile.getName().substring(resFile.getName().lastIndexOf('.') + 1);
                if(extensions.contains(extension)) {
                    fileTimestampMap.put(resFile.getName(), resFile.lastModified());
                }
            }
        }
        if(fileTimestampMap.isEmpty()) {
            getLog().info("File timestamp map is empty. Json not written.");
            return;
        }
        getLog().info("Writing map to timestamp.json: " + fileTimestampMap);
        String relativePath = resDir.getPath().substring(resRootDir.getPath().length());
        File targetDir = new File(outputDir, relativePath);
        if(!targetDir.exists()) {
            targetDir.mkdirs();
        }
        getLog().info("Writing to directory: " + targetDir);
        File timestampJson = new File(targetDir, "timestamp.json");
        FileWriter writer = new FileWriter(timestampJson);
        Gson gson = new GsonBuilder().create();
        gson.toJson(fileTimestampMap, writer);
        writer.close();
    }
}
