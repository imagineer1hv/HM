package service

import config.WebAppConfig
import model.Content
import model.Department
import model.ResourceFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import repo.ContentRepo

@Service
class ContentService{

    @Autowired
    ContentRepo repo

    Content create( MultipartFile[] files,String contentText,String contextPath ){
        def resFiles=[]
        def fileDir=new File(WebAppConfig.fileRootDir,contextPath)
        fileDir.mkdirs()
        files.each{
            def f=new File(fileDir,it.originalFilename.replace(' ','-'))
            if(f.name.trim()&&!f.exists()){
                it.transferTo(f)
                resFiles<<new ResourceFile(f)
            }
        }
        repo.save(new Content(text: contentText,files: resFiles))
    }

    void delete(Content ct){

    }
}
