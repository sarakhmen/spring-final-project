package com.sarakhman.onlineStore.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class ProductPropertyUtil {
    private ProductPropertyUtil(){}

    public static Map<String, Set<String>> retrieveAndProcessSelectedProperties(
            Map<String, Set<String>>allProperties, HttpServletRequest request){
        Map<String, Set<String>>selectedProperties = null;
        String action = request.getParameter("action");

        if(action != null && action.equals("selectByProperties")){
            Enumeration<String> selectedProps = request.getParameterNames();
            selectedProperties = new HashMap<>();
            while (selectedProps.hasMoreElements()) {
                String propertyName = selectedProps.nextElement();
                if (allProperties.containsKey(propertyName)) {
                    Set<String> propertyValues = new HashSet<>(Arrays.asList(request.getParameterValues(propertyName)));
                    selectedProperties.put(propertyName, propertyValues);
                }
            }
        }
        else {
            selectedProperties = (Map<String, Set<String>>) request.getSession().getAttribute("selectedProperties");
            if(selectedProperties == null){
                selectedProperties = new HashMap<>();
            }
        }

        request.getSession().setAttribute("selectedProperties", selectedProperties);
        return selectedProperties;
    }

    public static double[] retrieveAndProcessPriceRange(double priceFrom, double priceTo, HttpServletRequest request){
        double[] price = new double[2];
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        if(action != null && action.equals("selectByProperties")){
            price[0] = priceFrom;
            price[1] = priceTo;
        }
        else{
            if(session.getAttribute("priceFrom") != null && session.getAttribute("priceTo") != null){
                price[0] = (Double)session.getAttribute("priceFrom");
                price[1] = (Double)session.getAttribute("priceTo");
            }
            else{
                price[0] = priceFrom;
                price[1] = priceTo;
            }
        }

        if(price[0] <= price[1]){
            session.setAttribute("priceFrom", price[0]);
            session.setAttribute("priceTo", price[1]);
        }
        else {
            session.setAttribute("priceFrom", null);
            session.setAttribute("priceTo", null);
        }
        return price;
    }
}
