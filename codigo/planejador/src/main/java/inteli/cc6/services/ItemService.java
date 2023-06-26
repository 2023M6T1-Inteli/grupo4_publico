package inteli.cc6.services;

import inteli.cc6.models.ItemModel;
import inteli.cc6.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService {

    final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ItemModel save(ItemModel itemModel) {
        return itemRepository.save(itemModel);
    }

    @Transactional
    public ItemModel getById(UUID id) {
        Optional<ItemModel> itemModel = itemRepository.findById(id);
        if(itemModel.isPresent()) {
            return itemModel.get();
        }
        else {
            return null; // Throw an exception or return a suitable default here
        }
    }

    @Transactional
    public List<ItemModel> getAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID id) {
        itemRepository.deleteById(id);
    }
}
