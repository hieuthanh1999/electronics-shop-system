package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Discount;
import com.doantotnghiep.nhanambooks.model.ReceiveEmail;
import com.doantotnghiep.nhanambooks.repository.DiscountRepository;
import com.doantotnghiep.nhanambooks.repository.ReceiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReceiveEmailServiceImpl implements ReceiveEmailService{
    private final ReceiveRepository receiveRepository;
    private final EmailService emailService;
    private final DiscountRepository discountRepository;

    @Override
    public void save(String name, String email) {
        Optional<ReceiveEmail> optionalReceiveEmail = receiveRepository.findByEmail(email);
        if(optionalReceiveEmail.isEmpty()){
            receiveRepository.save(new ReceiveEmail(name,email));
        }
    }

    @Override
    public void sendEmail(String title, String content, Long id) {
        Set<String> receiveEmailSet = receiveRepository.findAllEmail();
        Optional<Discount> optionalDiscount = discountRepository.findById(id);
        if(optionalDiscount.isPresent()){
            Discount discount = optionalDiscount.get();
            content = content.concat("Chúng tôi xin gửi đến các bạn mã giảm giá:"+discount.getName()+" sau giảm:"
                    +discount.getDiscount() + " có hạn đến ngày:" + discount.getExpired());
        }
        for (String email:receiveEmailSet
             ) {
            emailService.sendSimpleEmail(email,title,content);
        }
    }
}
