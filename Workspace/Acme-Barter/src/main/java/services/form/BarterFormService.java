package services.form;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import services.BarterService;
import services.ItemService;

import domain.Barter;
import domain.Item;
import domain.form.BarterForm;

@Service
@Transactional
public class BarterFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private BarterService barterService;
	
	@Autowired
	private ItemService itemService;
	
	// Constructors -----------------------------------------------------------

	public BarterFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public BarterForm create() {
		BarterForm result;
		Collection<String> pictures;
		
		pictures = new ArrayList<String>();
		
		result = new BarterForm();
		
		result.setOfferedPictures(pictures);
		result.setRequestedPictures(pictures);
		
		return result;
	}
	
	public Barter reconstruct(BarterForm barterForm) {
		Barter result;
		Item offeredItem, requestedItem;
		
		offeredItem = itemService.create();
		offeredItem.setName(barterForm.getOfferedName());
		offeredItem.setDescription(barterForm.getOfferedDescription());
		offeredItem.setPictures(barterForm.getOfferedPictures());
		offeredItem = itemService.save(offeredItem);
		
		requestedItem = itemService.create();
		requestedItem.setName(barterForm.getRequestedName());
		requestedItem.setDescription(barterForm.getRequestedDescription());
		requestedItem.setPictures(barterForm.getRequestedPictures());
		requestedItem = itemService.save(requestedItem);
		
		result = barterService.create();
		result.setTitle(barterForm.getTitle());
		result.setOffered(offeredItem);
		result.setRequested(requestedItem);
		
		return result;
	}
}
