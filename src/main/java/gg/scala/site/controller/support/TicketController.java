package gg.scala.site.controller.support;

import gg.scala.site.util.ErrorUtil;
import gg.scala.site.util.SessionUtil;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.model.support.SupportTicketModel;
import gg.scala.site.model.support.enums.TicketStatus;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TicketController {

    @RequestMapping("/ticket/{id}")
    public ModelAndView viewTicket(HttpServletRequest request, @PathVariable("id") String id) {
        ProfileModel profile = SessionUtil.getProfile(request);

        if (profile == null)
            return ErrorUtil.loginRedirect("/ticket/" + id);

        RequestResponse response = RequestHandler.get("forum/ticket/%s", id);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        SupportTicketModel ticket = new SupportTicketModel(response.asObject());
        if (!profile.getUuid().equals(ticket.getAuthor())
                && !profile.hasPermission("website.support.viewall")
                && (!profile.hasPermission("webite.support.category." + ticket.getCategory().name().toLowerCase())))
            return ErrorUtil.noPerms("You do not have permission to view this ticket.");

        ModelAndView view = new ModelAndView("support/ticket");
        view.addObject("ticket", ticket);
        view.addObject("statuses", TicketStatus.getSettable(ticket, ticket.getStatus()));

        if (ticket.getPunishment() != null)
            view.addObject("punishment", ticket.getPunishment());

        return view;
    }

}