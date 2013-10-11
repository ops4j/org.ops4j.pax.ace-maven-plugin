/*
 * Copyright (c) 2013 Dmytro Pishchukhin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.pax.maven.plugins.ace;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

/**
 * Upload Artifact from URL(s)
 *
 * @author dpishchukhin
 */
@Mojo(name = "upload-urls", requiresProject = false, aggregator = true)
public class UploadUrlsMojo extends AbstractUploadAceMojo {
    /**
     * Urls to upload
     */
    @Parameter(property = "urls", required = true)
    private URL[] urls;

    /**
     * Temporary Folder for downloaded artifacts
     */
    @Parameter(defaultValue = "${java.io.tmpdir}/ace", required = false)
    private transient File workDir;

    private File downloadDir;

    private void generateDirectoryTree() {
        this.downloadDir = new File(this.workDir, "download");
        this.downloadDir.mkdirs();
    }

    @Override
    protected List<File> getArtifactLocalFiles() throws IOException {
        generateDirectoryTree();

        List<File> files = new ArrayList<File>();

        if (urls != null) {
            for (URL url : urls) {
                files.add(downloadUrlLocally(url));
            }
        }

        return files;
    }

    private File downloadUrlLocally(URL url) throws IOException {
        String filePath = url.getPath();
        int lastSeparatorIndex = filePath.lastIndexOf('/');
        if (lastSeparatorIndex != -1) {
            filePath = filePath.substring(lastSeparatorIndex + 1);
        }
        File file = new File(this.downloadDir, filePath);
        FileOutputStream out = new FileOutputStream(file);
        InputStream in = url.openStream();
        try {
            out.getChannel().transferFrom(Channels.newChannel(in), 0, Integer.MAX_VALUE);
        } finally {
            in.close();
            out.close();
        }
        return file;
    }
}
