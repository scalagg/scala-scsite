package gg.scala.site.controller.misc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MiscController {

    @RequestMapping("/policy")
    public ModelAndView privacyPolicy() {
        return new ModelAndView("misc/privacy-policy");
    }

    @RequestMapping("/ranked/tutorial")
    public ModelAndView rankedTutorial() {
        return new ModelAndView("misc/ranked-tutorial");
    }

    @RequestMapping("/terms")
    public ModelAndView termsofService() {
        return new ModelAndView("misc/terms");
    }
    @RequestMapping("/data")
    public ModelAndView dataPolicy() {
        return new ModelAndView("misc/data-policy");
    }
    @RequestMapping("/care")
    public ModelAndView carePolicy() {
        return new ModelAndView("misc/care");
    }
    @RequestMapping("/rules")
    public ModelAndView rules() {
        return new ModelAndView("redirect:http://scala.gg/thread/nqmfsfuc");
    }
    @RequestMapping("/forum-rules")
    public ModelAndView forumRules() {
        return new ModelAndView("misc/forum-rules");
    }

}