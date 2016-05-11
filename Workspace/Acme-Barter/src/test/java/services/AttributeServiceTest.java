package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Attribute;
import domain.AttributeDescription;
import domain.Item;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class AttributeServiceTest extends AbstractTest{

	// Service under test -------------------------
	@Autowired
	private AttributeService attributeService;
	
	@Autowired
	private AttributeDescriptionService attributeDescriptionService;
		
	// Other services needed -----------------------
	@Autowired
	private ItemService itemService;
	
	// Tests ---------------------------------------

	/**
	 * Acme-Barter - Level A - 2.1
	 * A user who is authenticated as a user must be able to:
	 * Select the attributes that best describe an item that he or she is including in a barter and provide values.
	 * 
	 * Positive test case: Add an attribute to an item.
	 * 
	 */
	@Test
	public void testAttributeDescribe1(){
		Collection<Item> allItems;
		Collection<Attribute> allAttributes;
		Item item = null;
		AttributeDescription attributeDescription = null;
		Attribute attribute = null;
		
		authenticate("user1");

		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
				break;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		allAttributes = attributeService.findAllNotUsed(item);
		
		for(Attribute a:allAttributes){
			if(a.getName().equals("warranty")){
				attribute = a;
				break;
			}
		}
		
		attributeDescription = attributeDescriptionService.create(item.getId());
		attributeDescription.setAttribute(attribute);
		attributeDescription.setValue("Una cualquiera");
		attributeDescriptionService.save(attributeDescription);
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
				break;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 7);
		
		authenticate(null);
		attributeService.flush();
		attributeDescriptionService.flush();
	}
	
	/**
	 * Acme-Barter - Level A - 2.1
	 * A user who is authenticated as a user must be able to:
	 * Select the attributes that best describe an item that he or she is including in a barter and provide values.
	 * 
	 * Negative test case: An item's attribute is not added because it wasn't an user.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAttributeDescribe2(){
		Collection<Item> allItems;
		Collection<Attribute> allAttributes;
		Item item = null;
		AttributeDescription attributeDescription = null;
		Attribute attribute = null;
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
				break;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		allAttributes = attributeService.findAllNotUsed(item);
		
		for(Attribute a:allAttributes){
			if(a.getName().equals("warranty")){
				attribute = a;
				break;
			}
		}
		
		attributeDescription = attributeDescriptionService.create(item.getId());
		attributeDescription.setAttribute(attribute);
		attributeDescription.setValue("Una cualquiera");
		attributeDescriptionService.save(attributeDescription);
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
				break;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 7);
		
		attributeService.flush();
		attributeDescriptionService.flush();
	}
	
	/**
	 * Acme-Barter - Level A - 2.1
	 * A user who is authenticated as a user must be able to:
	 * Select the attributes that best describe an item that he or she is including in a barter and provide values.
	 * 
	 * Positive test case: An item's attribute is modified.
	 * 
	 */
	@Test
	public void testAttributeDescribe3(){
		Collection<Item> allItems;
		Item item = null;
		AttributeDescription attributeDescription = null;
		
		authenticate("user1");

		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		for(AttributeDescription ad:item.getAttributesDescription()){
			if(ad.getAttribute().getName().equals("size")){
				attributeDescription = ad;
			}
		}
		
		attributeDescription.setValue("Nuevo size");
		attributeDescriptionService.save(attributeDescription);
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		authenticate(null);
		attributeService.flush();
		attributeDescriptionService.flush();
	}
	
	/**
	 * Acme-Barter - Level A - 2.1
	 * A user who is authenticated as a user must be able to:
	 * Select the attributes that best describe an item that he or she is including in a barter and provide values.
	 * 
	 * Negative test case:  An item's attribute is not modified because it wasn't an user.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAttributeDescribe4(){
		Collection<Item> allItems;
		Item item = null;
		AttributeDescription attributeDescription = null;
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		for(AttributeDescription ad:item.getAttributesDescription()){
			if(ad.getAttribute().getName().equals("size")){
				attributeDescription = ad;
			}
		}
		
		attributeDescription.setValue("Nuevo size");
		attributeDescriptionService.save(attributeDescription);
		
		allItems = itemService.findAllByUser();
		
		for(Item i:allItems){
			if(i.getName().equals("Portatil HP")){
				item = i;
			}
		}
		
		Assert.isTrue(item.getAttributesDescription().size() == 6);
		
		attributeService.flush();
		attributeDescriptionService.flush();
	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Positive test case: A new attribute is created.
	 * 
	 */
	@Test
	public void testAttributeCreate1(){
		Collection<Attribute> result;
		Attribute newAttribute;
		
		authenticate("admin");
		
		result = attributeService.findAll();
		Assert.isTrue(result.size() == 6);
		
		newAttribute = attributeService.create();
		newAttribute.setName("Nuevo atributo");
		attributeService.save(newAttribute);
		
		result = attributeService.findAll();
		Assert.isTrue(result.size() == 7);
		
		authenticate(null);
		attributeService.flush();
		attributeDescriptionService.flush();		
	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Negative test case: A new attribute is not created because it wasn't an admin.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAttributeCreate2(){
		Collection<Attribute> result;
		Attribute newAttribute;
				
		result = attributeService.findAll();
		Assert.isTrue(result.size() == 6);
		
		newAttribute = attributeService.create();
		newAttribute.setName("Nuevo atributo");
		attributeService.save(newAttribute);
		
		result = attributeService.findAll();
		Assert.isTrue(result.size() == 7);
		
		attributeService.flush();
		attributeDescriptionService.flush();		
	}

	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Positive test case: An attribute is modified.
	 * 
	 */
	@Test
	public void testAttributeModify1(){
		Collection<Attribute> result;
		Attribute modifyAttribute = null;
		Boolean testResult = false;
		
		authenticate("admin");
		
		result = attributeService.findAll();
		
		for(Attribute a:result){
			if(a.getName().equals("color")){
				modifyAttribute = a;
				break;
			}
		}
		
		modifyAttribute.setName("Nuevo color");
		attributeService.save(modifyAttribute);
		
		result = attributeService.findAll();
		for(Attribute a:result){
			if(a.getName().equals("Nuevo color")){
				modifyAttribute = a;
				testResult = true;
				break;
			}
		}
		
		Assert.isTrue(testResult);
		authenticate(null);
		attributeService.flush();
		attributeDescriptionService.flush();		
	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Negative test case: An attribute is not modified because it wasn't an admin.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAttributeModify2(){
		Collection<Attribute> result;
		Attribute modifyAttribute = null;
		Boolean testResult = false;
				
		result = attributeService.findAll();
		
		for(Attribute a:result){
			if(a.getName().equals("color")){
				modifyAttribute = a;
				break;
			}
		}
		
		modifyAttribute.setName("Nuevo color");
		attributeService.save(modifyAttribute);
		
		result = attributeService.findAll();
		for(Attribute a:result){
			if(a.getName().equals("Nuevo color")){
				modifyAttribute = a;
				testResult = true;
				break;
			}
		}
		
		Assert.isTrue(testResult);
		attributeService.flush();
		attributeDescriptionService.flush();		
	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Positive test case: An attribute is deleted.
	 * 
	 */
	@Test
	public void testAttributeDelete1(){
		Collection<Attribute> result;
		Attribute deleteAttribute = null;
		
		authenticate("admin");
		
		result = attributeService.findAll();
		
		for(Attribute a:result){
			if(a.getName().equals("color")){
				deleteAttribute = a;
				break;
			}
		}
		
		Assert.isTrue(result.size() == 6);
		
		attributeService.delete(deleteAttribute);
		
		attributeService.flush();
		attributeDescriptionService.flush();
		
		result = attributeService.findAll();
		
		Assert.isTrue(result.size() == 5);
		authenticate(null);
		attributeService.flush();
		attributeDescriptionService.flush();		
	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * A user who is authenticated as an administrator must be able to:
	 * Manage the catalog of attributes that the user can use. An attribute may be deleted even in cases in which it is currently used by an item.
	 * 
	 * Negative test case: An attribute is not deleted because it wasn't an admin.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAttributeDelete2(){
		Collection<Attribute> result;
		Attribute deleteAttribute = null;
				
		result = attributeService.findAll();
		
		for(Attribute a:result){
			if(a.getName().equals("color")){
				deleteAttribute = a;
				break;
			}
		}
		
		Assert.isTrue(result.size() == 6);
		
		attributeService.delete(deleteAttribute);
		
		result = attributeService.findAll();
		
		Assert.isTrue(result.size() == 5);
		attributeService.flush();
		attributeDescriptionService.flush();		
	}
}
