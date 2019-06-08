package com.iovation.launchkey.sdk.example.springmvc;

import com.iovation.launchkey.sdk.example.springmvc.model.LinkingData;
import com.iovation.launchkey.sdk.example.springmvc.model.LinkingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class DemoController {
    private final AuthManager authManager;

    @Autowired
    DemoController(AuthManager authManager) {
        this.authManager = authManager;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/link")
    public String linkGet(Model model) {
        model.addAttribute("linking", new LinkingRequest());
        return "link";
    }

    @PostMapping(value = "/link")
    public String linkPost(Model model, @ModelAttribute LinkingRequest linking) throws Exception {
        LinkingData linkingData = authManager.link(linking.getUsername());
        model.addAttribute("linkingData", linkingData);
        return "linking";
    }

    @GetMapping(value = "/linked/{deviceId}")
    public ResponseEntity linkedPost(@PathVariable String deviceId) {
        HttpStatus result =  authManager.isLinked(deviceId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND;
        return new ResponseEntity(result);
    }

    @RequestMapping(value = "/authorized", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public HttpEntity<Authorized> authorized(HttpSession session) {
        Authorized authorized = new Authorized();
        try {
            authorized.authorized = authManager.isAuthorized();
        } catch (AuthManager.AuthException e) {
            authorized.authorized = false;
        }
        if (!authorized.authorized) {
            session.invalidate();
        }
        return new ResponseEntity<>(authorized, HttpStatus.OK);
    }

    @RequestMapping(value = AuthManager.SERVICE_WEBHOOK, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void serviceWebhook (WebRequest request, @RequestBody String body) throws AuthManager.AuthException {
        Map<String, List<String>> headers = getWebhookHeaders(request);
        authManager.handleServiceWebhook(headers, body, "POST", AuthManager.SERVICE_WEBHOOK);
    }

    @RequestMapping(value = AuthManager.DIRECTORY_WEBHOOK, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void directoryWebhook (WebRequest request, @RequestBody String body) throws AuthManager.AuthException    {
        Map<String, List<String>> headers = getWebhookHeaders(request);
        authManager.handleDirectoryWebhook(headers, body, "POST", AuthManager.DIRECTORY_WEBHOOK);
    }

    public static class Authorized {
        public boolean authorized;
    }

    private static Map<String, List<String>> getWebhookHeaders(WebRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            headers.put(headerName, Arrays.asList(request.getHeaderValues(headerName)));
        }
        return headers;
    }
}
