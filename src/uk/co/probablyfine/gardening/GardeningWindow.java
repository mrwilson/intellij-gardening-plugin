package uk.co.probablyfine.gardening;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileAttributes;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Processor;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class GardeningWindow {
    private JTextPane fileList;
    private JPanel toolWindowContent;
    private static final Logger log = Logger.getInstance(GardeningWindow.class);

    public GardeningWindow(Project project) {
        this.listFilesForGardening(project);
    }


    public void listFilesForGardening(Project project) {
        Map<VirtualFile, FileAttributes> files = new HashMap<>();

        VfsUtilCore.processFilesRecursively(
            project.getBaseDir(),
            this.consumeFileInto(files)
        );

        String collect = files
            .entrySet()
            .stream()
            .map(x -> new FileWithLastModified(x.getKey().getName(), x.getValue().lastModified))
            .sorted(comparing(x -> x.lastModified))
            .limit(10)
            .map(this::mapToOutput)
            .collect(Collectors.joining("\n"));

        fileList.setText(collect);
    }

    private String mapToOutput(FileWithLastModified file) {
        return String.format(
            "%s (%d)",
            file.name,
            file.lastModified
        );
    }

    private Processor<VirtualFile> consumeFileInto(Map<VirtualFile, FileAttributes> list) {
        return (file) -> {
            if (!file.isDirectory()) {
                list.put(file, LocalFileSystem.getInstance().getAttributes(file));
            }
            return true;
        };
    }

    public JPanel getContent() {
        return toolWindowContent;
    }

    private class FileWithLastModified {
        private final String name;
        private final long lastModified;

        public FileWithLastModified(String name, long lastModified) {
            this.name = name;
            this.lastModified = lastModified;
        }
    }
}