package com.doantotnghiep.nhanambooks.controller;

import com.doantotnghiep.nhanambooks.config.OrderExporter;
import com.doantotnghiep.nhanambooks.enums.OrderStatus;
import com.doantotnghiep.nhanambooks.model.Order;
import com.doantotnghiep.nhanambooks.model.OrderDetail;
import com.doantotnghiep.nhanambooks.model.Product;
import com.doantotnghiep.nhanambooks.repository.OrderRepository;
import com.doantotnghiep.nhanambooks.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final PaypalService paypalService;
    private final OrderRepository orderRepository;
    @GetMapping(value = "/admin/order")
    public String orderList(Model model, @RequestParam(required = false) String keyword,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "20") int size,
                            @RequestParam(defaultValue = "fullname,asc") String[] sort){
        model.addAttribute("PageTitle","Order");
        model.addAttribute("link","admin/order");
        model.addAttribute("pageLink","/admin/order?");
        getListOrder(model,keyword,page,size,sort);
        return "admin/list/order";
    }

    @PostMapping(value = "/admin/order/status/{id}")
    public String orderStatus(@PathVariable UUID id, @RequestParam OrderStatus orderStatus){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()){
            return "redirect:/errore?code=not-found";
        }
        optionalOrder.get().setOrderStatus(orderStatus);
        orderRepository.save(optionalOrder.get());
        return "redirect:/admin/order";
    }

    @GetMapping(value = "/admin/order/detail/{id}")
    public String orderDetail(@PathVariable UUID id, Model model){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()){
            return "redirect:/errore?code=not-found";
        }
        List<OrderDetail> orderDetails = orderRepository.findDetailById(id);
        model.addAttribute("orderDetails", orderDetails);
        return "admin/list/order-detail";
    }

    @GetMapping(value = "/admin/order/export-pdf/{id}")
    public void exportOrder(HttpServletResponse response, @PathVariable UUID id) throws IOException {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isEmpty()){
            response.sendRedirect("/errore?code=not-found");
        }
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Order_" + currentDateTime + id + ".pdf";
        response.setHeader(headerKey, headerValue);

        OrderExporter exporter = new OrderExporter(optionalOrder.get());
        exporter.export(response);
    }

    @GetMapping(value = "/pay-success/{id}")
    public String successPay(@PathVariable UUID id,@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                Optional<Order> order = orderRepository.findById(id);
                if(order.isPresent()){
                    order.get().setOrderStatus(OrderStatus.PAID);
                }
                return "user/success";
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException();
        }
        return "redirect:/";
    }

    @GetMapping(value = "/pay-canel")
    public String cancelPay(){
        return "user/cancel";
    }


    private void getListOrder(Model model, String keyword, int page, int size, String[] sort) {
        try {
            List<Order> orders = new ArrayList<>();
            String sortField = sort[0];
            String sortDirection = sort[1];

            Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, sortField);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

            Page<Order> pageOrders;
            if (keyword == null) {
                pageOrders = orderRepository.findAll(pageable);
            } else {
                pageOrders = orderRepository.findByFullnameContainingIgnoreCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            orders = pageOrders.getContent();

            model.addAttribute("orders", orders);
            model.addAttribute("currentPage", pageOrders.getNumber() + 1);
            model.addAttribute("totalItems", pageOrders.getTotalElements());
            model.addAttribute("totalPages", pageOrders.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
    }




}
