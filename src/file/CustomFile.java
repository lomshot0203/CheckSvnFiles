package file;

import java.io.File;

public class CustomFile{

    private String path;
    private String name;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content += content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
