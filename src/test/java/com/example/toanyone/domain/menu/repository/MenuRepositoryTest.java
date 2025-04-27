package com.example.toanyone.domain.menu.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MenuRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;

}
