import file.CustomFile;

import java.io.*;
import java.util.*;

public class CheckSvnFiles {

    private static ArrayList<String> list;

    public static ArrayList<String> getList() {
        return list;
    }

    /**    프로퍼티 내용을 가져온다.*/
    public static HashMap<String, String> getProps(String name) {
        HashMap<String, String> renMap = new HashMap<>();
        InputStream is = CheckSvnFiles.class.getClassLoader().getResourceAsStream(name+".properties");
        BufferedInputStream bs = new BufferedInputStream(is);
        Properties props = new Properties();
        try {
            props.load(bs);
            Enumeration<Object> propsKeys = props.keys();
            while (propsKeys.hasMoreElements()) {
                String key = propsKeys.nextElement().toString();
                renMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return renMap;
    }

        /**변경된 파일 리스트 가져오기*/
    public static void getChangedFiles (String path) {
        list = new ArrayList<>();
        File file = new File(path);
        getFileList(file);
    }

    /**변경대상 파일 리스트 가져오기*/
    public static ArrayList<String> getTargetFiles (ArrayList<String> changedFileList, String path, String[] ignores) {
        list = new ArrayList<>();
        File file = new File(path);
        getFileList(file);
        for (int idx=0; idx < changedFileList.size(); idx++) {
            if ( list.contains(changedFileList.get(idx)) ) {
                list.;
            }
        }
        return ;
    }


    /**파일 리스트를 반환한다.*/
    private static void getFileList(File file) {
        for (File item : file.listFiles()) {
            if (item.isDirectory()) {
                getFileList(item);
            }
            if (item != null && item.isFile() && item.canRead()) {
                list.add(item.getAbsolutePath());
            }
        }
    }

    /**파일 내용을 읽어온다*/
    public static void getFileContent () {

    }


    /** 파일 내용을 비교한다.*/
    public static ArrayList<CustomFile> compareCustomFiles (ArrayList<CustomFile> changedList, ArrayList<CustomFile> targetList) {
        return null;
    }

}
