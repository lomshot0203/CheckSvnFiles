import file.CustomFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Exec {

    public static void main(String[] args) {
        ArrayList<CustomFile> changedFileList = new ArrayList<>();
        ArrayList<CustomFile> targetFileList = new ArrayList<>();
        HashMap<String, String> props = new HashMap<>();
        String[] ignores = {};

        props.putAll(CheckSvnFiles.getProps("resource"));
        ignores = getIgnores(props);

        changedFileList.addAll(getChangedFiles(props.get("COM.CHANGED.PATH")));

        targetFileList.addAll(getTargetFiles(changedFileList, props.get("COM.TARGET.PATH"), ignores));

        CheckSvnFiles.compareCustomFiles(changedFileList, targetFileList);

    }

    public static ArrayList<CustomFile> getChangedFiles(String path) {
        CheckSvnFiles.getChangedFiles(path);
        return CheckSvnFiles.getList();
    }

    public static ArrayList<CustomFile> getTargetFiles(ArrayList<CustomFile> changedFileList, String path, String[] ignores) {
        CheckSvnFiles.getTargetFiles(changedFileList, path, ignores);
        return CheckSvnFiles.getList();
    }

    public static String[] getIgnores (HashMap<String, String> props) {
        String[] str = props.get("COM.EXCEPTIONS").split(",");
        return str;
    }
}
