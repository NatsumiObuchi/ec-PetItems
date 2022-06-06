package jp.co.example.ecommerce_b.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.repository.FavoriteRepository;

@Service
public class FavoriteService {

	@Autowired
	private FavoriteRepository repository;

	public void insertFavorite(Favorite favorite) {
		repository.insertFavorite(favorite);
	}

}
