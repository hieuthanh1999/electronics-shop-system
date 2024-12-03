package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Contact;
import com.doantotnghiep.nhanambooks.model.Product;
import com.doantotnghiep.nhanambooks.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService{

    private final ContactRepository contactRepository;

    @Override
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> getListContact(Model model, String keyword, int page, int size, String[] sort) {
        List<Contact> contacts = new ArrayList<>();
        try {
            String sortField = sort[0];
            String sortDirection = sort[1];

            Sort.Direction direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, sortField);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(order));

            Page<Contact> pageContacts;
            if (keyword == null) {
                pageContacts = contactRepository.findAll(pageable);
            } else {
                pageContacts = contactRepository.findByNameContainingIgnoreCase(keyword, pageable);
                model.addAttribute("keyword", keyword);
            }

            contacts = pageContacts.getContent();

            model.addAttribute("currentPage", pageContacts.getNumber() + 1);
            model.addAttribute("totalItems", pageContacts.getTotalElements());
            model.addAttribute("totalPages", pageContacts.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return contacts;
    }
}
