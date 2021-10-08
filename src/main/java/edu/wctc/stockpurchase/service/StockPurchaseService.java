package edu.wctc.stockpurchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockPurchaseService {
    private StockPurchaseRepository stockRepo;
    private ObjectMapper objectMapper;

    @Autowired
    public StockPurchaseService(StockPurchaseRepository sr, ObjectMapper om){
        this.stockRepo = sr;
        this.objectMapper = om;
    }

    public StockPurchase patch(int id, JsonPatch patch) throws ResourceNotFoundException, JsonPatchException, JsonProcessingException {
        StockPurchase existingStock = getStock(id);
        JsonNode patched = patch.apply(objectMapper.convertValue(existingStock, JsonNode.class));
        StockPurchase patchedStock = objectMapper.treeToValue(patched, StockPurchase.class);
        stockRepo.save(patchedStock);
        return patchedStock;
    }
    public void delete(int id) throws ResourceNotFoundException {
        if(stockRepo.existsById(id)){
            stockRepo.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Student", "id", id);
        }
    }

    public StockPurchase update(StockPurchase stock) throws ResourceNotFoundException{
        if (stockRepo.existsById(stock.getId())){
            return stockRepo.save(stock);
        } else {
            throw new ResourceNotFoundException("Student", "id", stock.getId());
        }
    }

    public StockPurchase save(StockPurchase stock){
        return stockRepo.save(stock);
    }

    public List<StockPurchase> getAllStocks(){
        List<StockPurchase> list = new ArrayList<>();
        stockRepo.findAll().forEach(list::add);
        return list;
    }

    public StockPurchase getStock(int id) throws ResourceNotFoundException{
        return stockRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Stock", "id", id));
    }
}
