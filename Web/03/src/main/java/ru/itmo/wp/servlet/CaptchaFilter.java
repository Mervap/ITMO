package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (request.getMethod().equals("GET") && session.getAttribute("Captcha") == null && !request.getRequestURI().equals("/favicon.ico")) {
            String answer = request.getParameter("answer");
            String number = (String) session.getAttribute("number");
            if (number != null && number.equals(answer)) {
                session.setAttribute("Captcha", "complete");
                response.sendRedirect(request.getRequestURI());
            } else {
                String random_number = Integer.toString((new Random()).nextInt(900) + 100);
                session.setAttribute("number", random_number);
                session.setAttribute("captcha_img", Base64.getEncoder().encodeToString(ImageUtils.toPng(random_number)));

                response.setContentType("text/html");
                request.getRequestDispatcher("/static/js/captcha.jsp").forward(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
