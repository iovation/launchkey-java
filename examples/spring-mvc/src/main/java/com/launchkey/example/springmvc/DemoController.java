package com.launchkey.example.springmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@Controller
public class DemoController {
    private static final Logger LOG = LoggerFactory.getLogger(DemoController.class);
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

    @RequestMapping(value = "/authorized", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public HttpEntity<Authorized> authorized(HttpSession session) throws AuthManager.AuthException {
        Authorized authorized = new Authorized();
        try {
            authorized.authorized = (authManager.isAuthorized() == true);
        } catch (AuthManager.AuthException e) {
            authorized.authorized = false;
        }
        if (!authorized.authorized) {
            session.invalidate();
        }
        return new ResponseEntity<Authorized>(authorized, HttpStatus.OK);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void callback (WebRequest request) throws AuthManager.AuthException {
        Map<String, String> callbackData = new HashMap<String, String>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            callbackData.put(entry.getKey(), entry.getValue()[0]);
        }
        authManager.handleCallback(callbackData);
    }


    public static class Authorized {
        public boolean authorized;
    }
}
