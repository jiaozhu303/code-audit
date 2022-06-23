package icons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;


public interface MyIcons {

    // action 16*16
    Icon Action = IconLoader.getIcon("/icons/braces_16*16.svg", MyIcons.class);
    // tool window 13*13
    Icon ToolWindow = IconLoader.getIcon("/icons/braces_13*13.svg", MyIcons.class);
    // Editor gutter 12*12
    Icon Editor = IconLoader.getIcon("/icons/snow_12*12.svg", MyIcons.class);
    // 12 * 12
    Icon EditorQuestion = IconLoader.getIcon("/icons/question_12*12.svg", MyIcons.class);

    Icon Exclamation = IconLoader.getIcon("/icons/exclamation_12*12.svg", MyIcons.class);
}
