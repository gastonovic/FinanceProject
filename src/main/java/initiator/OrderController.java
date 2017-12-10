package initiator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import quickfix.Application;

@Controller
public class OrderController {
    @Autowired
    ClientApplicationAdapter clientApplication;

    @GetMapping("/passorder")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order";
    }

    @PostMapping("/passorder")
    public String orderSubmit(@ModelAttribute Order order) {
        clientApplication.buyOrder(order);
        return "result";
    }

}
