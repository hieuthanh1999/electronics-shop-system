package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Contact;
import org.springframework.ui.Model;

import java.util.List;

public interface ContactService {
    void save(Contact contact);

    List<Contact> getListContact(Model model, String keyword, int page, int size, String[] sort);
}
