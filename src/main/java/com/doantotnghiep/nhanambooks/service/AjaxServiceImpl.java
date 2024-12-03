package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.dto.AjaxRespone;
import com.doantotnghiep.nhanambooks.dto.CartItemRequest;
import com.doantotnghiep.nhanambooks.dto.DiscountResponse;
import com.doantotnghiep.nhanambooks.dto.OrderRequest;
import com.doantotnghiep.nhanambooks.enums.OrderStatus;
import com.doantotnghiep.nhanambooks.enums.PaymentMethod;
import com.doantotnghiep.nhanambooks.model.*;
import com.doantotnghiep.nhanambooks.repository.*;
import com.doantotnghiep.nhanambooks.user.User;
import com.doantotnghiep.nhanambooks.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AjaxServiceImpl implements AjaxService{
    private final PaypalService paypalService;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public void saveOrder(OrderRequest orderRequest, HttpServletResponse response, HttpServletRequest request, Cart cart) throws IOException {
        Order order = new Order();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().toString().equalsIgnoreCase("anonymousUser")){
            String name = authentication.getName();
            Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
            order.setUserid(optionalUser.get().getId());
        }
        order.setEmail(orderRequest.getEmail());
        order.setFullname(orderRequest.getFullname());
        order.setPhone(orderRequest.getPhone());
        order.setAddress(orderRequest.getAddress());
        List<OrderDetail> orderDetails = new ArrayList<>();
        AtomicBoolean outProduct = new AtomicBoolean(false);
        AtomicInteger total = new AtomicInteger(0);
        cart.getItems().stream().forEach(cartItem -> {
            if(!productRepository.existsToSaveOrder(cartItem.getProduct().getId(),cartItem.getQuantity())){
                System.out.println("Product error");
                outProduct.set(true);
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetails.add(orderDetail);
            total.addAndGet(cartItem.getFinalPrice());
        });
        if(outProduct.get()){
            response.sendRedirect("/error?code=PQ");
            return;
        }
        if(!StringUtils.isEmpty(orderRequest.getDiscountName())){
            if(!checkDiscount(orderRequest.getDiscountName()).isValid()){
                response.sendRedirect("/error?code=DC");
                return;
            };
            order.setDiscount(discountRepository.findByName(orderRequest.getDiscountName()));
        }
        order.setOncreate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotal(total.get());
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        orderDetails.stream().forEach(orderDetail -> {
            productRepository.updateQuantity(orderDetail.getQuantity(),orderDetail.getProduct().getId());
        });
        order.setOrderDetails(orderDetails);
        UUID id = orderRepository.save(order).getId();
        orderDetailRepository.saveAll(orderDetails);
        cart.setItems(new ArrayList<>());
        emailService.sendSimpleEmail(orderRequest.getEmail(),"Cảm ơn","Cảm ơn đã mua sách tại cửa hàng của " +
                "chúng tôi, sách sẽ được giao đến bạn trong thời gian sớm nhất có thể ");
        emailService.sendSimpleEmail("lethilan26032001@gmail.com","Đơn hàng mới","Vui lòng kiểm tra đơn hàng mới trên hệ thống");
        if(orderRequest.getPaymentMethod().equals(PaymentMethod.PAYPAL)){
            try {
                Payment payment = paypalService.createPayment(
                        (double) (total.get()/23000),
                        "USD",
                        PaymentMethod.PAYPAL.toString().toLowerCase(),
                        "sale",
                        "Thanh toán cho order:",
                        Utils.getBaseURL(request) +"/pay-cancel",
                        Utils.getBaseURL(request) + "/pay-success/" + id);
                emailService.sendSimpleEmail(orderRequest.getEmail(),"Cảm ơn","Bạn đã thành toán thành công");
                for(Links links : payment.getLinks()){
                    if(links.getRel().equals("approval_url")){
                        response.sendRedirect(links.getHref());
                        return;
                    }
                }
            } catch (PayPalRESTException e) {
                System.out.println("Pay fail");
            }

        }

        response.sendRedirect("/");
    }

    @Override
    public DiscountResponse checkDiscount(String name) {
        DiscountResponse discountResponse = new DiscountResponse();
        discountResponse.setValid(false);
        if(discountRepository.exitsDiscountCanUse(name)){
            discountResponse.setValid(true);
            discountResponse.setDiscount(discountRepository.findByName(name));
        }
        return discountResponse;
    }

    @Override
    public void deleteProductInCart(Long id, HttpServletResponse response, Cart cart) throws IOException {
        List<CartItem> ds = cart.getItems();
        cart.setItems(ds.stream().filter(item -> item.getProduct().getId() != id).collect(Collectors.toList()));
        response.sendRedirect("/cart");
    }

    @Override
    public Cart addProductToCart(CartItemRequest cartItem, Cart cart) {
        AjaxRespone result = new AjaxRespone();
        Optional<Product> optionalProduct = productRepository.findById(cartItem.getProductid());
        if(optionalProduct.isPresent()){
            cart.addCartItem(optionalProduct.get(), cartItem.getQuantity());
            cart.setTotal(caculateTotal(cart));
            result.setMessage("Thêm sách vào giỏ hàng thành công!");
        }
        return cart;
    }
    private Integer caculateTotal(Cart cart){
        AtomicInteger total = new AtomicInteger();
        cart.getItems().stream().forEach(cartItem -> total.addAndGet(cartItem.getFinalPrice() * cartItem.getQuantity()));
        return total.get();
    }
}
