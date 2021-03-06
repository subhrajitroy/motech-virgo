package org.motechproject.rules.service;

import org.motechproject.commons.api.MotechException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Load rule files from the file system
 *
 * @author Ricky Wang
 */
public class FilesystemRuleLoader {

    private static Logger logger = LoggerFactory.getLogger(FilesystemRuleLoader.class);

    private String internalRuleFolder;

    private String externalRuleFolder;

    @Autowired
    private KnowledgeBaseManagerInterface knowledgeBaseManager;

    /**
     * Load rule files from the internal and external rule folders
     *
     * @throws UnsupportedEncodingException
     */
    public void load() throws UnsupportedEncodingException {
        List<File> ruleFiles = new ArrayList<File>();
        if (internalRuleFolder != null) {
            File[] internalRuleFiles = new File(URLDecoder.decode(getClass().getResource(internalRuleFolder).getFile(), "UTF-8")).listFiles();
            ruleFiles.addAll(Arrays.asList(internalRuleFiles));
        }

        if (externalRuleFolder != null) {
            File folder = new File(externalRuleFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            } else {
                File[] externalRuleFiles = folder.listFiles();
                ruleFiles.addAll(Arrays.asList(externalRuleFiles));
            }
        }

        List<ClassLoader> classLoaders = new ArrayList<ClassLoader>();
        classLoaders.add(Thread.currentThread().getContextClassLoader());

        for (File file : ruleFiles) {
            if (file.getName().toLowerCase().endsWith(".drl")) {
                try {
                    knowledgeBaseManager.addOrUpdateRule(file, classLoaders.toArray(new ClassLoader[classLoaders.size()]));
                    logger.debug("Loaded rules from " + file.getAbsolutePath());
                } catch (IOException e) {
                    throw new MotechException("Failed to load the rule file [" + file.getName() + "]", e);
                }
            }
        }
    }

    public void setInternalRuleFolder(String ruleFolder) {
        this.internalRuleFolder = ruleFolder;
    }

    public void setExternalRuleFolder(String externalRuleFolder) {
        this.externalRuleFolder = externalRuleFolder;
    }

    public void setKnowledgeBaseManager(KnowledgeBaseManagerInterface knowledgeBaseManager) {
        this.knowledgeBaseManager = knowledgeBaseManager;
    }
}
