package com.example.delivery_service.interceptor;

import com.example.delivery_service.model.Entity.Address;
import com.example.delivery_service.model.Entity.Order;
import com.example.delivery_service.model.Entity.User;
import com.example.delivery_service.model.GoogleMapsApi;
import com.example.delivery_service.services.OrderService;
import com.example.delivery_service.services.StateService;
import com.example.delivery_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class Interceptor implements HandlerInterceptor {

    private final int PAGE_SIZE = 5;

    private final UserService userService;
    private final OrderService orderService;
    private final StateService stateService;

    @Autowired
    public Interceptor(OrderService orderService, UserService userService, StateService stateService) {
        this.orderService = orderService;
        this.userService = userService;
        this.stateService = stateService;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        User user = User.getCurrentUser(userService);
        if(modelAndView != null && user != null){
            Page<Order> ordersPage;

            if(user.getRoles().iterator().next().getName().equals("ADMIN")){
                ordersPage = orderService.getAllOrders(PageRequest.of(0, PAGE_SIZE, Sort.by("createDate")));
                modelAndView.getModel().put("navOrders", ordersPage.getContent());
            }
            else if(user.getRoles().iterator().next().getName().equals("DRIVER")){
                List<Order> orders = orderService.getByDriverIdWithoutDelivered(User.getCurrentUser().getId());
                String officceAddress = Address.getOfficeAddress(stateService).getFormatAddress();

                try {
                    GoogleMapsApi api = new GoogleMapsApi();
                    orders = api.sortOrdersByShortestRoute(officceAddress, orders);
                }
                catch (Exception ex){
                    orders = orderService.getByDriverIdWithoutDelivered(User.getCurrentUser().getId(),
                            PageRequest.of(0, PAGE_SIZE, Sort.by("createDate"))).getContent();
                }

                if(orders.size() > 5){
                    orders = orders.subList(0, PAGE_SIZE - 1);
                }

                modelAndView.getModel().put("navOrders", orders);
            }
            else{
                ordersPage = orderService.getFiltredOrdersByUserId(user.getId(),
                        PageRequest.of(0, PAGE_SIZE, Sort.by("createDate")));
                modelAndView.getModel().put("navOrders", ordersPage.getContent());
            }
        }
    }
}
