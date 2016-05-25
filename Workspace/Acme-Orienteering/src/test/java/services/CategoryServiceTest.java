package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Category;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CategoryServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private CategoryService categoryService;
	
	// Other services needed -----------------------
	
	
	// Tests ---------------------------------------
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive test: Se muestran las categorías.
	 */
	@Test
	public void testListCategory1(){
		
		// Declare variable
		Collection<Category> result;
		
		// Load object to test
		authenticate("admin");
		
		// Execution of test
		result = categoryService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 6);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive test: La categoría se crea correctamente.
	 */
	@Test
	public void testCreateCategory1(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 6);
		
		// Execution of test
		category = categoryService.create();
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 7);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se crea correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCategory2(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		//authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		
		// Execution of test
		category = categoryService.create();
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 3);
		//unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se crea correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCategory3(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		authenticate("runner1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		
		// Execution of test
		category = categoryService.create();
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se crea correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCategory4(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		authenticate("manager1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		
		// Execution of test
		category = categoryService.create();
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se crea correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateCategory5(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		authenticate("referee1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		
		// Execution of test
		category = categoryService.create();
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se crea correctamente porque dejamos valores vacíos.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateCategory6(){
		
		// Declare variable
		Collection<Category> result;
		Category category;
		
		// Load object to test
		authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 6);
		
		// Execution of test
		category = categoryService.create();
		//category.setName("Prueba");
		//category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 7);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive test: La categoría se edita correctamente.
	 */
	@Test
	public void testEditCategory1(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		Category category2 = null;
		
		// Load object to test
		authenticate("admin");
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Prueba")){
				category2 = c;
			}
		}
		Assert.notNull(category2);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se edita correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void testEditCategory2(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		Category category2 = null;
		
		// Load object to test
		//authenticate("admin");
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Prueba")){
				category2 = c;
			}
		}
		Assert.notNull(category2);
		//unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se edita correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void testEditCategory3(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		Category category2 = null;
		
		// Load object to test
		authenticate("runner1");
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Prueba")){
				category2 = c;
			}
		}
		Assert.notNull(category2);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se edita correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void testEditCategory4(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		Category category2 = null;
		
		// Load object to test
		authenticate("manager1");
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Prueba")){
				category2 = c;
			}
		}
		Assert.notNull(category2);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se edita correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback
	public void testEditCategory5(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		Category category2 = null;
		
		// Load object to test
		authenticate("referee1");
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		
		category.setName("Prueba");
		category.setDescription("Prueba");
		categoryService.save(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		for(Category c:result){
			if(c.getName().equals("Prueba")){
				category2 = c;
			}
		}
		Assert.notNull(category2);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive test: La categoría se borra correctamente.
	 */
	@Test
	public void testDeleteCategory1(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 6);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se borra correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCategory2(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		//authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 1);
		//unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se borra correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCategory3(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		authenticate("runner1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 1);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se borra correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCategory4(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		authenticate("manager1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 1);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative test: La categoría no se borra correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteCategory5(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		authenticate("referee1");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 2);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 1);
		unauthenticate();
	}
	
	/**
	 * @see 24.e
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las categorías. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive test: La categoría no se borra correctamente porque está siendo usada por otras entidades.
	 */
	@Test(expected = DataIntegrityViolationException.class)
	@Rollback(value = true)
	public void testDeleteCategory6(){
		
		// Declare variable
		Collection<Category> result;
		Category category = null;
		
		// Load object to test
		authenticate("admin");
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 6);
		for(Category c:result){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		// Execution of test
		categoryService.delete(category);
		categoryService.flush();
		
		// Check result
		result = categoryService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
}
