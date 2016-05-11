package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Attribute;
import domain.AttributeDescription;
import domain.Barter;
import domain.Item;

import repositories.AttributeDescriptionRepository;

@Service
@Transactional
public class AttributeDescriptionService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AttributeDescriptionRepository attributeDescriptionRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private AttributeService attributeService;
	
	@Autowired
	private BarterService barterService;
	
	// Constructors -----------------------------------------------------------

	public AttributeDescriptionService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public AttributeDescription create(int itemId) {
		Assert.isTrue(actorService.checkAuthority("USER"), "Only an user can create an Attribute's description.");
		
		AttributeDescription result;
		Item item;
		Barter barter;
		
		barter = barterService.findOneByItemId(itemId);
		Assert.isTrue(barter.getUser().getId() == userService.findByPrincipal().getId());
		
		item = itemService.findOne(itemId);
		
		result = new AttributeDescription();
		
		result.setItem(item);
		
		return result;
	}
	
	public AttributeDescription save(AttributeDescription attributeDescription) {
		Assert.isTrue(actorService.checkAuthority("USER"), "Only an user can save an Attribute's description.");
		Assert.notNull(attributeDescription);
		Item item;
		Attribute attribute;
		Barter barter;
		
		barter = barterService.findOneByItemId(attributeDescription.getItem().getId());
		Assert.isTrue(barter.getUser().getId() == userService.findByPrincipal().getId());
		
		if(attributeDescription.getId() == 0) {
			attributeDescription = attributeDescriptionRepository.save(attributeDescription);
			
			item = attributeDescription.getItem();
			attribute = attributeDescription.getAttribute();
			
			item.addAttributeDescription(attributeDescription);
			attribute.addAttributeDescription(attributeDescription);
			
			itemService.save(item);
			attributeService.save(attribute);
		} else {
			AttributeDescription preSave;
			
			preSave = this.findOne(attributeDescription.getId());
			preSave.setValue(attributeDescription.getValue());
			attributeDescription = attributeDescriptionRepository.save(attributeDescription);
		}
		
		return attributeDescription;
	}
	
	public void delete(AttributeDescription attributeDescription) {
		Assert.notNull(attributeDescription);
		Assert.isTrue(attributeDescription.getId() != 0);
		
		Item item;
		Attribute attribute;
		
		item = attributeDescription.getItem();
		attribute = attributeDescription.getAttribute();
		
		item.removeAttributeDescription(attributeDescription);
		attribute.removeAttributeDescription(attributeDescription);
		
		itemService.save(item);
		attributeService.save(attribute);
		
		attributeDescriptionRepository.delete(attributeDescription);
	}
	
	public AttributeDescription findOne(int id) {
		AttributeDescription result;
		
		result = attributeDescriptionRepository.findOne(id);
		
		return result;
	}
	
	public Collection<AttributeDescription> findAll() {
		Collection<AttributeDescription> result;
		
		result = attributeDescriptionRepository.findAll();
		
		return result;
	}


	// Other business methods -------------------------------------------------

	public void flush() {
		attributeDescriptionRepository.flush();
	}

	public Collection<AttributeDescription> findAllByItemId(int itemId) {
		Collection<AttributeDescription> result;
		
		result = attributeDescriptionRepository.findAllByItemId(itemId);
				
		return result;
	}
}
