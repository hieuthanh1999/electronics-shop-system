package com.doantotnghiep.nhanambooks.service;

public interface ReceiveEmailService {
    void save(String name, String email);

    void sendEmail(String title, String content, Long id);
}
