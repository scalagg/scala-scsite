package gg.scala.site.controller.auth;

import com.google.gson.JsonObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import gg.scala.site.util.ErrorUtil;
import gg.scala.site.util.SessionUtil;
import gg.scala.site.util.password.PasswordValidation;
import gg.scala.site.model.account.ForumAccountModel;
import gg.scala.site.model.profile.ProfileModel;
import gg.scala.site.request.RequestHandler;
import gg.scala.site.request.RequestResponse;
import gg.scala.site.util.password.impl.DigitRule;
import gg.scala.site.util.password.impl.LengthRule;
import gg.scala.site.util.password.impl.LowercaseRule;
import gg.scala.site.util.password.impl.UppercaseRule;
import gg.scala.site.util.password.result.PasswordResult;
import gg.scala.site.util.uuid.UUIDCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
public class AuthController {

    public static final PasswordValidation VALIDATION = new PasswordValidation(
            new LengthRule(6, 32),
            new DigitRule(1),
            new LowercaseRule(1),
            new UppercaseRule(1)
    );

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, @RequestParam(value = "redirect", required = false, defaultValue = "") String redirect) {
        ModelAndView view = new ModelAndView("redirect:/" + redirect);
        request.setAttribute("redirect", "/" + redirect);
        request.getSession().setAttribute("redirect", "/" + redirect);

        ProfileModel profile = SessionUtil.getProfile(request);
        if (profile != null)
            return view;

        return new ModelAndView("auth/login");
    }

    @GetMapping("/register")
    public ModelAndView register(HttpServletRequest request, @RequestParam(value = "token", defaultValue = "") String token) {
        ProfileModel currentProfile = SessionUtil.getProfile(request);
        if (currentProfile != null)
            return new ModelAndView("redirect:/u/" + currentProfile.getName());

        if (token == null || token.isEmpty())
            return new ModelAndView("auth/register-steps");

        RequestResponse response = RequestHandler.get("forum/account/token/%s", token);
        if (!response.wasSuccessful())
            return ErrorUtil.create(response);

        ModelAndView view = new ModelAndView("auth/register");
        JsonObject object = response.asObject();

        view.addObject("token", token);
        view.addObject("email", object.get("email").getAsString());
        view.addObject("username", UUIDCache.getName(UUID.fromString(object.get("uuid").getAsString())));

        return view;
    }

    @PostMapping("/register/{token}")
    public ModelAndView postRegistration(@Valid ForumAccountModel model,
                                         HttpServletRequest request,
                                         @PathVariable("token") String token,
                                         @RequestParam("password") String password) {
        ModelAndView view = new ModelAndView("redirect:/login");
        PasswordResult validate = VALIDATION.validate(password);

        if (!validate.isValid()) {
            request.getSession().setAttribute("error_message", validate.getMessage());
            return new ModelAndView("redirect:/register?token=" + model.getToken());
        }

        JsonObject body = new JsonObject();
        body.addProperty("token", token);
        body.addProperty("password", password);

        RequestResponse response = RequestHandler.post("forum/account/register", body);
        if (!response.wasSuccessful()) {
            request.getSession().setAttribute("error_message", response.getErrorMessage());
            return new ModelAndView("redirect:/register?token=" + model.getToken());
        }

        return view;
    }

}