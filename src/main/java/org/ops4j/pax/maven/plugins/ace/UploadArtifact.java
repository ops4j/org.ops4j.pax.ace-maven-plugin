package org.ops4j.pax.maven.plugins.ace;

/**
 * @author dpishchukhin
 */
public class UploadArtifact {
    /**
     * Artifact group ID
     */
    private String groupId;
    /**
     * Artifact artifact ID
     */
    private String artifactId;
    /**
     * Artifact version
     */
    private String version;
    /**
     * Artifact type
     */
    private String type = "jar";
    /**
     * Artifact classifier
     */
    private String classifier;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }
}
