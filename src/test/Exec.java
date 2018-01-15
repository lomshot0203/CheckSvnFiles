package test;

import file.CustomFile;
import util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Exec {

    public static void main(String[] args) {
        HashMap<String, String> props = Util.getProps("resource");
        String[] ignores = getIgnores(props, "COM.EXCEPTIONS");

        ArrayList<CustomFile> changeFileList =  Util.getTargetFileList(props.get("COM.CHANGED.PATH"), ignores);
        changeFileList = Util.getFileContent(changeFileList);

        ArrayList<CustomFile> targetFileList =  Util.getTargetFileList(props.get("COM.TARGET.PATH"), ignores);
        targetFileList = Util.getChangedFileList(changeFileList, targetFileList);
        targetFileList = Util.getFileContent(targetFileList);

        targetFileList = compareFile(changeFileList, targetFileList);

        Util.backupFiles(targetFileList, props.get("COM.BACKUP.PATH"));

        ArrayList<CustomFile> operationFile = copyFile(changeFileList, targetFileList);

        Util.printFileNameList(operationFile, props.get("COM.BACKUP.PATH"), "변경된 파일");
    }

    public static String[] getIgnores (HashMap<String, String> props, String key) {
        return props.get(key).split(",");
    }

    public static ArrayList<CustomFile> compareFile(ArrayList<CustomFile> changeFileList, ArrayList<CustomFile> targetFileList) {
        for (CustomFile cf : changeFileList) {
            for (int idx=0; idx<targetFileList.size(); idx++) {
                if (cf.getName().equals(targetFileList.get(idx).getName())) {
                    targetFileList.get(idx).setChgYn(Util.compareFileContent(cf, targetFileList.get(idx)));
                }
            }
        }
        return targetFileList;
    }

    public static ArrayList<CustomFile> copyFile(ArrayList<CustomFile> changeFileList, ArrayList<CustomFile> targetFileList) {
        ArrayList<CustomFile> rtnFileList = new ArrayList<>();
        for (CustomFile cf : changeFileList) {
            for (int idx=0; idx<targetFileList.size(); idx++) {
                int exeCnt = 0;
                /** 파일이름이 같고 변경된 대상 파일인 경우*/
                if (cf.getName().equals(targetFileList.get(idx).getName()) && "Y".equals(targetFileList.get(idx).getChgYn())) {
                    exeCnt = Util.copyFile(cf, targetFileList.get(idx));
                    if (exeCnt > 0) {
                        rtnFileList.add(targetFileList.get(idx));
                    }
                }
            }
        }
        return rtnFileList;
    }

}
