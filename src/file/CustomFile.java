package file;

import java.io.File;

public class CustomFile extends File{

    private String content;

    public CustomFile(String pathname) {
        super(pathname);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content += content;
    }

    @Override
    public String getAbsolutePath() {
        return super.getAbsolutePath();
    }

    @Override
    public String toString() {
        return "CustomFile{" +
                "content=" + content +
                '}';
    }
}
