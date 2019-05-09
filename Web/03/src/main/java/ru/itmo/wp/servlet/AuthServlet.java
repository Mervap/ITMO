package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;

public class AuthServlet extends HttpServlet {

    private class Message {
        private String user;
        private String text;

        private Message(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }

    private ArrayList <Message> messages = new ArrayList <>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        HttpSession session = request.getSession(true);
        response.setContentType("application/json");
        String json;
        switch (uri) {
            case "/message/auth":
                String name = (String) session.getAttribute("user");
                if (name == null) {
                    name = request.getParameter("user");
                    if (name == null) {
                        name = "";
                    } else {
                        session.setAttribute("user", name);
                    }
                }
                json = new Gson().toJson(name);
                response.getWriter().print(json);
                response.getWriter().flush();
                break;
            case "/message/findAll":
                json = new Gson().toJson(messages);
                response.getWriter().print(json);
                response.getWriter().flush();
                break;
            case "/message/add":
                messages.add(new Message((String) session.getAttribute("user"), request.getParameter("text")));
                break;

        }
    }
}
