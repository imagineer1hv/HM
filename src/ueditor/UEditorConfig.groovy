package ueditor

import org.json.JSONArray
import org.json.JSONObject
import ueditor.defination.ActionMap

class UEditorConfig{

    static final String configFileName = "ueditor-config.json"
    JSONObject configObj = null
    // 涂鸦上传filename定义
    final static String SCRAWL_FILE_NAME = "scrawl"
    // 远程图片抓取filename定义
    final static String REMOTE_FILE_NAME = "remote"

    /*
     * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
     */

    UEditorConfig(){
        try{
            String configContent = this.readConfigFile(new File(this.class.classLoader.getResource('ueditor/ueditor-config.json').toURI()))
            configObj=new JSONObject(configContent)
        }catch(Exception e){
            e.printStackTrace()
        }
    }

    // 验证配置文件加载是否正确
    boolean valid(){
        return this.configObj!=null
    }

    Map<String,Object> getConfig(int type){

        Map<String,Object> conf = new HashMap<String,Object>()
        String savePath = null

        switch(type){
            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64","false")
                conf.put("maxSize",this.configObj.getLong("fileMaxSize"))
                conf.put("allowFiles",this.getArray("fileAllowFiles"))
                conf.put("fieldName",this.configObj.getString("fileFieldName"))
                savePath=this.configObj.getString("filePathFormat")
                break

            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64","false")
                conf.put("maxSize",this.configObj.getLong("imageMaxSize"))
                conf.put("allowFiles",this.getArray("imageAllowFiles"))
                conf.put("fieldName",this.configObj.getString("imageFieldName"))
                savePath=this.configObj.getString("imagePathFormat")
                break

            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize",this.configObj.getLong("videoMaxSize"))
                conf.put("allowFiles",this.getArray("videoAllowFiles"))
                conf.put("fieldName",this.configObj.getString("videoFieldName"))
                savePath=this.configObj.getString("videoPathFormat")
                break

            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename",SCRAWL_FILE_NAME)
                conf.put("maxSize",this.configObj.getLong("scrawlMaxSize"))
                conf.put("fieldName",this.configObj.getString("scrawlFieldName"))
                conf.put("isBase64","true")
                savePath=this.configObj.getString("scrawlPathFormat")
                break

            case ActionMap.CATCH_IMAGE:
                conf.put("filename",REMOTE_FILE_NAME)
                conf.put("filter",this.getArray("catcherLocalDomain"))
                conf.put("maxSize",this.configObj.getLong("catcherMaxSize"))
                conf.put("allowFiles",this.getArray("catcherAllowFiles"))
                conf.put("fieldName",this.configObj.getString("catcherFieldName")+"[]")
                savePath=this.configObj.getString("catcherPathFormat")
                break

            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles",this.getArray("imageManagerAllowFiles"))
                conf.put("dir",this.configObj.getString("imageManagerListPath"))
                conf.put("count",this.configObj.getInt("imageManagerListSize"))
                break

            case ActionMap.LIST_FILE:
                conf.put("allowFiles",this.getArray("fileManagerAllowFiles"))
                conf.put("dir",this.configObj.getString("fileManagerListPath"))
                conf.put("count",this.configObj.getInt("fileManagerListSize"))
                break

        }
        conf.put("savePath",savePath)
        return conf
    }


    String[] getArray(String key){
        JSONArray jsonArray = this.configObj.getJSONArray(key)
        String[] result = new String[jsonArray.length()]
        int len = jsonArray.length();
        for(int i = 0;i<len;i++){
            result[i]=jsonArray.getString(i)
        }
        return result
    }

    String readConfigFile(File configFile) throws IOException{
        StringBuilder builder = new StringBuilder()
        try{
            InputStreamReader reader = new InputStreamReader(new FileInputStream(configFile),"UTF-8")
            BufferedReader bfReader = new BufferedReader(reader)
            String tmpContent = null
            while((tmpContent=bfReader.readLine())!=null){
                builder.append(tmpContent)
            }
            bfReader.close()
        }catch(UnsupportedEncodingException e){
        }
        return filter(builder.toString())
    }

    // 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
    static String filter(String input){
        return input.replaceAll("/\\*[\\s\\S]*?\\*/","")
    }
    
    static void main(String... args){
        println new UEditorConfig().configObj
    }

}
