package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Information;
import jp.co.example.ecommerce_b.repository.InformationRepository;

@Service
@Transactional
public class InformationService {
	
	@Autowired
	private InformationRepository repository;
	
	public void insertInfo(Information information) {
		repository.insertInfo(information);
	}

}
