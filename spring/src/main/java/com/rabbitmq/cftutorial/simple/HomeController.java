package com.rabbitmq.cftutorial.simple;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.AmqpTemplate;

@Controller
public class HomeController {
    @Autowired AmqpTemplate amqpTemplate;

    @RequestMapping(value = "/")
    public String home(Model model) {
        model.addAttribute(new Message());
        return "WEB-INF/views/home.jsp";
    }

    @RequestMapping(value = "/publish", method=RequestMethod.POST)
    public String publish(Model model, Message message) {
        // Send a message to the "messages" queue
        amqpTemplate.convertAndSend("messages", message.getValue());
        model.addAttribute("published", true);
        return home(model);
    }

    @RequestMapping(value = "/get", method=RequestMethod.POST)
    public String get(Model model) {
        // Receive a message from the "messages" queue
        String message = (String)amqpTemplate.receiveAndConvert("messages");
        if (message != null)
            model.addAttribute("got", message);
        else
            model.addAttribute("got_queue_empty", true);

        return home(model);
    }
    
    // Allow to produce a message from something like curl
    // To send/produce from curl do: curl -X POST http://your_app_route.cfapps.io/produce?message=Hello1
    @RequestMapping(value = "/produce", method=RequestMethod.GET)
    public String produce(Model model, @RequestParam(value="message", required=true, defaultValue="Hello00") String messageContent) {
    	Message message = new Message();
    	message.setValue(messageContent);
        return publish(model, message);
    }    
}
