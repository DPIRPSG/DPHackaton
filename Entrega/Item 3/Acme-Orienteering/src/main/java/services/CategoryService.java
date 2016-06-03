package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Category;

import repositories.CategoryRepository;

@Service
@Transactional
public class CategoryService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CategoryRepository categoryRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------

	public CategoryService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Category create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create categories");
		
		Category result;		
		
		result = new Category();
		
		return result;
	}
	
	public void save(Category category) {
		Assert.notNull(category);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save categories");
		
		categoryRepository.save(category);
	}
	
	public void delete(Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete categories");
		
		categoryRepository.delete(category);
		
	}
	
	public Category findOne(int categoryId) {
		Category result;
		
		result = categoryRepository.findOne(categoryId);
		
		return result;
	}
	
	public Collection<Category> findAll() {
		Collection<Category> result;

		result = categoryRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		categoryRepository.flush();
	}
	
	// DASHBOARD
	
	public Collection<Category> findAllMostFrequentInRaces(){
		Collection<Category> result;
		
		result = categoryRepository.findAllMostFrequentInRaces();
		
		return result;
	}
}
