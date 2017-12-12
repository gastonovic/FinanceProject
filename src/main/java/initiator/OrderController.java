package initiator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.WebContext;
import quickfix.Application;
import org.thymeleaf.TemplateEngine;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    ClientApplicationAdapter clientApplication;

    @GetMapping("/passorder")
    public String orderForm(Model model) {
        model.addAttribute("order", new OrderF());
        return "order";
    }

    @PostMapping("/passorder")
    public String orderSubmit(@ModelAttribute OrderF order) {
        try {
            clientApplication.send(order);
            clientApplication.user.getOrdres().add(order);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return "result";
    }
    @GetMapping("/myorders")

    public ModelAndView viewOrders() {

        ModelAndView mav = new ModelAndView("myorders");
        mav.addObject("myOrders", clientApplication.user.getOrdres());
        return mav;

    }

}
