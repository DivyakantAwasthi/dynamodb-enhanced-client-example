package ca.sunlife.dynamo_db.controller;

import ca.sunlife.dynamo_db.entity.Item;
import ca.sunlife.dynamo_db.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @PostMapping
    public void createItem(@RequestBody Item item) {
        itemRepository.save(item);
    }

    @PostMapping("/batch")
    public void createItems(@RequestBody List<Item> items) {
        itemRepository.saveAll(items);
    }

    @GetMapping("/{id}/{category}")
    public Item getItem(@PathVariable String id, @PathVariable String category) {
        return itemRepository.getById(id, category);
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.getAll();
    }

    @DeleteMapping("/{id}/{category}")
    public void deleteItem(@PathVariable String id, @PathVariable String category) {
        itemRepository.deleteById(id, category);
    }

    @GetMapping("/category/{category}")
    public List<Item> getItemsByCategory(@PathVariable String category) {
        return itemRepository.getItemsByCategory(category);
    }

    @GetMapping("/price-range")
    public List<Item> getItemsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return itemRepository.getItemsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public List<Item> getItemsByManufacturer(@PathVariable String manufacturer) {
        return itemRepository.getItemsByManufacturer(manufacturer);
    }
}
