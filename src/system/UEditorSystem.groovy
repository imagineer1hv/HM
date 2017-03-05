package system

import model.Model
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.annotation.SessionScope
import ueditor.ActionEnter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@SessionScope
class UEditorSystem{
    
    Model m    = new Model()
    
    @PostMapping('/ueditor/submit')
    ueditorSubmit(@RequestParam('content')String ct){
        m.content=ct
        m>>'/ueditor-test.jsp'
    }
    
    @RequestMapping('/ueditor')
    void ueditorAction(HttpServletRequest req,HttpServletResponse resp){
        resp.setHeader('Content-Type','text/html')
        def s = new ActionEnter(req).exec()
        resp.outputStream.println(new String(s))
        resp.flushBuffer()
    }
}
