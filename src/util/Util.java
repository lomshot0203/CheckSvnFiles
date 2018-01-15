package util;

import file.CustomFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {

    private static ArrayList<CustomFile> fileNameList;

    public static ArrayList<CustomFile> getFileNameList() {
        return fileNameList;
    }

    /**    프로퍼티 내용을 가져온다.*/
    public static HashMap<String, String> getProps(String name) {
        HashMap<String, String> renMap = new HashMap<>();
        InputStream is = Util.class.getClassLoader().getResourceAsStream(name+".properties");
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

    /** 파일추출*/
    public static ArrayList<CustomFile> getTargetFileList (String path, String[] ignores) {
        fileNameList = new ArrayList<>();
        getFileList(path, ignores);
        return fileNameList;
    }

    /**파일 리스트를 반환한다.*/
    private static void getFileList(String path, String[] ignores) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        for (int i=0; i<fileList.length; i++) {
            if (fileList[i].isFile() && fileList[i].canRead()) {
                if (ignores != null && ignores.length > 0) {
                    for (String ignore : ignores) {
                        if (fileList[i].getAbsolutePath().lastIndexOf(ignore) == -1) {
                            CustomFile cf = new CustomFile(fileList[i].getAbsolutePath());
                            cf.setName(fileList[i].getName());
                            cf.setPath(fileList[i].getAbsolutePath());
                            fileNameList.add(cf);
                            break;
                        }
                    }
                } else {
                    CustomFile cf = new CustomFile(fileList[i].getAbsolutePath());
                    cf.setName(fileList[i].getName());
                    cf.setPath(fileList[i].getAbsolutePath());
                    fileNameList.add(cf);
                }
            }

            if (fileList[i].isDirectory() && fileList[i].canRead()) {
                String tmpPath = fileList[i].getAbsolutePath();
                if (new File(tmpPath).listFiles().length > 0) {
                    getFileList(tmpPath, ignores);
                }
            }
        }
    }

    /** 대상파일 리스트를 기준으로 변경 파일의 이름에 해당하는 파일 리스트를 추출한다.*/
    public static ArrayList<CustomFile> getChangedFileList (ArrayList<CustomFile> changeList, ArrayList<CustomFile> targetList) {
        fileNameList = new ArrayList<>();
        for (CustomFile path : targetList) {
            for (CustomFile name : changeList) {
                if (path.getPath().lastIndexOf(name.getName()) != -1) {
                    fileNameList.add(path);
                }
            }
        }
        return fileNameList;
    }

    /**파일 내용을 읽어온다*/
    public static ArrayList<CustomFile> getFileContent (ArrayList<CustomFile> param) {
        for (int idx=0; idx<param.size(); idx++) {
            try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(new File(param.get(idx).getAbsolutePath())))) {
                byte[] readBuffer = new byte[br.available()];
                while ((br.read(readBuffer, 0, readBuffer.length)) != -1) {
                    param.get(idx).setContent(readBuffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return param;
    }

    /**파일 내용을 비교한다.*/
    public static String compareFileContent (CustomFile param1, CustomFile param2) {
        String rtnVal = "N";
        byte[] param1Byte = getContentToByteArr(param1);
        byte[] param2Byte = getContentToByteArr(param2);

        if (param1Byte.length != param2Byte.length) { /* 배열 크기체크*/
            rtnVal = "Y";
            return rtnVal;
        }

        for (int idx=0; idx<param1Byte.length; idx++) { /* 배열의 내용이 다른것 체크*/
            if (param1Byte[idx] != param2Byte[idx]) {
                rtnVal = "Y";
                return rtnVal;
            }
        }
        return rtnVal;
    }

    /** 파일내용을 바이트 배열로 리턴한다.*/
    private static byte[] getContentToByteArr (CustomFile param) {
        int arrSize = 0;
        for (byte[] content : param.getContent()) { // 배열은 가변되지 않으므로 초기에 사이즈를 정해준다.
            arrSize += content.length;
        }

        byte[] tmpBytes = new byte[arrSize];
        int totalIdx = 0;
        for (byte[] item1 : param.getContent()) {
            for (int idx1=0; idx1<item1.length; idx1++) {
                tmpBytes[totalIdx] = item1[idx1];
                totalIdx++;
            }
        }
        return tmpBytes;
    }

    /** 변경할 파일의 원본파일을 백업한다.*/
    public static void backupFiles(ArrayList<CustomFile> param, String path) {
        File file = new File(path);
        if (!file.exists()) { /*디렡토리 존재확인*/
            file.mkdir();
        }
        for (CustomFile cf : param) {
            if ("Y".equals(cf.getChgYn())) {
                try(FileWriter fw = new FileWriter(path+"/"+cf.getName())){
                    for (byte[] b : cf.getContent()) {
                        fw.write(new String(b));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 변경 대상인 파일을 백업폴더로 복사한다.*/
    public static int copyFile (CustomFile change, CustomFile target) {
        int result = 0;
        String targetPath = target.getAbsolutePath();
        File targetFile = new File(targetPath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        try(FileWriter fw = new FileWriter(targetFile)){
            for (byte[] b : change.getContent()) {
                fw.write(new String(b));
            }
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }


    /** 파일 리스트를 출력한다.*/
    public static void printFileNameList (ArrayList<CustomFile> param, String path, String title) {
        Date date = new Date();
        String format = new SimpleDateFormat("yyyyMMdd").format(date);
        String titleFormat = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String backupPath = path +"/"+format+"_backup.txt";
        File file = new File(backupPath);
        try(FileWriter fw = new FileWriter(file, true)) {
            String content = "";
            content += "************************************* "+ titleFormat +" / "+ title+" ****************************\r\n";
            for (CustomFile list : param) {
                content += list.getAbsolutePath() +"\r\n";
            }
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
