package uk.co.probablyfine.gardening;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;


public class GardeningWindowFactory implements ToolWindowFactory {

    private static final Logger logger = Logger.getInstance(GardeningWindowFactory.class);

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        logger.warn("STARTING");

        GardeningWindow myToolWindow = new GardeningWindow(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
