package gg.scala.site.controller.forum;

import com.google.gson.JsonObject;
import gg.scala.site.SiteConstant;
import gg.scala.site.model.replies.ReplyModel;
import gg.scala.site.util.ErrorUtil;
import gg.scala.site.util.PopupUtil;
import gg.scala.site.util.SessionUtil;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.model.forum.ForumThread;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/reply")
public class ReplyController {

    @PostMapping("/{threadId}")
    public ModelAndView reply(HttpServletRequest request,
                              @PathVariable("threadId") String threadId,
                              @RequestParam(value = "replyId", required = false) String replyId,
                              @RequestParam("body") String content) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/thread/%s", threadId);

        RequestResponse response = RequestHandler.get("forum/thread/%s", threadId);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        ForumThread mainThread = new ForumThread(response.asObject());

        if (mainThread.isLocked() && !profile.hasPermission("website.thread.lock")) {
            PopupUtil.error(request.getSession(), "This thread is locked.");
            return new ModelAndView("redirect:/thread/" + threadId);
        }

        // Convert newlines to HTML line breaks before sanitizing
        String contentWithBreaks = content.replaceAll("\\r?\\n", "<br>");
        String sanitizedContent = SiteConstant.POLICY_FACTORY.sanitize(contentWithBreaks);
        if (sanitizedContent.isBlank()) {
            PopupUtil.error(request.getSession(), "The reply cannot be empty.");
            return new ModelAndView("redirect:/thread/" + threadId);
        }

        UUID id = UUID.randomUUID();
        JsonObject body = new JsonObject();

        body.addProperty("uuid", id.toString());
        body.addProperty("comment", sanitizedContent);
        body.addProperty("author", profile.getUuid().toString());
        if(replyId != null)
            body.addProperty("parentReplyId", replyId);

        response = RequestHandler.post(
                "forum/replies/%s",
                body, threadId
        );

        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        ForumThread.REPLY_CACHE.forceExecute();
        return new ModelAndView("redirect:/thread/" + threadId);
    }

    @PostMapping("/delete")
    public ModelAndView delete(HttpServletRequest request,
                               @RequestParam("parentId") String parentId,
                               @RequestParam("replyId") String replyId) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/thread/%s", parentId);

        RequestResponse response = RequestHandler.get("forum/replies/%s", replyId);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        ReplyModel replyModel = new ReplyModel(response.asObject());
        if (!replyModel.canDelete(profile)) {
            PopupUtil.error(request.getSession(), "You cannot delete this reply.");
            return new ModelAndView("redirect:/thread/" + parentId);
        }

        response = RequestHandler.delete("forum/replies/%s", replyId);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        ForumThread.REPLY_CACHE.forceExecute();
        return new ModelAndView("redirect:/thread/" + parentId);
    }

}