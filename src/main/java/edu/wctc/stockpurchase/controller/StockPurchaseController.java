package edu.wctc.stockpurchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {
    private StockPurchaseService stockPurchase;

    @Autowired
    public StockPurchaseController(StockPurchaseService pr){
        this.stockPurchase = pr;
    }

    @GetMapping public List<StockPurchase> getStocks(){
        return stockPurchase.getAllStocks();
    }

    @PostMapping
    public StockPurchase createStock(@RequestBody StockPurchase newStock){
        newStock.setId(0);
        return stockPurchase.save(newStock);
    }

    @DeleteMapping("/{purchaseId}")
    public String deleteStock(@PathVariable String purchaseId){
        try {
            int id = Integer.parseInt(purchaseId);
            stockPurchase.delete(id);
            return "Purchase deleted: ID" + purchaseId + ". No more stonk!";
         }
        catch (NumberFormatException e ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock ID must be a number", e);
        }
        catch (ResourceNotFoundException e)  {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/{purchaseId}")
    public StockPurchase patchStock(@PathVariable String purchaseId, @RequestBody JsonPatch patch){
        try {
            int id = Integer.parseInt(purchaseId);
            return stockPurchase.patch(id, patch);
        }
        catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase ID must be a number", e);
        }
        catch(ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (JsonPatchException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid patch format: " + e.getMessage(), e);
        }
    }

    @PutMapping
    public StockPurchase updateStock(@RequestBody StockPurchase stonk){
        try{
            return stockPurchase.update(stonk);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{purchaseId}")
    public StockPurchase getGet(@PathVariable String purchaseId){
        try{
            int id = Integer.parseInt(purchaseId);
            return stockPurchase.getStock(id);
        } catch (NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ID must be a number", e);
        } catch (ResourceNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
