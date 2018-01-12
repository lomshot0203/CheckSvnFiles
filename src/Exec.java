import file.CustomFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Exec {

    public static void main(String[] args) {
        ArrayList<CustomFile> changedFileList = new ArrayList<>();
        ArrayList<CustomFile> targetFileList = new ArrayList<>();
        HashMap<String, String> props = new HashMap<>();

        props.putAll(CheckSvnFiles.getProps("resource"));

        changedFileList.addAll(getFileList(props.get("COM.CHANGED.PATH")));

        targetFileList.addAll(getFileList(props.get("COM.TARGET.PATH")));

        CheckSvnFiles.compareCustomFiles(changedFileList, targetFileList);

    }

    public static ArrayList<CustomFile> getChangedFilesFileList (String path) {
        CheckSvnFiles.getChangedFiles(path);
        return CheckSvnFiles.getList();
    }
}
