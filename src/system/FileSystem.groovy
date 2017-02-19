package system

import config.WebAppConfig
import model.Model
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.annotation.SessionScope

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.regex.Matcher

@SessionScope
@Controller
class FileSystem{

    Model m=new Model()

    @GetMapping('/file/**')
    getFile(HttpServletRequest req,HttpServletResponse resp ){
        def uri=URLDecoder.decode(req.requestURI,'UTF-8')
        def mat=uri=~$/.*/file/(.*)/$
        mat.find()
        def p=mat.group(1)
        File f=new File(WebAppConfig.fileRootDir,p)
        if(!f.exists()) return -m<<'文件不存在'

//        resp.addHeader('Content-Disposition',"attachment; filename=${URLEncoder.encode(f.name,'UTF-8')}")
//        resp.contentType='application/octet-stream;'
        resp.addHeader('Content-Length',f.size() as String)
        try{
            resp.outputStream<<f.newInputStream()
        }catch(IOException e){}
        null
    }
}
