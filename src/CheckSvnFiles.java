import file.CustomFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class CheckSvnFiles {

    private static ArrayList<CustomFile> list;

    public static ArrayList<CustomFile> getList() {
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
    public static ArrayList<CustomFile> getTargetFiles (ArrayList<CustomFile> changedFileList, String path, String[] ignores) {
        list = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        File file = new File(path);
        getFileList(file);
        return getList();
    }


    /**변경대상 파일 리스트를 반환한다.*/
    private static void getFileList(File file) {
        for (File item : file.listFiles()) {
            if (item.isDirectory()) {
                getFileList(item);
            }
            if (item != null && item.isFile() && item.canRead()) {
                CustomFile cf = new CustomFile(item.getAbsolutePath());
                list.add(cf);
            }
        }
    }

    /**파일 내용을 읽어온다*/
    public static void getFileContent () {
        for(CustomFile file : list) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))){
                String line = "";
                while ((line = br.readLine()) != null) {
                    file.setContent(line);
//                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /** 파일 내용을 비교한다.*/
    public static ArrayList<CustomFile> compareCustomFiles (ArrayList<CustomFile> changedList, ArrayList<CustomFile> targetList) {
        for(CustomFile targetFile : targetList) {
            for (CustomFile changedFile : changedList) {
                if (targetFile.getAbsolutePath().equals(changedFile.getAbsolutePath())) {
                }
            }
        }
        return null;
    }

}
