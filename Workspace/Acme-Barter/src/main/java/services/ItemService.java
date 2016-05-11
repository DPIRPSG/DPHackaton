package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ItemRepository;
import domain.AttributeDescription;
import domain.Item;

@Service
@Transactional
public class ItemService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ItemRepository itemRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;

	// Constructors -----------------------------------------------------------

	public ItemService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public Item findOne(int itemId) {
		Item result;

		result = itemRepository.findOne(itemId);

		return result;
	}
	

	public Item create(){
		
		Assert.isTrue(actorService.checkAuthority("USER"), "Only a user can create an item.");
		
		Item result;
		Collection<String> pictures;
		Collection<AttributeDescription> attributesDescription;
		
		result = new Item();
		pictures = new ArrayList<String>();
		attributesDescription = new ArrayList<AttributeDescription>();
		result.setPictures(pictures);
		result.setAttributesDescription(attributesDescription);
		
		return result;
	}
	
	public Item save(Item item){
		
		Assert.notNull(item);
		Assert.isTrue(actorService.checkAuthority("USER") || actorService.checkAuthority("ADMIN"), "Only an user or an admin can save an item");
		Item result;
		
		result = itemRepository.save(item);
		
		return result;
		
	}

	/**
	 * Needed by BarterServiceTest
	 * @return
	 */
	public Collection<Item> findAll(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"));
		Collection<Item> result;
		
		result = itemRepository.findAll();
		
		return result;
	}


	// Other business methods -------------------------------------------------

	public void flush() {
		itemRepository.flush();
	}

	public Collection<Item> findAllByUser() {
		Collection<Item> result, offereds, requesteds;
		int userId;
		
		userId = userService.findByPrincipal().getId();
		
		result = new ArrayList<Item>();
		
		offereds = itemRepository.findAllOfferedByUser(userId);
		requesteds = itemRepository.findAllRequestedByUser(userId);
		
		result.addAll(offereds);
		result.addAll(requesteds);
		
		return result;
	}
}
