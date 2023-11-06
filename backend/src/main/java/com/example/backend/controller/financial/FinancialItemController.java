package com.example.backend.controller.financial;

import com.example.backend.service.financial_item.DeleteFinancialItem;
import com.example.backend.service.financial_item.FindAllFinancialItemsPaged;
import com.example.backend.service.financial_item.SaveFinancialItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/financial-items")
public class FinancialItemController {

    private final FindAllFinancialItemsPaged findAllFinancialItemsPaged;
    private final SaveFinancialItem saveFinancialItem;
    private final DeleteFinancialItem deleteFinancialItem;

    public FinancialItemController(FindAllFinancialItemsPaged findAllFinancialItemsPaged, SaveFinancialItem saveFinancialItem, DeleteFinancialItem deleteFinancialItem) {
        this.findAllFinancialItemsPaged = findAllFinancialItemsPaged;
        this.saveFinancialItem = saveFinancialItem;
        this.deleteFinancialItem = deleteFinancialItem;
    }

    @GetMapping
    public ResponseEntity<Page<FinancialItemDTO>> findAllPaged(Principal principal, @PageableDefault(sort = "creationDate", page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(this.findAllFinancialItemsPaged.execute(principal, pageable));
    }

    @PostMapping
    public ResponseEntity<FinancialItemDTO> save(Principal principal, @RequestBody FinancialItemDTO input) {
        FinancialItemDTO saved = this.saveFinancialItem.execute(principal, input);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();

        return ResponseEntity.created(uri).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FinancialItemDTO> delete(Principal principal, @PathVariable Long id) throws Exception {
        FinancialItemDTO item = this.deleteFinancialItem.execute(principal, id);
        return ResponseEntity.ok(item);
    }

}
